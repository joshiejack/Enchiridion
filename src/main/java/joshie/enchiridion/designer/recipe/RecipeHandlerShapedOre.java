package joshie.enchiridion.designer.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandlerShapedOre extends RecipeHandlerRecipeBase {
    public RecipeHandlerShapedOre() {}
    public RecipeHandlerShapedOre(IRecipe recipe) {
        try {
            init(recipe.getRecipeOutput(), new ArrayList<Object>(Arrays.asList((Object[]) this.input.get(recipe))), width.getInt(recipe));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    protected String getRecipeName() {
        return "ShapedOreRecipe";
    }
    
    @Override
    protected Class getHandlerClass() {
        return this.getClass();
    }

    /** Setup Reflection for grabbing the input and recipe width **/
    private static Field input;
    private static Field width;
    static {
        try {
            input = ShapedOreRecipe.class.getDeclaredField("input");
            input.setAccessible(true);
            width = ShapedOreRecipe.class.getDeclaredField("width");
            width.setAccessible(true);
        } catch (Exception e) {}
    }
}
