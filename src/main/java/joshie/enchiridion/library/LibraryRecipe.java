package joshie.enchiridion.library;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.items.EItems;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.util.SafeStack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = EInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LibraryRecipe extends SpecialRecipe {
    public static final Set<SafeStack> VALID_WOODS = new HashSet<>();
    static final SpecialRecipeSerializer<LibraryRecipe> LIBRARY_SERIALIZER = IRecipeSerializer.register(EInfo.MODID + ":crafting_special_library", new SpecialRecipeSerializer<>(LibraryRecipe::new));

    public LibraryRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return new ResourceLocation(EInfo.MODID, "library");
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return LIBRARY_SERIALIZER;
    }

    private boolean isWood(@Nonnull ItemStack stack) {
        for (SafeStack safe : SafeStack.allInstances(stack)) {
            if (VALID_WOODS.contains(safe)) return true;
        }
        return false;
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
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
    public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
        return getRecipeOutput();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 3;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return new ItemStack(EItems.LIBRARY);
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingInventory inv) {
        NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        list.set(3, getStackOfOne(inv, 3));
        list.set(4, getStackOfOne(inv, 4));
        list.set(5, getStackOfOne(inv, 5));

        return list;
    }

    @Nonnull
    private ItemStack getStackOfOne(CraftingInventory inv, int index) {
        ItemStack ret = inv.getStackInSlot(index).copy();
        ret.setCount(1);
        return ret;
    }

    @SubscribeEvent
    public static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(LIBRARY_SERIALIZER);
    }
}