package joshie.enchiridion.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class HeldHelper {
    @Nonnull
    public static ItemStack getStackFromOrdinal(EntityPlayer player, int id) {
        return getStackFromHand(player, getHandFromOrdinal(id));
    }

    @Nonnull
    public static ItemStack getStackFromHand(EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND)
            return player.getHeldItemMainhand();
        if (hand == EnumHand.OFF_HAND)
            return player.getHeldItemOffhand();

        return ItemStack.EMPTY;
    }

    public static EntityEquipmentSlot getSlotFromHand(EnumHand hand) {
        return hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND;
    }

    public static EnumHand getHandFromOrdinal(int id) {
        return EnumHand.values()[id];
    }
}