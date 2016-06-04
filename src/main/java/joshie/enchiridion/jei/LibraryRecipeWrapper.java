package joshie.enchiridion.jei;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.gui.library.LibraryRecipe;
import joshie.enchiridion.helpers.ItemListHelper;
import joshie.enchiridion.util.SafeStack;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryRecipeWrapper extends VanillaRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final int width = 3;
    private final int height = 3;
    private List<ItemStack> output;
    private List inputs;

    public LibraryRecipeWrapper() {
        inputs = new ArrayList();
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
        List<ItemStack> list = new ArrayList();
        for (SafeStack safe : LibraryRecipe.validWoods) {
            list.add(safe.toStack());
        }

        return list;
    }

    public List<ItemStack> getBooksAsStacks() {
        List<ItemStack> list = new ArrayList();
        for (ItemStack stack : ItemListHelper.allItems()) {
            if (EnchiridionAPI.library.getBookHandlerForStack(stack) != null) {
                list.add(stack);
            }
        }

        return list;
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