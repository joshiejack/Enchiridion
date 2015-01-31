package joshie.enchiridion.wiki.gui.buttons;

import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleAll;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

public abstract class ButtonWikiSwitch extends ButtonBase {
    private final String two;

    public ButtonWikiSwitch(int id, int x, int y, String text, String two) {
        super(id, x, y, 55, 20, text, 2F);
        this.two = I18n.format("enchiridion." + two, new Object[0], 2F);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            int xPosition = this.xPosition;
            int yPosition = this.yPosition;
            start();
            scaleAll(scale);
            xPosition = gui.getLeft(scale, xPosition);
            yPosition = gui.getTop(scale, yPosition);
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(buttonTextures);
            fixColors();
            this.field_146123_n = x >= xPosition && y >= yPosition && x < xPosition + this.width && y < yPosition + this.height;
            int k = isOverButton(mc, x, y) ? 2 : 1;
            enable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(xPosition + this.width / 2, yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(mc, x, y);
            int l = 0xFFFFFF;
            if (isOverButton(mc, x, y)) {
                l = 0xAAAAAA;
            }

            if (isMode1()) {
                this.drawCenteredString(fontrenderer, this.displayString, xPosition + this.width / 2, yPosition + (this.height - 8) / 2, l);
            } else {
                this.drawCenteredString(fontrenderer, this.two, xPosition + this.width / 2, yPosition + (this.height - 8) / 2, l);
            }

            end();
        }
    }

    public abstract boolean isMode1();
}
