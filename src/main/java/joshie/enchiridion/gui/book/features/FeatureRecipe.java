package joshie.enchiridion.gui.book.features;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

public class FeatureRecipe extends FeatureItem {
    public transient static final ArrayList<IRecipeHandler> handlers = new ArrayList();

    private String ingredients = "plankWood:plankWood:plankWood:cobblestone:ingotAluminum:cobblestone:cobblestone:dustRedstone:cobblestone";
    private String recipeType = "ShapedOreRecipe";
    private transient int index = 0;
    private transient IRecipeHandler handler;
    private transient IFeatureProvider provider;

    public FeatureRecipe() {}
    public FeatureRecipe(ItemStack item) {
		setItemStack(item);
	}
    
    @Override
    public FeatureRecipe copy() {
        FeatureRecipe recipe = new FeatureRecipe();
        recipe.ingredients = ingredients;
        recipe.recipeType = recipeType;
        recipe.item = item;
        return recipe;
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
	public void setItemStack(ItemStack stack) {
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
        if (provider != null) {
        	update(provider);
        }
    }

    @Override
	public void update(IFeatureProvider position) {
    	provider = position;
    	int xPos = position.getX();
		int yPos = position.getY();
		
        if (handler != null) {
        	double width = position.getWidth();
            position.setHeight(handler.getHeight(width));
            size = handler.getSize(width);
        }
    }

    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
    	if (stack == null && item != null) stack = StackHelper.getStackFromString(item);
        if (handler != null) {
        	EnchiridionAPI.draw.setRenderData(provider.getX(), provider.getY(), provider.getWidth(), provider.getHeight(), size);
            handler.draw();
        } else {
        	buildRecipe(true);
        	update(provider); //Initiate the provider
        }
    }

    @Override
    public void addTooltip(List list, int mouseX, int mouseY) {
        if (!hideTooltip && handler != null) {
        	EnchiridionAPI.draw.setRenderData(provider.getX(), provider.getY(), provider.getWidth(), provider.getHeight(), size);
            handler.addTooltip(list);
        }
    }
}
