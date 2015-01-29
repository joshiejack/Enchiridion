package joshie.enchiridion.designer;

import net.minecraft.client.gui.GuiButton;

public class ButtonText extends GuiButton {
    public ButtonText(int id, int x, int y, String text) {
        super(id, x, y, text);
        this.width = 50;
    }
}
