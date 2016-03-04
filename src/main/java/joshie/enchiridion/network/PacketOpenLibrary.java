package joshie.enchiridion.network;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.lib.GuiIDs;
import joshie.lib.helpers.ClientHelper;
import joshie.lib.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenLibrary extends PenguinPacket {
    public PacketOpenLibrary() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, ClientHelper.getWorld(), 0, 0, 0);
    }
}