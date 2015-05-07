package joshie.enchiridion.designer.recipe;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;

public class RecipeHandlerShapelessVanilla extends RecipeHandlerBase {
    private ShapelessRecipes recipe;

    public RecipeHandlerShapelessVanilla() {}
    public RecipeHandlerShapelessVanilla(ShapelessRecipes recipe) {
        this.recipe = recipe;
    }

    @Override
    public String getUniqueName() {
        String extra = "";
        try {
            ArrayList<ItemStack> input = (ArrayList<ItemStack>) recipe.recipeItems;
            for (ItemStack stack : input) {
                    extra +=stack.getUnlocalizedName();
                
            }
        } catch (Exception e) {}

        return "ShapelessVanilla:" + recipe.getRecipeOutput().getUnlocalizedName() + recipe.getRecipeSize() + extra;
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        for (IRecipe check : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            ItemStack stack = check.getRecipeOutput();
            if (stack == null || (!(check instanceof ShapelessRecipes))) continue;

            if (stack.isItemEqual(output)) {
                list.add(new RecipeHandlerShapelessVanilla((ShapelessRecipes) check));
            }
        }
    }

    private static ItemStack getStack(ArrayList<ItemStack> list, int index) {
        if (index >= list.size()) return null;
        return list.get(index);
    }

    private static final ResourceLocation location = new ResourceLocation("books", "textures/gui/guide_elements.png");

    @Override
    public void draw() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        EnchiridionAPI.draw.drawTexturedRect(0D, 0D, 0, 0, 58, 58);
        EnchiridionAPI.draw.drawTexturedRect(84D, 50D, 1, 63, 20, 14);

        /* EnchiridionAPI.draw.drawStack(recipe.getRecipeOutput(), 115D, 35D, 1.75F);

        try {
            ArrayList<ItemStack> input = (ArrayList<ItemStack>) recipe.recipeItems;
            if (input.size() == 1) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 29D, 48D, 1F);
            } else if (input.size() == 2) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 29D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 1), 56D, 48D, 1F);
            } else if (input.size() == 3) {
                EnchiridionAPI.draw.drawStack(getStack(input, 0), 1D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 1), 29D, 48D, 1F);
                EnchiridionAPI.draw.drawStack(getStack(input, 2), 56D, 48D, 1F);
            } else if (input.size() == 4) {
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
