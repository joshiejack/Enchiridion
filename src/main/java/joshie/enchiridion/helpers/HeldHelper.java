package joshie.enchiridion.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public class HeldHelper {
    @Nonnull
    public static ItemStack getStackFromOrdinal(PlayerEntity player, int id) {
        return getStackFromHand(player, getHandFromOrdinal(id));
    }

    @Nonnull
    public static ItemStack getStackFromHand(PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND)
            return player.getHeldItemMainhand();
        if (hand == Hand.OFF_HAND)
            return player.getHeldItemOffhand();

        return ItemStack.EMPTY;
    }

    public static EquipmentSlotType getSlotFromHand(Hand hand) {
        return hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
    }

    public static Hand getHandFromOrdinal(int id) {
        return Hand.values()[id];
    }
}