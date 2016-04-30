package joshie.enchiridion.gui.book.features.recipe;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.api.recipes.ShapelessRecipe;
import minetweaker.mc19.recipes.ShapelessRecipeAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RecipeHandlerMTAdvancedShapeless extends RecipeHandlerRecipeBase {
    public RecipeHandlerMTAdvancedShapeless() {}
    public RecipeHandlerMTAdvancedShapeless(IRecipe recipe) {
        try {
            ShapelessRecipe mtRecipe = (ShapelessRecipe) shapeless.get(recipe);
            IIngredient[] inputs = mtRecipe.getIngredients();
            ItemStack outStack = MineTweakerMC.getItemStack(mtRecipe.getOutput());
            ArrayList<Object> objects = new ArrayList();
            for (IIngredient ingredient: inputs) {
                if (ingredient instanceof IItemStack) objects.add(MineTweakerMC.getItemStack(ingredient));
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
        return ShapelessRecipeAdvanced.class;
    }

    private static Field shapeless;
    static {
        try {
            shapeless = ShapelessRecipeAdvanced.class.getDeclaredField("recipe");
            shapeless.setAccessible(true);
        } catch (Exception e) {}
    }
}
