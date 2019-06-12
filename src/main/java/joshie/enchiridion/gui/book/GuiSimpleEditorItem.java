package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.helpers.ItemListHelper;
import joshie.enchiridion.util.ELocation;
import joshie.enchiridion.util.IItemSelectable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class GuiSimpleEditorItem extends AbstractGuiOverlay {
    public static final GuiSimpleEditorItem INSTANCE = new GuiSimpleEditorItem();
    private static final ResourceLocation CHECKED = new ELocation("check_selected");
    private static final ResourceLocation UNCHECKED = new ELocation("check_unselected");
    public IItemSelectable selectable = null;
    private ArrayList<ItemStack> sorted;
    private int position;

    protected GuiSimpleEditorItem() {
    }

    public IBookEditorOverlay setItem(IItemSelectable item) {
        selectable = item;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int backgroundColor = 0xFFB0A483; //0xFF48453C
        int fontColor = 0xCE48433D;
        if (mouseX >= EConfig.SETTINGS.editorXPos + 2 && mouseX <= EConfig.SETTINGS.editorXPos + 83) {
            if (mouseY >= EConfig.SETTINGS.toolbarYPos.get() + 9 && mouseY <= EConfig.SETTINGS.toolbarYPos.get() + 23) {
                fontColor = 0xFFB0A483;
                backgroundColor = 0xCE48433D;
            }
        }

        EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos + 2, EConfig.SETTINGS.toolbarYPos.get() + 9, EConfig.SETTINGS.editorXPos + 83, EConfig.SETTINGS.toolbarYPos.get() + 23, backgroundColor, 0xCE48433D);
        EnchiridionAPI.draw.drawSplitScaledString("[b]" + Enchiridion.format("tooltips") + ": [/b]", EConfig.SETTINGS.editorXPos + 4, EConfig.SETTINGS.toolbarYPos.get() + 14, 200, fontColor, 0.5F);
        EnchiridionAPI.draw.drawSplitScaledString("[b]" + selectable.getTooltipsEnabled() + "[/b]", EConfig.SETTINGS.editorXPos + 4 + 55, EConfig.SETTINGS.toolbarYPos.get() + 14, 200, fontColor, 0.5F);
        if (sorted != null) {
            int j = 0;
            int k = 0;
            for (int i = position; i < position + 132; i++) {
                if (i >= 0 && i < sorted.size()) {
                    //1F > 0.75F, 4 > 5, 16 > 13 + EConfig.SETTINGS.editorXPos + 4, -30 > + EConfig.SETTINGS. toolBarYPos + 12
                    EnchiridionAPI.draw.drawStack(sorted.get(i), (j * 12) + EConfig.SETTINGS.editorXPos + 7, (k * 12) + EConfig.SETTINGS.toolbarYPos.get() + 26, 0.75F);

                    j++;

                    if (j > 5) {
                        j = 0;
                        k++;
                    }
                }
            }
        } else updateSearch(GuiSimpleEditor.INSTANCE.getTextField());
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (mouseX >= EConfig.SETTINGS.editorXPos + 2 && mouseX <= EConfig.SETTINGS.editorXPos + 83) {
            if (mouseY >= EConfig.SETTINGS.toolbarYPos.get() + 9 && mouseY <= EConfig.SETTINGS.toolbarYPos.get() + 23) {
                selectable.setTooltips(!selectable.getTooltipsEnabled());

                return true;
            }
        }

        if (sorted != null) {
            int j = 0;
            int k = 0;
            for (int i = position; i < position + 132; i++) {
                if (i >= 0 && i < sorted.size()) {
                    if (mouseX >= (j * 12) + EConfig.SETTINGS.editorXPos + 7 && mouseX <= (j * 12) + EConfig.SETTINGS.editorXPos + 19) {
                        if (mouseY >= (k * 12) + EConfig.SETTINGS.toolbarYPos.get() + 26 && mouseY <= (k * 12) + EConfig.SETTINGS.toolbarYPos.get() + 38) {
                            if (selectable != null) {
                                selectable.setItemStack(sorted.get(i));

                                return true;
                            }
                        }
                    }
                    j++;

                    if (j > 5) {
                        j = 0;
                        k++;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {
        if (mouseX >= EConfig.SETTINGS.editorXPos + 7 && mouseX <= EConfig.SETTINGS.editorXPos + 91) {
            if (mouseY >= EConfig.SETTINGS.toolbarYPos.get() + 14 && mouseY <= EConfig.SETTINGS.toolbarYPos.get() + 302) {
                if (down) {
                    position = Math.min(sorted.size() - 200, position + 6);
                } else {
                    position = Math.max(0, position - 6);
                }
            }
        }
    }

    @Override
    public void updateSearch(String search) {
        ItemListHelper.addInventory();

        if (search == null || search.equals("")) {
            sorted = new ArrayList<>(ItemListHelper.allItems());
        } else {
            position = 0;
            sorted = new ArrayList<>();
            for (ItemStack stack : ItemListHelper.allItems()) {
                try {
                    if (!stack.isEmpty()) {
                        if (stack.getDisplayName().getFormattedText().toLowerCase().contains(search.toLowerCase())) {
                            sorted.add(stack);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
}