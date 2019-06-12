package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;

import java.util.ArrayList;

public class RecipeHandlerShapelessVanilla extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapelessVanilla() {
    }

    public RecipeHandlerShapelessVanilla(IRecipe recipe) {
        try {
            ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
            init(recipe.getRecipeOutput(), new ArrayList<>(shapeless.getIngredients()), 3);
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
        return ShapelessRecipe.class;
    }
}