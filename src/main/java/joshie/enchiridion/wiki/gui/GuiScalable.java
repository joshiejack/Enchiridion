package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

public class GuiScalable extends GuiScreen {
    public ScaledResolution resolution;
    public int theLeft;
    public int theTop = 150;

    @Override
    public void initGui() {
        resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    }

    public int getLeft() {
        return getLeft(1.0F, 0);
    }

    public int getLeft(float scale, int x) {
        return (int) ((((mc.displayWidth / 2D) - 512) + x) / scale);
    }

    public int getTop(float scale, int y) {
        return (int) ((theTop + y) / scale);
    }

    /** Only perform ONE click action... don't repeat button presses **/
    public boolean clickedButton(int x, int y, int button) {
        if (button == 0) {
            for (int l = 0; l < this.buttonList.size(); ++l) {
                GuiButton guibutton = (GuiButton) this.buttonList.get(l);
                if (guibutton.mousePressed(this.mc, x, y)) {
                    ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
                    if (MinecraftForge.EVENT_BUS.post(event)) break;
                    this.selectedButton = event.button;
                    event.button.func_146113_a(this.mc.getSoundHandler());
                    this.actionPerformed(event.button);
                    if (this.equals(this.mc.currentScreen)) MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this, event.button, this.buttonList));
                    return true;
                }
            }
        }

        return false;
    }

    public boolean releasedButton(int x, int y, int button) {
        if (this.selectedButton != null && button == 0) {
            this.selectedButton.mouseReleased(x, y);
            this.selectedButton = null;
            return true;
        }

        return false;
    }

    public void verticalGradient(int x, int y, int width, int height, int from, int to) {
        drawGradientRect(x, y, width, height, from, to);
    }

    public void horizontalGradient(int x, int y, int width, int height, int from, int to) {
        float a1 = (float) (from >> 24 & 255) / 255.0F;
        float r1 = (float) (from >> 16 & 255) / 255.0F;
        float g1 = (float) (from >> 8 & 255) / 255.0F;
        float b1 = (float) (from & 255) / 255.0F;
        float a2 = (float) (to >> 24 & 255) / 255.0F;
        float r2 = (float) (to >> 16 & 255) / 255.0F;
        float g2 = (float) (to >> 8 & 255) / 255.0F;
        float b2 = (float) (to & 255) / 255.0F;
        disable(GL11.GL_TEXTURE_2D);
        enable(GL11.GL_BLEND);
        disable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r1, g1, b1, a1);
        tessellator.addVertex((double) x, (double) y, (double) this.zLevel);
        tessellator.addVertex((double) x, (double) height, (double) this.zLevel);
        tessellator.setColorRGBA_F(r2, g2, b2, a2);
        tessellator.addVertex((double) width, (double) height, (double) this.zLevel);
        tessellator.addVertex((double) width, (double) y, (double) this.zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        disable(GL11.GL_BLEND);
        enable(GL11.GL_ALPHA_TEST);
        enable(GL11.GL_TEXTURE_2D);
    }
}
