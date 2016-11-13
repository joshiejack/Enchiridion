package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.ArrayList;

public class RecipeHandlerShapelessVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapelessVanilla() {
    }

    public RecipeHandlerShapelessVanilla(IRecipe recipe) {
        try {
            ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<>(shapeless.recipeItems), 3);
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