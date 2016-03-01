package joshie.enchiridion.gui.book.features.recipe;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

public class RecipeHandlerShapedVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapedVanilla() {}
    public RecipeHandlerShapedVanilla(IRecipe recipe) {
        try {
            ShapedRecipes shaped = (ShapedRecipes) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<Object>(Arrays.asList((ItemStack[]) shaped.recipeItems)), shaped.recipeWidth);
        } catch (Exception e) { e.printStackTrace(); }
    }
        
    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }
    
    @Override
    protected Class getRecipeClass() {
        return ShapedRecipes.class;
    }
}
