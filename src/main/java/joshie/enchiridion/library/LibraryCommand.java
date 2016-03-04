package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.network.PacketHandler;
import joshie.enchiridion.network.PacketReloadLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class LibraryCommand implements ICommand {
    @Override
    public int compareTo(ICommand o) {
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
            if (parameters[0].equals("refresh")) {
                PacketHandler.sendToServer(new PacketReloadLibrary());
            } else if (parameters[0].equals("lang")) {
                for (ModContainer mod : Loader.instance().getActiveModList()) {
                    LanguageRegistry.instance().loadLanguagesFor(mod, Side.SERVER);
                    break;
                }
            } else if (parameters[0].equals("resources")) {
                Minecraft.getMinecraft().scheduleResourcesRefresh();
            }
        } catch (NumberFormatException e) {}
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
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