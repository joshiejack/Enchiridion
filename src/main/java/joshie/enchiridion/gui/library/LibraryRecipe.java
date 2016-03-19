package joshie.enchiridion.gui.library;

import java.util.HashSet;
import java.util.Set;

import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class LibraryRecipe implements IRecipe {
    public static Set<SafeStack> validWoods = new HashSet();

    private boolean isWood(ItemStack stack) {
        for (SafeStack safe: SafeStack.allInstances(stack)) {
            if (validWoods.contains(safe)) return true;
        }

        return false;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
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
    public ItemStack getCraftingResult(InventoryCrafting inv) {
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

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[] { null, null, null, inv.getStackInSlot(3), inv.getStackInSlot(4), inv.getStackInSlot(5), null, null, null };
    }

}
