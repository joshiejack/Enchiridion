package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.book.IButtonAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

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
        Minecraft mc = Minecraft.getMinecraft();
        try {
            if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, command) != 0) return false;
            mc.thePlayer.sendChatMessage(command);
        } catch (Exception e) {}

        if (close) {
            mc.displayGuiScreen((GuiScreen)null);
        }

        return false;
    }
}