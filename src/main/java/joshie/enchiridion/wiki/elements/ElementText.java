package joshie.enchiridion.wiki.elements;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.util.IColorSelectable;
import joshie.enchiridion.util.IGuiDisablesMenu;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiColorEdit;
import joshie.enchiridion.wiki.gui.GuiTextEdit;
import joshie.enchiridion.wiki.gui.buttons.ButtonBase;
import joshie.enchiridion.wiki.gui.buttons.ButtonWikiTextEdit;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.annotations.Expose;

public class ElementText extends Element implements ITextEditable, IGuiDisablesMenu, IColorSelectable {
    @Expose
    private String text = "";
    @Expose
    private int color = 0xFFFFFFFF;
    private boolean init;

    @Override
    public ElementText setToDefault() {
        this.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        this.width = 200;
        this.height = 100;
        return this;
    }

    @Override
    public void display() {
        if (!init) {
            init = ((text = text.replace("Â§", "\u00a7").replace("§", "\u00a7")) != null);
        }

        String display = text.startsWith("translate:") ? StringEscapeUtils.unescapeJava(StatCollector.translateToLocal(text.replaceFirst("translate:", ""))) : GuiTextEdit.getText(this, text);
        EnchiridionAPI.draw.drawText(display, left, top, color, 0, size);
    }

    @Override
    public void onSelected(int x, int y) {
        WikiHelper.clearEditGUIs();

        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = true;
            }
        }

        GuiColorEdit.select(this);
        GuiTextEdit.select(this, getText().length());
    }

    @Override
    public void onDeselected() {
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = false;
            }
        }

        markDirty();
    }

    @Override
    public void setText(String text) {
        if (isSelected) {
            this.text = text;
            markDirty();
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public boolean canEdit(Object... objects) {
        return isSelected;
    }

    @Override
    public void keyTyped(char character, int key) {
        if (isSelected) {
            if (ClientHelper.isShiftPressed()) {
                if (key == 78) {
                    size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                    return;
                } else if (key == 74) {
                    size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                    return;
                }
            }
        }

        super.keyTyped(character, key);
    }

    @Override
    public void setColor(int hex) {
        this.color = hex;
        markDirty();
    }
}
