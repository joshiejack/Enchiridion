package joshie.enchiridion.network;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenLibrary extends PenguinPacket {
    public PacketOpenLibrary() {}

    @Override
    public void handlePacket(EntityPlayer player) {
        player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, MCClientHelper.getWorld(), 0, 0, 0);
    }
}