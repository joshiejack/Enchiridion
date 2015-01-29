package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import joshie.enchiridion.designer.features.FeatureItem;
import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class DesignerHelper {
    private static GuiDesigner gui;
    private static int x;
    private static int y;

    public static void setGui(GuiDesigner gui, int x, int y) {
        DesignerHelper.gui = gui;
        DesignerHelper.x = x;
        DesignerHelper.y = y;
    }
    
    public static GuiDesigner getGui() {
        return gui;
    }

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        gui.drawRect(x + left, y + top, x + right, y + bottom, color);
    }

    public static void drawSplitString(String text, int left, int top, int wrap, int color) {
        gui.mc.fontRenderer.drawSplitString(text, x + left, y + top, wrap, color);
    }

    public static void drawStack(ItemStack stack, int left, int top, float size) {
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = ClientHelper.getMinecraft();
        FeatureItem.itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) ((x + left) / size), (int) ((y + top) / size));
        FeatureItem.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) ((x + left) / size), (int) ((y + top) / size));
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void drawImage(DynamicTexture texture, ResourceLocation resource, int left, int top, int right, int bottom) {
        start();
        enable(GL_BLEND);
        texture.updateDynamicTexture();
        Tessellator tessellator = Tessellator.instance;
        ClientHelper.getMinecraft().getTextureManager().bindTexture(resource);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + left, y + bottom, 0, 0.0, 1.0);
        tessellator.addVertexWithUV(x + right, y + bottom, 0, 1.0, 1.0);
        tessellator.addVertexWithUV(x + right, y + top, 0, 1.0, 0.0);
        tessellator.addVertexWithUV(x + left, y + top, 0, 0.0, 0.0);
        tessellator.draw();
        disable(GL_BLEND);
        end();
    }

    public static void drawResource(ResourceLocation resource, int left, int top, int width, int height) {
        ClientHelper.getMinecraft().getTextureManager().bindTexture(resource);
        gui.drawTexturedModalRect(x + left, y + top, 0, 0, width, height);
    }
}
