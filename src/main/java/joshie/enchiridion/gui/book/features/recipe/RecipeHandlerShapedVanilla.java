package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipe;

import java.util.ArrayList;

public class RecipeHandlerShapedVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapedVanilla() {
    }

    public RecipeHandlerShapedVanilla(IRecipe recipe) {
        try {
            ShapedRecipe shaped = (ShapedRecipe) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<>(shaped.getIngredients()), shaped.getWidth());
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
        return ShapedRecipe.class;
    }
}