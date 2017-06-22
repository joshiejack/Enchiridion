package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeHandlerShapedVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapedVanilla() {
    }

    public RecipeHandlerShapedVanilla(IRecipe recipe) {
        try {
            ShapedRecipes shaped = (ShapedRecipes) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<>(Collections.singletonList(shaped.recipeItems)), shaped.recipeWidth);
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
        return ShapedRecipes.class;
    }
}