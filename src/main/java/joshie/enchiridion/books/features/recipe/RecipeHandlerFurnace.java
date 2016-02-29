package joshie.enchiridion.books.features.recipe;

import java.util.List;
import java.util.Map;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class RecipeHandlerFurnace extends RecipeHandlerBase {
    private static WrappedFuelStack fuels;

    public RecipeHandlerFurnace() {}
    public RecipeHandlerFurnace(ItemStack output, ItemStack input) {
        stackList.add(new WrappedStack(output, 110D, 32D, 2.5F));
        stackList.add(new WrappedStack(input, 0D, 0D, 2.5F));

        if (fuels == null) fuels = new WrappedFuelStack(0D, 65D, 2.5F);
        stackList.add(fuels);
        addToUnique(Item.itemRegistry.getNameForObject(input.getItem()));
        addToUnique(input.getItemDamage());
    }

    @Override
    public void addRecipes(ItemStack output, List<IRecipeHandler> list) {
        Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
        for (ItemStack key : smeltingList.keySet()) {
            ItemStack stack = smeltingList.get(key);
            if (stack == null) continue;
            if (stack.isItemEqual(output)) {
                list.add(new RecipeHandlerFurnace(stack, key));
            }
        }
    }

    @Override
    public String getRecipeName() {
        return "VanillaFurnace";
    }

    @Override
    public double getHeight(double width) {
        return width * 1.1D;
    }

    @Override
    public double getWidth(double width) {
        return width;
    }

    @Override
    public float getSize(double width) {
        return (float) (width / 110D);
    }

    private int burnTime = 0;

    private int getBurnTimeRemainingScaled(int scale) {
        if (burnTime == 0) {
            burnTime = 2000;
        } else burnTime--;

        return burnTime * scale / 2000;
    }

    private double getHeightForScaled(int i1) {
        return i1;
    }

    protected void drawBackground() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        EnchiridionAPI.draw.drawTexturedRectangle(55D, 38D, 1, 63, 20, 14, 1.75F);
        int i1 = getBurnTimeRemainingScaled(13);
        EnchiridionAPI.draw.drawTexturedReversedRectangle(44D, 56D, 0, 85, 14, 14, 1.75F);
        EnchiridionAPI.draw.drawTexturedReversedRectangle(44D, 46D + 12D, 14, 98 - i1, 14, i1 + 1, 1.75F);
    }
}
