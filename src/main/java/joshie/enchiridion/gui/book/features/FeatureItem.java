package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorItem;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.util.IItemSelectable;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FeatureItem extends FeatureAbstract implements IItemSelectable {
    public String item;
    public boolean hideTooltip;
    public transient float size;

    public FeatureItem(){}
    public FeatureItem(ItemStack item) {
        setItemStack(item);
    }

    public transient ItemStack stack;

    @Override
    public FeatureItem copy() {
        FeatureItem item = new FeatureItem(stack);
        item.hideTooltip = hideTooltip;
        return item;
    }

    @Override
    public String getName() {
        return stack == null ? super.getName() : stack.getDisplayName();
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        double width = position.getWidth();
        position.setHeight(width);
        size = (float) (width / 16D);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (stack == null && item != null) {
            stack = StackHelper.getStackFromString(item);
        } else EnchiridionAPI.draw.drawStack(stack, position.getLeft(), position.getTop(), size);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        this.stack = stack;
        this.item = StackHelper.getStringFromStack(stack);
    }

    @Override
    public void addTooltip(List list, int mouseX, int mouseY) {
        if (!hideTooltip && this.stack != null) {
            list.addAll(stack.getTooltip(MCClientHelper.getPlayer(), false));
        }
    }

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorItem.INSTANCE.setItem(this));
        return false;
    }

    @Override
    public boolean getTooltipsEnabled() {
        return !hideTooltip;
    }
    
    @Override
    public void setTooltips(boolean value) {
        hideTooltip = !value;
    }
}
