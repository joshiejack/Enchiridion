package joshie.enchiridion.library;

import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketLibraryCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class LibraryCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
        return getCommandName().compareTo(o.getCommandName());
    }

    @Override
    public String getCommandName() {
        return "enchiridion";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/enchiridion refresh";
    }

    @Override
    public List getCommandAliases() {
        return new ArrayList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            if (parameters[0].equals("refresh")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("refresh"));
            } if (parameters[0].equals("resources")) {
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            } else if (parameters[0].equals("reset")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("reset"));
            } else if (parameters[0].equals("clear")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("clear"));
            }
        } catch (NumberFormatException e) {}
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        ArrayList<String> string = new ArrayList();
        for (int i = 0; i < 30; i++) {
            string.add("" + i);
        }

        return string;
    }

    @Override
    public boolean isUsernameIndex(String[] parameter, int p_82358_2_) {
        return false;
    }
}