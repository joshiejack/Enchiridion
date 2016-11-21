package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.lang.reflect.Field;

public class RecipeHandlerShapelessOre extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapelessOre() {
    }

    public RecipeHandlerShapelessOre(IRecipe recipe) {
        try {
            init(recipe.getRecipeOutput(), (NonNullList<Object>) input.get(recipe), 3);
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
        return ShapelessOreRecipe.class;
    }

    private static Field input;

    static {
        try {
            input = ShapelessOreRecipe.class.getDeclaredField("input");
            input.setAccessible(true);
        } catch (Exception ignored) {
        }
    }
}