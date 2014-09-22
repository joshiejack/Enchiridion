package joshie.enchiridion.wiki;

import joshie.lib.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class WikiData {
    @Expose
    private String name;
    @Expose
    private String item;
    private ItemStack stack;

    public WikiData() {}

    public WikiData(String name) {
        this.name = name;
    }

    public WikiData(String name, ItemStack stack) {
        this.name = name;
        this.item = StackHelper.getStringFromStack(stack);
        this.stack = stack;
    }

    public String getLocalisation() {
        return name;
    }

    public ItemStack getStack() {
        if (stack != null) {
            return stack;
        } else {
            try {
                return StackHelper.getStackFromString(item);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void setStack(ItemStack stack) {
        this.item = StackHelper.getStringFromStack(stack);
        this.stack = stack;
    }

    public void setTranslation(String text) {
        this.name = text;
    }
}
