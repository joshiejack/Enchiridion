package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.book.IButtonAction;
import net.minecraft.client.Minecraft;

public class ActionExecuteCommand extends AbstractAction {
    public String command;
    public boolean close;

    public ActionExecuteCommand() {
        super("command");
        this.command = "/say hello";
        this.close = false;
    }

    @Override
    public IButtonAction copy() {
        ActionExecuteCommand action = new ActionExecuteCommand();
        action.command = command;
        action.close = close;
        copyAbstract(action);
        return action;
    }

    @Override
    public IButtonAction create() {
        return new ActionExecuteCommand();
    }

    @Override
    public boolean performAction() {
        Minecraft mc = Minecraft.getInstance();
        try {
            String parsedCommand = command.replace("@p", mc.player.getName().getString());
            mc.player.sendChatMessage(parsedCommand);
        } catch (Exception ignored) {
        }

        if (close) {
            mc.displayGuiScreen(null);
        }
        return true;
    }
}