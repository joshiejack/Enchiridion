package joshie.enchiridion.wiki.data;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class DataTab extends Data {
    @Expose
    private String item;
    private ItemStack stack;
    
    public DataTab(){}
    public DataTab(String string, ItemStack stack) {
        super(string);
        
        this.item = StackHelper.getStringFromStack(stack);
        this.stack = stack;
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
}
