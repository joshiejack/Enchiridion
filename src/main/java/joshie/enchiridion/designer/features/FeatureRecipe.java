package joshie.enchiridion.designer.features;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.IRecipeHandler;
import joshie.enchiridion.designer.DrawHelper;
import joshie.enchiridion.designer.recipe.RecipeHandlerFurnace;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapedOre;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapedVanilla;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapelessOre;
import joshie.enchiridion.designer.recipe.RecipeHandlerShapelessVanilla;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;

public class FeatureRecipe extends FeatureItem {
    public static final ArrayList<IRecipeHandler> handlers = new ArrayList();

    @Expose
    private String uniqueRecipe = "ShapedOreRecipe:plankWood:plankWood:plankWood:cobblestone:ingotAluminum:cobblestone:cobblestone:dustRedstone:cobblestone";
    private int index = 0;
    private IRecipeHandler handler;

    static {
        handlers.add(new RecipeHandlerShapedVanilla());
        handlers.add(new RecipeHandlerShapedOre());
        handlers.add(new RecipeHandlerShapelessVanilla());
        handlers.add(new RecipeHandlerShapelessOre());
        handlers.add(new RecipeHandlerFurnace());
    }

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

        int number = -1;
        for (IRecipeHandler handler : recipes) {
            String uniqueName = handler.getUniqueName();
            if (isLoading) {
                if (uniqueName.equals(uniqueRecipe)) {
                    this.handler = handler;
                    return true;
                }
            } else {
                number++;
                if (number == index) {
                    this.handler = handler;
                    this.uniqueRecipe = uniqueName;
                    return true;
                }
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
        // height = (width * 2) / 3;
        //size = (float) (width / 80D);
        if (handler != null) {
            height = handler.getHeight(width);
            size = handler.getSize(width);
        }
    }

    @Override
    public void drawFeature() {
        super.drawFeature();
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
