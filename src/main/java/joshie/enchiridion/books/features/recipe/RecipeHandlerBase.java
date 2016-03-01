package joshie.enchiridion.books.features.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IItemStack;
import joshie.enchiridion.api.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeHandlerBase implements IRecipeHandler {
    protected static final ResourceLocation location = new ResourceLocation("enchiridion", "textures/books/guide_elements.png");
    protected ArrayList<IItemStack> stackList = new ArrayList();
    private String unique;

    public RecipeHandlerBase() {}
    
    public void addToUnique(Object o) {
        String string = "" + o;
        if (unique == null) {
            unique = string;
        } else unique += ":" + string;
    }
    
    @Override
    public void addTooltip(List list) {
        for (IItemStack stack : stackList) {
            if (stack == null || stack.getItemStack() == null) continue;
            if (EnchiridionAPI.draw.isMouseOverIItemStack(stack)) {
                list.addAll(stack.getItemStack().getTooltip(Minecraft.getMinecraft().thePlayer, false));
                break; //Only permit one item to display
            }
        }
    }

    protected final Object getObject(ArrayList<Object> input, int i) {
        if (i >= input.size()) return null;
        for (Object o: input) {
            if (o instanceof ItemStack) {
                ((ItemStack)o).stackSize = 1;
            }
        }
        
        return input.get(i);
    }

    protected final String getMostCommonName(ArrayList<ItemStack> stacks) {
        HashMap<String, Integer> map = new HashMap();
        for (ItemStack stack : stacks) {
            int[] ids = OreDictionary.getOreIDs(stack);
            for (int i : ids) {
                String name = OreDictionary.getOreName(i);
                if (map.containsKey(name)) {
                    int value = map.get(name) + 1;
                    map.put(name, value);
                } else map.put(name, 1);
            }
        }

        String highest = "";
        int highest_value = 0;
        for (String string : map.keySet()) {
            int value = map.get(string);
            if (value > highest_value) {
                highest_value = value;
                highest = string;
            }
        }

        return highest;
    }

    @Override
    public String getUniqueName() {
        return unique;
    }

    @Override
    public void draw() {
        drawBackground();
        for (IItemStack stack : stackList) {
            EnchiridionAPI.draw.drawIItemStack(stack);
        }
    }

    protected abstract void drawBackground();
}
