package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FeatureRecipe extends FeatureItem {
    public transient static final ArrayList<IRecipeHandler> HANDLERS = new ArrayList<>();

    protected String ingredients = "plankWood:plankWood:plankWood:cobblestone:ingotAluminum:cobblestone:cobblestone:dustRedstone:cobblestone";
    protected String recipeType = "ShapedOreRecipe";
    protected transient int index = 0;
    protected transient IRecipeHandler handler;

    public FeatureRecipe() {
    }

    public FeatureRecipe(@Nonnull ItemStack stack) {
        setItemStack(stack);
    }

    @Override
    public FeatureRecipe copy() {
        FeatureRecipe recipe = new FeatureRecipe();
        recipe.ingredients = ingredients;
        recipe.recipeType = recipeType;
        recipe.itemString = itemString;
        recipe.hideTooltip = hideTooltip;
        return recipe;
    }

    private boolean buildRecipe(boolean isLoading) {
        ArrayList<IRecipeHandler> recipes = new ArrayList<>();
        for (IRecipeHandler handler : HANDLERS) {
            handler.addRecipes(stack, recipes);
        }

        //Basic loop checking type and recipe
        if (isLoading) {
            //Loop 1, Exact Match
            for (IRecipeHandler handler : recipes) {
                if (recipeType.equals(handler.getRecipeName())) {
                    if (!ingredients.isEmpty() && ingredients.equals(handler.getUniqueName())) {
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
    public void setItemStack(@Nonnull ItemStack stack) {
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

        //Update the provider
        if (position != null) {
            update(position);
        }
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);

        if (handler != null) {
            double width = position.getWidth();
            position.setHeight(handler.getHeight(width));
            size = handler.getSize(width);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (stack.isEmpty() && itemString != null) stack = StackHelper.getStackFromString(itemString);
        if (handler != null) {
            EnchiridionAPI.draw.setRenderData(position.getLeft(), position.getTop(), position.getWidth(), position.getHeight(), size);
            handler.draw();
        } else {
            buildRecipe(true);
            update(position); //Initiate the provider
        }
    }

    @Override
    public void addTooltip(List<String> list, int mouseX, int mouseY) {
        if (!hideTooltip && handler != null) {
            EnchiridionAPI.draw.setRenderData(position.getLeft(), position.getTop(), position.getWidth(), position.getHeight(), size);
            handler.addTooltip(list);
        }
    }
}