package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledStack;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.IItemSelectable;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiTab;
import joshie.lib.helpers.OpenGLHelper;
import net.minecraft.item.ItemStack;

public class GuiTabs extends GuiExtension implements IItemSelectable, ITextEditable {
    @Override
    public void draw() {
        /* Tab Title */
        drawScaledCentredText(2.5F, GuiTextEdit.getText(this, getTab().getTitle()), 510, 14, 0xFFFFFF);
        
        int i = 0;
        for (WikiTab tab : getMod().getTabs()) {
            int yBonus = isTabSelected(tab) ? -5 : 0;
            int x = (isTabSelected(tab) ? 88 : getIntFromMouse(20 + (50 * i), 60 + (50 * i), -49, -3, 0, 44));
            drawScaledTexture(texture, 20 + (50 * i), -49 + yBonus, x, 0, 44, 47 - yBonus, 1F);
            drawScaledStack(tab.getItemStack(), 28 + (50 * i), -41 + yBonus, 1.85F);
            i++;
        }
    }

    @Override
    public void clicked(int button) {
        if (button == 0) {
            int i = 0;
            for (WikiTab tab : getMod().getTabs()) {
                if (!isTabSelected(tab)) {
                    if (getIntFromMouse(20 + (50 * i), 60 + (50 * i), -49, -3, 0, 44) == 44) {
                        setTab(tab);
                    }
                } else if (isEditMode()) {
                    if (getIntFromMouse(20 + (50 * i), 60 + (50 * i), -54, -3, 0, 44) == 44) {
                        GuiItemSelect.select(this);
                    }
                }

                i++;
            }
        }
        
        if(isEditMode()) {
            if (getIntFromMouse(300, 700, 0, 45, 0, 1) == 1) {
                GuiTextEdit.select(this);
            }
        }
    }

    @Override
    public void setItemStack(ItemStack stack) {
        getTab().setStack(stack);
    }
    
    @Override
    public void setText(String text) {
        if(EClientProxy.font.getStringWidth(text) <= 130) {
            getTab().setTranslation(text);
        }
    }
    
    @Override
    public String getText() {
        return getTab().getTitle();
    }

    @Override
    public boolean canEdit(Object... objects) {
        return true;
    }
}
