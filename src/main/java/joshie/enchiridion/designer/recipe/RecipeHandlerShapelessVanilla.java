package joshie.enchiridion.designer.recipe;

import java.util.ArrayList;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipeHandlerShapelessVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapelessVanilla() {}
    public RecipeHandlerShapelessVanilla(IRecipe recipe) {
        try {
            ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<Object>(shapeless.recipeItems), 3);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    public String getRecipeName() {
        return "ShapelessRecipes";
    }
    
    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }
}
