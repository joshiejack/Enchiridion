package joshie.enchiridion;

import joshie.enchiridion.library.LibraryCommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;

public class ECommands {
    public static void init(ICommandManager iCommand) {
        if(iCommand instanceof ServerCommandManager) {
            ServerCommandManager manager = ((ServerCommandManager)iCommand);
            manager.registerCommand(new LibraryCommand());
        }
    }
}
