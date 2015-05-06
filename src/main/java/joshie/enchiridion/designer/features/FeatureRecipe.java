package joshie.enchiridion.designer.features;

import java.util.ArrayList;

import joshie.enchiridion.api.IRecipeHandler;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapedOre;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapelessOre;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class FeatureRecipe extends FeatureItem {
    public static final ArrayList<IRecipeHandler> handlers = new ArrayList();
    
    @Expose
    private int index = 0;
    private IRecipeHandler handler;

    static {
        handlers.add(new RecipeHandlerShapedOre());
        handlers.add(new RecipeHandlerShapelessOre());
    }

    public FeatureRecipe() {
        drawStack = false;
        width = 128;
        height = 128;
        item = "minecraft:piston";
    }

    private boolean buildRecipe() {
        ArrayList<IRecipeHandler> recipes = new ArrayList();
        for (IRecipeHandler handler : handlers) {
            handler.addRecipes(stack, recipes);
        }
        
        int number = -1;
        for (IRecipeHandler handler : recipes) {
            number++;
            if (number == index) {
                this.handler = handler;
                return true;
            }
        }

        index = 0;
        return true;
    }

    @Override
    protected void setItemStack(ItemStack stack) {
        if (stack.isItemEqual(this.stack)) {
            index++;
        } else index = 0;

        IRecipeHandler previous = handler;
        super.setItemStack(stack);
        buildRecipe();
        if (previous == handler) {
            index = 0;
            buildRecipe();
        }
    }

    @Override
    public void recalculate(int x, int y) {
        super.recalculate(x, y);
        height = (width * 2) / 3;
        size = (float) (width / 80D);
    }

    @Override
    public void drawFeature() {
        super.drawFeature();
        if (handler != null) {
            handler.draw(left, top, height, width, size);
        } else buildRecipe();

        /** Draw Shaped **/

        /** Draw Shapeless **/
    }
}
