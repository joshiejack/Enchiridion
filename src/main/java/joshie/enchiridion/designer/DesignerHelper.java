package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleAll;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glScalef;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.designer.features.FeatureImage;
import joshie.enchiridion.designer.features.FeatureItem;
import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;
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

    public static void drawTexturedRect(int left, int top, int u, int v, int w, int h) {
        gui.drawTexturedModalRect(x + left, y + top, u, v, w, h);
    }

    public static void drawSplitString(String text, int left, int top, int wrap, int color) {
        EClientProxy.font.drawSplitString(text, x + left, y + top, wrap, color);
    }

    public static void drawSplitScaledString(String text, int left, int top, int wrap, int color, float scale) {
        start();
        scaleAll(scale);
        EClientProxy.font.drawSplitString(text, (int) ((x + left) / scale), (int) ((y + top) / scale), wrap, color);
        end();
    }

    public static void drawStack(ItemStack stack, int left, int top, float size) {
        if (stack == null || stack.getItem() == null) return; //Don't draw stacks that don't exist
        int x2 = (int) Math.floor(((x + left) / size));
        int y2 = (int) Math.floor( ((y + top) / size));

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        start();
        GL11.glScalef(size, size, size);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = ClientHelper.getMinecraft();
        FeatureItem.itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x2, y2);
        FeatureItem.itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x2, y2);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        end();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
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

    public static void drawTexturedRect(int left, int top, int u, int v, int w, int h, float size) {
        int x2 = (int) Math.floor(((x + left) / size));
        int y2 = (int) Math.floor( ((y + top) / size));
        
        start();
        enable(GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glScalef(size, size, 1.0F);
        gui.drawTexturedModalRect(x2, y2, u, v, w, h);
        disable(GL_BLEND);
        end();
    }

    public static void drawReversedTexturedRect(int left, int top, int u, int v, int w, int h, float size) {
        int x2 = (int) Math.floor(((x + left) / size));
        int y2 = (int) Math.floor( ((y + top) / size));
        
        start();
        enable(GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glScalef(size, size, 1.0F);
        gui.drawReversedTexturedModalRect(x2, y2, u, v, w, h);
        disable(GL_BLEND);
        end();
    }

    public static void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY) {
        start();
        enable(GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ClientHelper.bindTexture(resource);
        glScalef(scaleX, scaleY, 1.0F);
        gui.drawTexturedModalRect((int) ((x + left) / scaleX), (int) ((y + top) / scaleY), 0, 0, width, height);
        disable(GL_BLEND);
        end();
    }

    private static File last_directory;

    public static FeatureImage loadImage(String dir) {
        //Only allow pngs to be selected, force the window on top.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "gif");
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setAlwaysOnTop(true);
                return dialog;
            }
        };

        if (last_directory == null) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setCurrentDirectory(last_directory);
        }

        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            File books_dir = new File(Enchiridion.root, "books");
            if (!books_dir.exists()) books_dir.mkdir();
            File img_dir = new File(books_dir, "images");
            if (!img_dir.exists()) img_dir.mkdir();
            File book_dir = new File(img_dir, dir);
            if (!book_dir.exists()) book_dir.mkdir();
            File new_location = new File(book_dir, selected.getName());
            try {
                FileUtils.copyFile(selected, new_location);
                return new FeatureImage().setPath(dir + "/" + new_location.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            last_directory = selected.getParentFile();
        }

        return null;
    }

    public static void addTooltip(List<String> tooltip) {
        gui.addTooltip(tooltip);
    }
}
