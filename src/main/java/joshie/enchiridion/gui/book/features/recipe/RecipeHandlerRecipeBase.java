package joshie.enchiridion.gui.book.features.recipe;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeHandlerRecipeBase extends RecipeHandlerBase {
    protected void init(ItemStack output, ArrayList<Object> input, int width) {
        int length = input.size();
        stackList.add(new WrappedStack(output, 115D, 31D, 1.75F));
        if (length == 1) {
            stackList.add(new WrappedStack(getObject(input, 0), 29D, 38D, 1F));
        } else if (length == 2) {
            if (width == 1) {
                stackList.add(new WrappedStack(getObject(input, 0), 29D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 38D, 1F));
            } else {
                stackList.add(new WrappedStack(getObject(input, 0), 29D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 56D, 38D, 1F));
            }
        } else if (length == 3) {
            if (width == 1) {
                stackList.add(new WrappedStack(getObject(input, 0), 29D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 2), 29D, 75D, 1F));
            } else {
                stackList.add(new WrappedStack(getObject(input, 0), 1D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 2), 56D, 38D, 1F));
            }
        } else if (length == 4) {
            stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 2), 1D, 38D, 1F));
            stackList.add(new WrappedStack(getObject(input, 3), 29D, 38D, 1F));
        } else if (length == 6) {
            if (width == 2) {
                stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 2), 1D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 3), 29D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 4), 1D, 75D, 1F));
                stackList.add(new WrappedStack(getObject(input, 5), 29D, 75D, 1F));
            } else {
                stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
                stackList.add(new WrappedStack(getObject(input, 2), 56D, 2D, 1F));

                stackList.add(new WrappedStack(getObject(input, 3), 1D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 4), 29D, 38D, 1F));
                stackList.add(new WrappedStack(getObject(input, 5), 56D, 38D, 1F));
            }
        } else {
            stackList.add(new WrappedStack(getObject(input, 0), 1D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 1), 29D, 2D, 1F));
            stackList.add(new WrappedStack(getObject(input, 2), 56D, 2D, 1F));

            stackList.add(new WrappedStack(getObject(input, 3), 1D, 38D, 1F));
            stackList.add(new WrappedStack(getObject(input, 4), 29D, 38D, 1F));
            stackList.add(new WrappedStack(getObject(input, 5), 56D, 38D, 1F));

            stackList.add(new WrappedStack(getObject(input, 6), 1D, 75D, 1F));
            stackList.add(new WrappedStack(getObject(input, 7), 29D, 75D, 1F));
            stackList.add(new WrappedStack(getObject(input, 8), 56D, 75D, 1F));
        }

        for (Object o : input) {
            if (o instanceof List) {
                addToUnique(getMostCommonName(new ArrayList((List)o)));
            } else if (o instanceof ItemStack) {
                addToUnique(Item.itemRegistry.getNameForObject(((ItemStack) o).getItem()));
                addToUnique(((ItemStack) o).getItemDamage());
            }
        }
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        for (IRecipe check : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            ItemStack stack = check.getRecipeOutput();
            if (stack == null || (!check.getClass().equals(getRecipeClass()))) continue;
            if (stack.isItemEqual(output)) {
                try {
                    list.add((IRecipeHandler) Class.forName(getHandlerClass().getName()).getConstructor(IRecipe.class).newInstance(check));
                } catch (Exception e) {}
            }
        }
    }
    
    @Override
    public String getRecipeName() {
        return getRecipeClass().getSimpleName();
    }
    
    protected abstract Class getRecipeClass();

    protected abstract Class getHandlerClass();

    @Override
    public double getHeight(double width) {
        return width / 2D;
    }

    @Override
    public double getWidth(double width) {
        return width;
    }

    @Override
    public float getSize(double width) {
        return (float) (width / 110D);
    }

    @Override
    protected void drawBackground() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        EnchiridionAPI.draw.drawTexturedRectangle(0D, 0D, 0, 0, 58, 58, 1F);
        EnchiridionAPI.draw.drawTexturedRectangle(84D, 42D, 1, 63, 20, 14, 1F);
    }
}
