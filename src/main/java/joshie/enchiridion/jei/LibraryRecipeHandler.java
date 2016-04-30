package joshie.enchiridion.jei;

import joshie.enchiridion.gui.library.LibraryRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.Nonnull;

public class LibraryRecipeHandler implements IRecipeHandler<LibraryRecipe> {
    @Override
    @Nonnull
    public Class<LibraryRecipe> getRecipeClass() {
        return LibraryRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull LibraryRecipe recipe) {
        return new LibraryRecipeWrapper();
    }

    @Override
    public boolean isRecipeValid(@Nonnull LibraryRecipe recipe) {
        return true;
    }
}