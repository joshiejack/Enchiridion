package joshie.enchiridion.wiki.mode.edit;

import static joshie.enchiridion.wiki.WikiHelper.gui;

import java.util.List;

import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.mode.ButtonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

public class ButtonWikiEdit extends ButtonBase {
    public Class clazz;

    public ButtonWikiEdit(int id, int x, int y, Class clazz, List list) {
        super(id, x, y, 55, 20, clazz.getSimpleName().substring(7).toLowerCase(), 2F);
        this.clazz = clazz;
        try {
            ((Element)clazz.newInstance()).addEditButtons(list);
        } catch (Exception e) {}
    }
    
    public ButtonWikiEdit(int id, int x, int y, String text) {
        super(id, x, y, 55, 20, text, 2F);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            int xPosition = this.xPosition;
            int yPosition = this.yPosition;
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale);
            xPosition = gui.getLeft(scale, xPosition);
            yPosition = gui.getTop(scale, yPosition);
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = x >= xPosition && y >= yPosition && x < xPosition + this.width && y < yPosition + this.height;
            int k = isOverButton(mc, x, y) ? 2 : 1;
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(xPosition + this.width / 2, yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(mc, x, y);
            int l = 0xFFFFFF;
            if (isOverButton(mc, x, y)) {
                l = 0xAAAAAA;
            }

            this.drawCenteredString(fontrenderer, this.displayString, xPosition + this.width / 2, yPosition + (this.height - 8) / 2, l);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onClicked() {
        try {
            gui.page.add(((Element) clazz.newInstance()).setToDefault());
        } catch (Exception e) {}
    }
}
