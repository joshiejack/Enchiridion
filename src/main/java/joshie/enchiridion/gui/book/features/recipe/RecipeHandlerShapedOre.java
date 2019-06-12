/*package joshie.enchiridion.gui.book.features.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RecipeHandlerShapedOre extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapedOre() {
    }

    public RecipeHandlerShapedOre(IRecipe recipe) {
        try {
            init(recipe.getRecipeOutput(), new ArrayList<>((NonNullList<Ingredient>)input.get(recipe)), width.getInt(recipe));
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
        return ShapedOreRecipe.class;
    }

    /**
     * Setup Reflection for grabbing the input and recipe width
     **/
    /*private static Field input;
    private static Field width;

    static {
        try {
            input = ShapedOreRecipe.class.getDeclaredField("input");
            input.setAccessible(true);
            width = ShapedOreRecipe.class.getDeclaredField("width");
            width.setAccessible(true);
        } catch (Exception ignored) {
        }
    }
}*/