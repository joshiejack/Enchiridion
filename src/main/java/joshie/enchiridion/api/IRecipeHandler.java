package joshie.enchiridion.api;

import net.minecraft.item.crafting.IRecipe;

public interface IRecipeHandler {
    /** This is for handling different recipe type, return null if the recipe wasn't processed **/
    public String[] getRecipeAsString(IRecipe recipe);
}
