package joshie.enchiridion.books.features.recipe;

import java.util.ArrayList;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipeHandlerShapelessVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapelessVanilla() {}
    public RecipeHandlerShapelessVanilla(IRecipe recipe) {
        try {
            ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<Object>(shapeless.recipeItems), 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }

    @Override
    protected Class getRecipeClass() {
        return ShapelessRecipes.class;
    }

}
