package joshie.enchiridion.gui.book.features.recipe;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.api.recipes.ShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RecipeHandlerMTAdvancedShapeless extends RecipeHandlerRecipeBase {
    public RecipeHandlerMTAdvancedShapeless() {}
    public RecipeHandlerMTAdvancedShapeless(IRecipe recipe) {
        try {
            ShapelessRecipe mtRecipe = (ShapelessRecipe) shapeless.get(recipe);
            IIngredient[] inputs = mtRecipe.getIngredients();
            ItemStack outStack = toStack(mtRecipe.getOutput());
            ArrayList<Object> objects = new ArrayList<Object>();
            for (IIngredient ingredient: inputs) {
                if (ingredient instanceof IItemStack) objects.add(toStack((IItemStack) ingredient));
                if (ingredient instanceof IOreDictEntry) objects.add(((IOreDictEntry) ingredient).getName());
            }

            init(outStack, objects, 3);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public String getRecipeName() {
        return "MTAdvancedShapelessdRecipe";
    }

    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }

    @Override
    protected Class getRecipeClass() {
        return clazz;
    }

    private static Field shapeless;
    private static Class clazz;
    static {
        try {
            if (Loader.MC_VERSION.equals("1.9.4")) clazz = Class.forName("minetweaker.mc19.recipes.ShapelessRecipeAdvanced");
            else if (Loader.MC_VERSION.equals("1.10.2")) clazz = Class.forName("minetweaker.mc1102.recipes.ShapelessRecipeAdvanced");
            shapeless = clazz.getDeclaredField("recipe");
            shapeless.setAccessible(true);
        } catch (Exception ignored) {}
    }
}