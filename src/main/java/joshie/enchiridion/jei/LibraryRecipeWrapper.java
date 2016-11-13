package joshie.enchiridion.jei;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.ItemListHelper;
import joshie.enchiridion.library.LibraryRecipe;
import joshie.enchiridion.util.SafeStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final int width = 3;
    private final int height = 3;
    private List<List<ItemStack>> inputs;
    private List<ItemStack> output;

    public LibraryRecipeWrapper() {
        inputs = new ArrayList<>();
        inputs.add(getWoodsAsStacks());
        inputs.add(getWoodsAsStacks());
        inputs.add(getWoodsAsStacks());
        inputs.add(getBooksAsStacks());
        inputs.add(getBooksAsStacks());
        inputs.add(getBooksAsStacks());
        inputs.add(getWoodsAsStacks());
        inputs.add(getWoodsAsStacks());
        inputs.add(getWoodsAsStacks());
        output = Collections.singletonList(new ItemStack(ECommonProxy.book, 1, 1));
    }

    private List<ItemStack> getWoodsAsStacks() {
        return LibraryRecipe.VALID_WOODS.stream().map(SafeStack::toStack).collect(Collectors.toList());
    }

    public List<ItemStack> getBooksAsStacks() {
        return ItemListHelper.allItems().stream().filter(stack -> EnchiridionAPI.library.getBookHandlerForStack(stack) != null).collect(Collectors.toList());
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, output);
    }

    @Nonnull
    @Override
    public List getInputs() {
        return inputs;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return output;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}