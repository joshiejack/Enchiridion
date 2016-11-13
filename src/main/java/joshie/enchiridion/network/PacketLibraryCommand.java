package joshie.enchiridion.network;

import io.netty.buffer.ByteBuf;
import joshie.enchiridion.library.LibraryHelper;
import joshie.enchiridion.library.LibraryInventory;
import joshie.enchiridion.library.ModSupport;
import joshie.enchiridion.network.core.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import static joshie.enchiridion.network.core.PacketPart.REQUEST_SIZE;

public class PacketLibraryCommand extends PenguinPacket {
    private String command;

    public PacketLibraryCommand() {
    }

    public PacketLibraryCommand(String string) {
        command = string;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, command);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        command = ByteBufUtils.readUTF8String(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        //Refresh command resets the modded books allowed
        //Reset command resets whether players have received books or not
        //Clear command clears the inventory of all the players libraries
        switch (command) {
            case "refresh":
                if (!player.worldObj.isRemote) {
                    ModSupport.reset(); //Reset the modded data, then tell clients to reset it and request data
                    PacketHandler.sendToEveryone(new PacketLibraryCommand("refresh"));
                } else {
                    ModSupport.reset(); //Reset the modded data, then request the info from the server
                    PacketHandler.sendToServer(new PacketSyncLibraryAllowed(REQUEST_SIZE));
                }
                break;
            case "reset":
                if (!player.worldObj.isRemote) {
                    LibraryHelper.getAllInventories().forEach(LibraryInventory::reset);
                }
                break;
            case "clear":
                if (!player.worldObj.isRemote) {
                    LibraryHelper.getAllInventories().forEach(LibraryInventory::clear);
                }
                break;
        }
    }
}