package joshie.enchiridion.gui.book;

import joshie.enchiridion.data.book.Template;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.util.TextEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class GuiSimpleEditorTemplateSave extends GuiSimpleEditorAbstract implements ITextEditable {
    public static final GuiSimpleEditorTemplateSave INSTANCE = new GuiSimpleEditorTemplateSave();
    private String displayname = "New Template";
    private String sanitized;
    private Template template;
    private boolean isTakingScreenshot = false;

    @Override
    public void setTextField(String text) {
        displayname = text;
        if (displayname.contains("\n")) { //If we pressed enter, clear out the return
            text = text.replace("\n", "");
            sanitized = text.replaceAll("[^A-Za-z0-9]", "_");
            sanitized = sanitized + "_" + System.currentTimeMillis(); //Add the time to make sure it remains unique, To avoid name clashes when saving
            template = new Template(sanitized, text, GuiBook.INSTANCE.getPage());
            GuiSimpleEditorTemplate.INSTANCE.registerTemplate(template);
            isTakingScreenshot = true;
        }
    }

    private void saveScreenshot(String name) {
        GL11.glReadBuffer(GL11.GL_FRONT);
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        int screenWidth = mc.displayWidth;
        int screenHeight = mc.displayHeight;

        int scale = res.getScaleFactor();
        int width = 450 * scale;
        int height = 255 * scale;

        int left = screenWidth / 2 - width / 2;
        int top = (screenHeight / 2 - height / 2) - (5 * scale);

        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(left, top, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        File file = new File(FileHelper.getTemplatesDirectory(), name + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(resizeImage(image, BufferedImage.TYPE_INT_RGB, 280, 158), format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, type);
        Graphics2D g = resized.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resized;
    }

    private void saveTemplate(String name, Template template) {
        try {
            File toSave = new File(FileHelper.getTemplatesDirectory(), name + ".json");
            Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
            writer.write(GsonHelper.getModifiedGson().toJson(template));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTextField() {
        return displayname;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (!isTakingScreenshot) {
            TextEditor.INSTANCE.setEditable(this);
            drawBorderedRectangle(175, 100, 455, 115, 0xFF312921, 0xFF191511);
            drawSplitScaledString(TextEditor.INSTANCE.getText(this), 180, 104, 0xFFFFFFFF, 1F);
        } else { //Doing this down here so we don't have the bar in the way
            saveScreenshot(sanitized);
            saveTemplate(sanitized, template);
            GuiSimpleEditor.INSTANCE.setEditor(null);
            isTakingScreenshot = false;
            displayname = "New Template";
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        TextEditor.INSTANCE.keyTyped(character, key);
    }
}