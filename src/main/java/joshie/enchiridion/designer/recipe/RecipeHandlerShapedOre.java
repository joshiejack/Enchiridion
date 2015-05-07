package joshie.enchiridion.designer.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHandlerShapedOre extends RecipeHandlerBase {
    private ShapedOreRecipe recipe;

    public RecipeHandlerShapedOre() {}
    public RecipeHandlerShapedOre(IRecipe recipe) {
        this.recipe = (ShapedOreRecipe) recipe;
        try {
            init(recipe.getRecipeOutput(), new ArrayList<Object>(Arrays.asList((Object[]) this.input.get(recipe))), width.getInt(recipe));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    @Override
    protected Class getRecipeClass() {
        return this.getClass();
    }

    @Override
    public String getUniqueName() {
        return super.getUniqueName();
    }
    
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
