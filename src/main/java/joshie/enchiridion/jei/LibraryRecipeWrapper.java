package joshie.enchiridion.jei;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.gui.library.LibraryRecipe;
import joshie.enchiridion.library.LibraryRegistry;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LibraryRecipeWrapper extends VanillaRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final int width = 3;
    private final int height = 3;

    public LibraryRecipeWrapper() {}

    @Nonnull
    @Override
    public List getInputs() {
        return Arrays.asList(LibraryRecipe.validWoods, LibraryRecipe.validWoods, LibraryRecipe.validWoods,
                LibraryRegistry.getBooksAsItemStack(), LibraryRegistry.getBooksAsItemStack(), LibraryRegistry.getBooksAsItemStack(),
                LibraryRecipe.validWoods, LibraryRecipe.validWoods, LibraryRecipe.validWoods);
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(new ItemStack(ECommonProxy.book, 1, 1));
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