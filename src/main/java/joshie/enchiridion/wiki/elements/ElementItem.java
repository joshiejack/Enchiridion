package joshie.enchiridion.wiki.elements;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiRender;
import joshie.lib.helpers.ClientHelper;
import joshie.lib.helpers.StackHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.gson.annotations.Expose;

public class ElementItem extends Element {
    public static final RenderBlocks renderer = new RenderBlocks();
    public static final RenderItem itemRenderer = new WikiRender();
    @Expose
    public String item;
    public ItemStack stack;
    public static String search = "";

    @Override
    public ElementItem setToDefault() {
        setStack(new ItemStack(Blocks.stone));
        this.size = 4.0F;
        this.width = (int) (8 * size);
        this.height = (int) (8 * size);
        return this;
    }

    @Override
    public void updateWidth(int change) {
        return;
    }

    @Override
    public void updateHeight(int change) {
        height += change;
        width += change;
        this.size = (float) (width / 8D);
    }
    
    @Override
    public String getTitle() {
        return stack != null? stack.getDisplayName(): super.getTitle();
    }

    public ElementItem setStack(ItemStack stack) {
        this.stack = stack;
        item = Item.itemRegistry.getNameForObject(stack.getItem());
        if (stack.getHasSubtypes()) {
            item += " " + stack.getItemDamage();
        }

        if (stack.hasTagCompound()) {
            item += " " + stack.stackTagCompound.toString();
        }

        return this;
    }

    public void renderStack(ItemStack stack, int x, int y) {        
        if (stack != null && stack.getItem() != null) {
            try {
                GL11.glPushMatrix();
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                if (!ForgeHooksClient.renderInventoryItem(renderer, wiki.mc.getTextureManager(), stack, itemRenderer.renderWithColor, itemRenderer.zLevel, (float)x, (float)y)) {
                    itemRenderer.renderItemIntoGUI(wiki.mc.fontRenderer, wiki.mc.getTextureManager(), stack, x, y, false);
                }
                
                RenderHelper.disableStandardItemLighting();
                GL11.glPopMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            } catch (Exception e) {}
        }
    }

    @Override
    public void display(boolean isEditMode) {
        if (stack == null) {
            stack = StackHelper.getStackFromString(item);
        }
        
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        renderStack(stack, scaledX, scaledY);
        GL11.glPopMatrix();
    }

    private static ArrayList<ItemStack> items;
    private static ArrayList<ItemStack> sorted;
    private static int position;

    @Override
    public void keyTyped(char character, int key) {
        if (isSelected) {
            if (key == 14) {
                delete();
            } else if (key == 28 || key == 156) {
                add("\n");
            } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                add(Character.toString(character));
            }
        }
    }

    public void add(String string) {
        String temp = search + string;
        if (wiki.mc.fontRenderer.getStringWidth(temp) < 145) {
            search += string;
            updateSearch();
        }
    }

    private void delete() {
        if (search != null && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        }

        updateSearch();
    }

    @Override
    public void addEditButtons(List list) {
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

    public void updateSearch() {           
        if (search == null || search.equals("")) {
            sorted = new ArrayList(items);
        } else {
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
    public boolean clickButton(int x, int y, int button) {
        if (sorted == null) updateSearch();
        int j = 0, k = 0;
        for (int i = position; i < position + 175; i++) {
            if (i >= 0 && i < sorted.size()) {
                if (x >= -261 + (j * 32) && x <= -229 + (j * 32)) {
                    if (y >= 0 + (k * 32) && y <= 32 + (k * 32)) {
                        if (isSelected) {
                            setStack(sorted.get(i));
                            return true;
                        }
                    }
                }

                j++;

                if (j > 6) {
                    j = 0;
                    k++;
                }
            }
        }

        return super.clickButton(x, y, button);
    }

    @Override
    public void follow(int x, int y) {
        super.follow(x, y);
        int j = 0;
        int k = 0;
        for (int i = position; i < position + 175; i++) {
            if (i >= 0 && i < sorted.size()) {
                if (x >= wiki.getLeft(1.0F, 16 + (j * 32)) && x <= wiki.getLeft(1.0F, 48 + (j * 32))) {
                    if (y >= wiki.getTop(1.0F, 68 + (k * 32)) && y <= wiki.getTop(1.0F, 90 + (k * 32))) {
                        //wiki.drawTooltip(sorted.get(i));
                    }
                }

                j++;

                if (j > 6) {
                    j = 0;
                    k++;
                }
            }
        }
    }

    @Override
    public void whileSelected() {
        if (mouseX >= -1 && mouseX <= 244) {
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                position = Math.min(items.size(), position + 7);
            } else if (dWheel > 0) {
                position = Math.max(0, position - 7);
            }
        }

        if (sorted == null) {
            updateSearch();
        }

        GL11.glPushMatrix();
        GL11.glScalef(2F, 2F, 2F);
        wiki.mc.fontRenderer.drawSplitString(search, wiki.getLeft(2F, BASE_X - 280), wiki.getTop(2F, BASE_Y - 52), 145, 0xFFFFFFFF);
        GL11.glPopMatrix();

        int j = 0;
        int k = 0;
        for (int i = position; i < position + 175; i++) {
            if (i >= 0 && i < sorted.size()) {
                GL11.glPushMatrix();
                GL11.glScalef(2F, 2F, 2F);
                renderStack(sorted.get(i), wiki.getLeft(2F, (BASE_X - 260 + (j * 32))), wiki.getTop(2F, (BASE_Y + (k * 32))));
                GL11.glPopMatrix();

                j++;

                if (j > 6) {
                    j = 0;
                    k++;
                }
            }
        }
    }
}
