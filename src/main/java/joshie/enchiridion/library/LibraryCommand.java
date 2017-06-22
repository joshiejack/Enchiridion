package joshie.enchiridion.library;

import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketLibraryCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LibraryCommand implements ICommand {
    @Override
    public int compareTo(@Nonnull ICommand command) {
        return getName().compareTo(command.getName());
    }

    @Override
    @Nonnull
    public String getName() {
        return "enchiridion";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/enchiridion refresh";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            if (parameters[0].equals("refresh")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("refresh"));
            }
            if (parameters[0].equals("resources")) {
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            } else if (parameters[0].equals("reset")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("reset"));
            } else if (parameters[0].equals("clear")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("clear"));
            }
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, BlockPos pos) {
        ArrayList<String> string = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            string.add("" + i);
        }

        return string;
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] parameter, int index) {
        return false;
    }
}