package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.util.IItemSelectable;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiItemSelect;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class ElementItem extends Element implements IItemSelectable {
    public static final RenderBlocks renderer = new RenderBlocks();
    public static final RenderItem itemRenderer = new RenderItem();
    @Expose
    public String item;
    public ItemStack stack;

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
        return stack != null ? stack.getDisplayName() : super.getTitle();
    }

    public void setItemStack(ItemStack stack) {
        setStack(stack);
        markDirty();
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
        WikiHelper.renderStack(stack, x, y);
    }

    @Override
    public void display() {
        if (stack == null) {
            stack = StackHelper.getStackFromString(item);
        }
        
        EnchiridionAPI.draw.drawStack(stack, left, top, size);
    }

    @Override
    public void onSelected(int x, int y) {
        GuiItemSelect.select(this);
    }

    @Override
    public void onDeselected() {
        markDirty();
    }

    @Override
    public void addEditButtons(List list) {
        return;
    }
}
