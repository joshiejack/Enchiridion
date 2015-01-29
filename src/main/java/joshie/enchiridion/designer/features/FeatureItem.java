package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class FeatureItem extends FeatureWithText {
    @Expose
    public String item;
    public ItemStack stack;
    public float size;
    private String search = "";

    public FeatureItem() {
        width = 32;
        height = 32;
        item = "minecraft:stone";
    }

    @Override
    public void recalculate(int x, int y) {
        super.recalculate(x, y);
        height = width;
        size = (float) (width / 16D);
    }

    @Override
    public String getTextField() {
        return this.search;
    }

    @Override
    public void setTextField(String str) {
        this.search = str;
    }

    @Override
    public void drawFeature() {
        if (stack == null) {
            stack = StackHelper.getStackFromString(item);
        }

        DesignerHelper.drawStack(stack, left, top, size);
    }

    @Override
    public void loadEditor() {
        // DRAW SELECTION OF ITEM
        //TODO: Display the Search Box with Text
        
        //TODO: Display the Scrollable list of Items
    }
}
