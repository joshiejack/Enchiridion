package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketLibraryCommand;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class LibraryCommand implements ICommand {
    @Override
    public int compareTo(Object o) {
        return 0;
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
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            EPacketHandler.sendToServer(new PacketLibraryCommand());
        } catch (NumberFormatException e) {}
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
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