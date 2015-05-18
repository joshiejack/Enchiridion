package joshie.enchiridion.designer.features;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.IRecipeHandler;
import joshie.enchiridion.designer.DrawHelper;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class FeatureRecipe extends FeatureItem {
    public static final ArrayList<IRecipeHandler> handlers = new ArrayList();

    @Expose
    private String ingredients = "plankWood:plankWood:plankWood:cobblestone:ingotAluminum:cobblestone:cobblestone:dustRedstone:cobblestone";
    @Expose
    private String recipeType = "ShapedOreRecipe";
    private int index = 0;
    private IRecipeHandler handler;

    public FeatureRecipe() {
        drawStack = false;
        width = 128;
        height = 128;
        item = "minecraft:piston";
    }

    private boolean buildRecipe(boolean isLoading) {
        ArrayList<IRecipeHandler> recipes = new ArrayList();
        for (IRecipeHandler handler : handlers) {
            handler.addRecipes(stack, recipes);
        }

        //Basic loop checking type and recipe
        if (isLoading) {
            //Loop 1, Exact Match
            for (IRecipeHandler handler : recipes) {
                if (recipeType.equals(handler.getRecipeName())) {
                    if (ingredients.equals(handler.getUniqueName())) {
                        this.handler = handler;
                        return true;
                    }
                }
            }

            //Loop 2, Fuzzy Match
            for (IRecipeHandler handler : recipes) {
                if (recipeType.equals(handler.getRecipeName())) {
                    this.handler = handler;
                    return true;
                }
            }
        }

        //General Search
        int number = -1;
        for (IRecipeHandler handler : recipes) {
            number++;
            if (number == index) {
                this.handler = handler;
                this.recipeType = handler.getRecipeName();
                this.ingredients = handler.getUniqueName();
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
        buildRecipe(false);
        if (previous == handler) {
            index = 0;
            buildRecipe(false);
        }
    }

    @Override
    public void recalculate(int x, int y) {
        super.recalculate(x, y);
        if (handler != null) {
            height = handler.getHeight(width);
            size = handler.getSize(width);
        }
    }

    @Override
    public void drawFeature() {
        super.drawFeature();
        if (EConfig.RECIPE_DEBUG) buildRecipe(true);
        if (handler != null) {
            DrawHelper.update(true, left, top, height, width, size);
            handler.draw();
        } else buildRecipe(true);
    }

    @Override
    public void addTooltip(List list) {
        if (handler != null) {
            DrawHelper.update(true, left, top, height, width, size);
            handler.addTooltip(list);
        }
    }
}
