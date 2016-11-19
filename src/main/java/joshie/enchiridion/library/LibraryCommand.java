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
    public int compareTo(@Nonnull ICommand o) {
        return getName().compareTo(o.getName());
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
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length != 1) return;
        try {
            if (args[0].equals("refresh")) {
                PacketHandler.sendToServer(new PacketLibraryCommand("refresh"));
            }
            switch (args[0]) {
                case "resources":
                    Minecraft.getMinecraft().scheduleResourcesRefresh();
                    break;
                case "reset":
                    PacketHandler.sendToServer(new PacketLibraryCommand("reset"));
                    break;
                case "clear":
                    PacketHandler.sendToServer(new PacketLibraryCommand("clear"));
                    break;
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