package joshie.enchiridion.library;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class LibraryRecipe implements IRecipe {
    public static final Set<SafeStack> VALID_WOODS = new HashSet<>();

    private boolean isWood(ItemStack stack) {
        for (SafeStack safe : SafeStack.allInstances(stack)) {
            if (VALID_WOODS.contains(safe)) return true;
        }

        return false;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) return false;
            if (!isWood(stack)) return false;
        }

        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i + 3);
            if (stack == null) return false;
            else {
                if (EnchiridionAPI.library.getBookHandlerForStack(stack) == null) return false;
            }
        }

        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i + 6);
            if (stack == null) return false;
            if (!isWood(stack)) return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return matches(inv, null) ? getRecipeOutput() : null;
    }

    @Override
    public int getRecipeSize() {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ECommonProxy.book, 1, 1);
    }

    private ItemStack getStackOfOne(InventoryCrafting inv, int index) {
        ItemStack ret = inv.getStackInSlot(index).copy();
        ret.stackSize = 1;
        return ret;
    }

    @Override
    @Nonnull
    public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inv) {
        return new ItemStack[]{null, null, null, getStackOfOne(inv, 3), getStackOfOne(inv, 4), getStackOfOne(inv, 5), null, null, null};
    }
}