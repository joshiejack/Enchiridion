/*
package joshie.enchiridion.gui.book.features.recipe;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.api.recipes.ShapedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RecipeHandlerMTAdvancedShaped extends RecipeHandlerRecipeBase {
    public RecipeHandlerMTAdvancedShaped() {
    }

    public RecipeHandlerMTAdvancedShaped(IRecipe recipe) {
        try {
            ShapedRecipe mtRecipe = (ShapedRecipe) shaped.get(recipe);
            IIngredient[] inputs = mtRecipe.getIngredients();
            ItemStack outStack = toStack(mtRecipe.getOutput());
            ArrayList<Object> objects = new ArrayList<>();
            for (IIngredient ingredient : inputs) {
                if (ingredient instanceof IItemStack) objects.add(toStack((IItemStack) ingredient));
                if (ingredient instanceof IOreDictEntry) objects.add(((IOreDictEntry) ingredient).getName());
            }

            init(outStack, objects, mtRecipe.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRecipeName() {
        return "MTAdvancedShapedRecipe";
    }

    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }

    @Override
    protected Class getRecipeClass() {
        return clazz;
    }

    private static Field shaped;
    private static Class clazz;

    static {
        try {
            if (Loader.MC_VERSION.equals("1.11")) //TODO
                clazz = Class.forName("minetweaker.mc111.recipes.ShapedRecipeAdvanced");

            shaped = clazz.getDeclaredField("recipe");
            shaped.setAccessible(true);
        } catch (Exception ignored) {
        }
    }
}*/
