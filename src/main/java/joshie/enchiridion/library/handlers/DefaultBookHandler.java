package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.IBookHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DefaultBookHandler implements IBookHandler {
    @Override
    public String getName() {
        return "default";
    }

    @Override
    public void handle(ItemStack stack, World world, EntityPlayer player) {
        stack.getItem().onItemRightClick(stack, world, player);
    }
}
