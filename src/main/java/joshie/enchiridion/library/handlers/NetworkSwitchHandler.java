package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketNetworkSwitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NetworkSwitchHandler implements IBookHandler {
    @Override
    public String getName() {
        return "network";
    }

    @Override
    public void handle(ItemStack stack, World world, EntityPlayer player) {
        player.closeScreen();

        EPacketHandler.sendToServer(new PacketNetworkSwitch(stack, true));
    }
}
