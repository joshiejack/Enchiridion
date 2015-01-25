package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledStack;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;

import java.util.ArrayList;
import java.util.Iterator;

import joshie.enchiridion.api.IGuiDisablesMenu;
import joshie.enchiridion.api.IItemSelectable;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiItemSelect extends GuiExtension implements ITextEditable, IGuiDisablesMenu {
    public static IItemSelectable selectable = null;
    private static ArrayList<ItemStack> items;
    private static ArrayList<ItemStack> sorted;
    private static String search = "";
    private static int position;

    public GuiItemSelect() {
        /** On creation, create our lovely items list */
        items = new ArrayList();
        for (ItemStack stack : ClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                items.add(stack);
            }
        }

        Iterator iterator = Item.itemRegistry.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (item == null) {
                continue;
            }

            for (CreativeTabs tab : item.getCreativeTabs()) {
                item.getSubItems(item, tab, items);
            }
        }
    }
    
    public static void select(IItemSelectable selectable) {
        /** Clear all history when selecting this gui **/
        WikiHelper.clearEditGUIs();
        GuiItemSelect.selectable = selectable;
    }
    
    public static void clear() {
        GuiItemSelect.selectable = null;
        GuiItemSelect.search = "";
        GuiItemSelect.position = 0;
    }

    public void updateSearch() {
        if (search == null || search.equals("")) {
            sorted = new ArrayList(items);
        } else {
            position = 0;
            sorted = new ArrayList();
            for (ItemStack stack : items) {
                if (stack != null && stack.getItem() != null) {
                    if (stack.getDisplayName().toLowerCase().contains(search.toLowerCase())) {
                        sorted.add(stack);
                    }
                }
            }
        }
    }

    @Override
    public void draw() {
        if (selectable != null) {
            //Draw the Search box
            drawScaledTexture(texture, 10, 1, 0, 147, 254, 39, 1F);
            drawScaledTexture(texture, 126, 1, 100, 147, 154, 39, 1F);
            
            if (sorted == null) {
                updateSearch();
            }

            drawScaledText(2F, GuiTextEdit.getText(this, search), 19, 13, 0xFFFFFFFF);

            int j = 0;
            int k = 0;
            for (int i = position; i < position + 200; i++) {
                if (i >= 0 && i < sorted.size()) {
                    drawScaledStack(sorted.get(i), 10 + (j * 32), 50 + (k * 32), 2F);

                    j++;

                    if (j > 7) {
                        j = 0;
                        k++;
                    }
                }
            }
        }
    }

    @Override
    public void clicked(int button) {
        if (selectable != null) {
            if (button == 0) {
                if (sorted == null) updateSearch();
                int j = 0, k = 0;
                for (int i = position; i < position + 175; i++) {
                    if (i >= 0 && i < sorted.size()) {
                        if (getIntFromMouse(10 + (j * 32), 10 + 32 + (j * 32), 50 + (k * 32), 50 + 32 + (k * 32), 0, 1) == 1) {
                            selectable.setItemStack(sorted.get(i));
                        }

                        j++;

                        if (j > 7) {
                            j = 0;
                            k++;
                        }
                    }
                }
                
                if (isEditMode()) {
                    if (getIntFromMouse(0, 299, 0, 45, 0, 1) == 1) {
                        GuiTextEdit.select(this);
                    }
                }
            }

        }
    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (selectable != null) {
            if (mouseX >= 5 && mouseX <= 270) {
                if (scrolledDown) {
                    position = Math.min(sorted.size() - 200, position + 8);
                } else {
                    position = Math.max(0, position - 8);
                }
            }
        }
    }

    @Override
    public void setText(String text) {
        this.search = text;
        this.updateSearch();
    }

    @Override
    public String getText() {
        return this.search;
    }
    
    @Override
    public boolean canEdit(Object... objects) {
        return true;
    }
}
