package joshie.enchiridion.wiki.recipe;

import joshie.enchiridion.api.IRecipeHandler;
import joshie.lib.helpers.StackHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class VanillaRecipeHandler implements IRecipeHandler {
    @Override
    public String[] getRecipeAsString(IRecipe recipe) {
        if (recipe instanceof ShapedOreRecipe) {
            String[] items = new String[9];
            Object[] input = ((ShapedOreRecipe) recipe).getInput();
            for (int i = 0; i < input.length; i++) {
                items[i] = StackHelper.getStringFromObject(input[i]);
            }
            
            return items;
        }

        return null;
    }
}
