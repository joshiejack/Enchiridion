package joshie.enchiridion.wiki.gui.buttons;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiCanvas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public abstract class ButtonBase extends GuiButton {
    protected final float scale;
    public ButtonBase(int id, int x, int y, int width, int height, String text, float scale) {
        super(id, x, y, width, height, I18n.format("enchiridion." + text, new Object[0], 2F));
        this.scale = scale;
    }

    protected boolean isOverButton(Minecraft mc, int x, int y) {
        int realX = x * gui.resolution.getScaleFactor();
        int farLeft = gui.theLeft;
        int extraRight = width * 2 - 2;
        if (width == 1) {
            extraRight = (int) (mc.fontRenderer.getStringWidth(displayString) * scale);
        }

        boolean cont = realX >= xPosition - 3 + farLeft && realX <= xPosition + farLeft + extraRight;
        if (cont) {
            //COPY X
            int realY = y * gui.resolution.getScaleFactor();
            int farTop = gui.theTop;
            int extraBottom = height * 2 - 2;
            if (height == 1) {
                extraBottom = 14;
            }

            return realY >= yPosition + farTop && realY <= yPosition + farTop + extraBottom;
        } else return false;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            fontrenderer.setUnicodeFlag(false);
            mouseDragged(mc, x, y);

            int l = 0xFFFFFF;
            if (isOverButton(mc, x, y)) {
                l = 0xAAAAAA;
            }

            WikiHelper.drawScaledText(scale, displayString, xPosition, yPosition, l);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        return this.enabled && this.visible && isOverButton(mc, x, y);
    }

    public abstract void onClicked();
}
