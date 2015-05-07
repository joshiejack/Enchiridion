package joshie.enchiridion.designer.recipe;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;

public class RecipeHandlerShapedVanilla extends RecipeHandlerBase {
    private ShapedRecipes recipe;

    public RecipeHandlerShapedVanilla() {}
    public RecipeHandlerShapedVanilla(ShapedRecipes recipe) {
        this.recipe = recipe;
    }

    @Override
    public String getUniqueName() {
        String extra = "";
        try {
            ItemStack[] input = recipe.recipeItems;
            for (ItemStack stack : input) {
                extra += stack.getUnlocalizedName();
            }
        } catch (Exception e) {}

        return "ShapedVanilla:" + recipe.getRecipeOutput().getUnlocalizedName() + recipe.getRecipeSize() + extra;
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        for (IRecipe check : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            ItemStack stack = check.getRecipeOutput();
            if (stack == null || (!(check instanceof ShapedRecipes))) continue;

            if (stack.isItemEqual(output)) {
                list.add(new RecipeHandlerShapedVanilla((ShapedRecipes) check));
            }
        }
    }

    private static ItemStack getStack(ItemStack[] array, int i) {
        if (i >= array.length || array[i] == null) return null;
        ItemStack stack = array[i].copy();
        stack.stackSize = 1;
        return stack;
    }

    private static final ResourceLocation location = new ResourceLocation("books", "textures/gui/guide_elements.png");

    @Override
    public void draw() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        EnchiridionAPI.draw.drawTexturedRect(0D, 0D, 0, 0, 58, 58);
        EnchiridionAPI.draw.drawTexturedRect(84D, 50D, 1, 63, 20, 14);

        /*EnchiridionAPI.draw.drawStack(recipe.getRecipeOutput(), 115D, 35D, 1.75F);

        try {
            ItemStack[] input = recipe.recipeItems;
            if (input.length == 1) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 29D, 48D, 1F);
            } else if (input.length == 2) {
                if (recipe.recipeWidth == 1) {
                    EnchiridionAPI.draw.drawStack(getStack(input, 0), 29D, 2D, 1F);
                    EnchiridionAPI.draw.drawStack(getStack(input, 1), 29D, 48D, 1F);
                } else {
                    EnchiridionAPI.draw.drawStack(getStack(input, 0), 29D, 48D, 1F);
                    EnchiridionAPI.draw.drawStack(getStack(input, 1), 56D, 48D, 1F);
                }
            } else if (input.length == 3) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 1D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 1), 29D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 2), 56D, 48D, 1F);
            } else if (input.length == 4) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 1D, 2D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 1), 29D, 2D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 2), 1D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 3), 29D, 48D, 1F);
            } else {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 1D, 2D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 1), 29D, 2D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 2), 56D, 2D, 1F);

                EnchiridionAPI.draw.drawStack(getStack(input, 3), 1D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 4), 29D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 5), 56D, 48D, 1F);

                EnchiridionAPI.draw.drawStack(getStack(input, 6), 1D, 92D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 7), 29D, 92D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 8), 56D, 92D, 1F);
            }
        } catch (Exception e) {} */
    }
}
