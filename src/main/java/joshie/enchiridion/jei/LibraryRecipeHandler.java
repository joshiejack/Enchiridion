package joshie.enchiridion.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import javax.annotation.Nonnull;

public class LibraryRecipeHandler implements IRecipeHandler<LibraryRecipeWrapper> {
    @Override
    @Nonnull
    public Class<LibraryRecipeWrapper> getRecipeClass() {
        return LibraryRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull LibraryRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull LibraryRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull LibraryRecipeWrapper recipe) {
        return true;
    }
}