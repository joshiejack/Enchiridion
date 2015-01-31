package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SwitchBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "switch";
    }

    @Override
    public void handle(ItemStack stack, World world, EntityPlayer player) {
        ItemStack previous = player.getCurrentEquippedItem();
        if (previous != null) previous = previous.copy();
        player.setCurrentItemOrArmor(0, stack);
        stack.getItem().onItemRightClick(stack, world, player);
        player.setCurrentItemOrArmor(0, previous);
    }
}
