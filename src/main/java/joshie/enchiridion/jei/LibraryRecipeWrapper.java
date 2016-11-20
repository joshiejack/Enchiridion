package joshie.enchiridion.jei;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.ItemListHelper;
import joshie.enchiridion.library.LibraryRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final List<ItemStack> output;
    private final List<List<ItemStack>> inputs;

    LibraryRecipeWrapper() {
        inputs = new ArrayList<>();
        output = Collections.singletonList(new ItemStack(ECommonProxy.book, 1, 1));
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, buildOrGetInput());
        ingredients.setOutputs(ItemStack.class, output);
    }

    private List<List<ItemStack>> buildOrGetInput() {
        if (inputs.size() != 0) return inputs;
        else {
            List<ItemStack> wood = new ArrayList<>();
            List<ItemStack> books = new ArrayList<>();
            LibraryRecipe.validWoods.stream().forEach((l) -> wood.add(l.toStack()));
            ItemListHelper.allItems().stream().filter((l) -> EnchiridionAPI.library.getBookHandlerForStack(l) != null).forEach((books::add));

            //Build the list
            if (wood.size() > 0 && books.size() > 0) {
                for (int i = 0; i < 3; i++) inputs.add(wood);
                for (int i = 0; i < 3; i++) inputs.add(books);
                for (int i = 0; i < 3; i++) inputs.add(wood);
            }

            return inputs;
        }
    }

    @Nonnull
    @Override
    public List<List<ItemStack>> getInputs() {
        return inputs;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return output;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

}