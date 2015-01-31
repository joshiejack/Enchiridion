package joshie.enchiridion.library.handlers;

import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketNetworkSwitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class NetworkSwitchHandler implements IBookHandler {
    public NetworkSwitchHandler() {
        EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.SERVER);
        EPacketHandler.registerPacket(PacketNetworkSwitch.class, Side.CLIENT);
    }

    @Override
    public void handle(ItemStack stack, World world, EntityPlayer player) {
        player.closeScreen();
        
        EPacketHandler.sendToServer(new PacketNetworkSwitch(stack, true));
    }
}
