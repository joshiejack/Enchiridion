package joshie.enchiridion.library;

import joshie.enchiridion.ECommonHandler;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber
public class LibraryRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public static final Set<SafeStack> VALID_WOODS = new HashSet<>();


    private boolean isWood(@Nonnull ItemStack stack) {
        for (SafeStack safe : SafeStack.allInstances(stack)) {
            if (VALID_WOODS.contains(safe)) return true;
        }
        return false;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty() || !isWood(stack)) return false;
        }

        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i + 3);
            if (stack.isEmpty()) return false;
            else {
                if (EnchiridionAPI.library.getBookHandlerForStack(stack) == null) return false;
            }
        }

        for (int i = 0; i < 3; i++) {
            ItemStack stack = inv.getStackInSlot(i + 6);
            if (stack.isEmpty()) return false;
            if (!isWood(stack)) return false;
        }

        return true;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return getRecipeOutput();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 3;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return new ItemStack(ECommonHandler.book, 1, 1);
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        list.set(3, getStackOfOne(inv, 3));
        list.set(4, getStackOfOne(inv, 4));
        list.set(5, getStackOfOne(inv, 5));

        return list;
    }

    @Override
    public boolean isDynamic() {
        return true; //Hide recipe, since the recipe book can't handle custom IRecipes properly currently
    }

    @Nonnull
    private ItemStack getStackOfOne(InventoryCrafting inv, int index) {
        ItemStack ret = inv.getStackInSlot(index).copy();
        ret.setCount(1);
        return ret;
    }

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<IRecipe> event) {
        LibraryRecipe recipe = new LibraryRecipe();
        recipe.setRegistryName(new ResourceLocation(EInfo.MODID, "library"));
        if (EConfig.SETTINGS.addOreDictionaryRecipeForLibrary) {
            event.getRegistry().register(recipe);
        }
    }
}