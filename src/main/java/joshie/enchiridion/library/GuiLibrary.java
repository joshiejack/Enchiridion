package joshie.enchiridion.library;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledCentredText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledStack;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledText;
import static joshie.enchiridion.wiki.WikiHelper.getHeight;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.verticalGradient;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.OpenGLHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiExtension;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GuiLibrary extends GuiExtension {
    private static final int MAX_PER_ROW = 12;
    private static int SHELF = 0;

    @Override
    public void draw() {
        start();
        enable(GL_BLEND);

        drawRect(5, 43, 1019, 5000 + 10, 0xEE000000);
        drawScaledCentredText(2.5F, I18n.format("enchiridion.library", new Object[0], 2F), 510, 14, 0xFFFFFF);
        fixColors();
        drawBooks();
        drawTooltips();
        disable(GL_BLEND);
        end();
        fixColors();
    }

    public void drawBooks() {
        int j = 0;
        int k = 0;
        for (int i = SHELF * MAX_PER_ROW; i < LibraryDataClient.storage.getBooks().size(); i++) {
            ItemStack stack = LibraryDataClient.storage.getBooks().get(i);
            drawScaledStack(stack, 36 + (j * 74), 50 + (k * 80), 4F);
            j++;

            if (j > MAX_PER_ROW) {
                j = 0;
                k++;
            }
        }
    }

    public void drawTooltips() {
        OpenGLHelper.start();
        OpenGLHelper.resetZ();
        int j = 0;
        int k = 0;
        for (int i = SHELF * MAX_PER_ROW; i < LibraryDataClient.storage.getBooks().size(); i++) {
            ItemStack stack = LibraryDataClient.storage.getBooks().get(i);
            //Drawing the tooltips
            int xStart = 36 + (j * 74);
            int yStart = 50 + (k * 74);
            if (getIntFromMouse(xStart - 5, xStart + 69, yStart, yStart + 79, 0, 1) == 1) {
                yStart -= 5;
                String title = stack.getDisplayName();
                int length = WikiHelper.gui.mc.fontRenderer.getStringWidth(title);
                verticalGradient(xStart + 5, yStart - 24, xStart + 20 + (length * 2), yStart + 1, 0xEE2E555E, 0xEE1B2C43);
                //Top Side
                drawRect(xStart + 5, yStart - 28, xStart + 20 + (length * 2), yStart - 26, 0xCCC2C29C);
                drawRect(xStart + 5, yStart - 26, xStart + 20 + (length * 2), yStart - 24, 0xCC2A535E);
                //Bottom Side
                drawRect(xStart + 5, yStart + 1, xStart + 20 + (length * 2), yStart + 3, 0xCCC2C29C);
                drawRect(xStart + 5, yStart - 1, xStart + 20 + (length * 2), yStart + 1, 0xCC2A535E);
                //Left Side
                drawRect(xStart + 3, yStart - 28, xStart + 5, yStart + 3, 0xCCC2C29C);
                //Right Side
                drawRect(xStart + 20 + (length * 2), yStart - 28, xStart + 22 + (length * 2), yStart + 3, 0xCCC2C29C);
                drawScaledText(2F, title, xStart + 15, yStart - 20, 0xFFC2C29C);
            }

            j++;

            if (j > MAX_PER_ROW) {
                j = 0;
                k++;
            }
        }

        OpenGLHelper.end();
    }

    @Override
    public void clicked(int button) {
        int j = 0;
        int k = 0;
        for (int i = SHELF * MAX_PER_ROW; i < LibraryDataClient.storage.getBooks().size(); i++) {
            ItemStack stack = LibraryDataClient.storage.getBooks().get(i);
            if (getIntFromMouse(36 + (j * 74), 36 + 74 + (j * 74), 50 + (k * 80), 50 + 74 + (k * 80), 0, 1) == 1) {
                BookHandlerRegistry.getHandler(stack).handle(stack, ClientHelper.getWorld(), ClientHelper.getPlayer());
            }

            j++;

            if (j > MAX_PER_ROW) {
                j = 0;
                k++;
            }
        }
    }

    @Override
    public void release(int button) {

    }

    @Override
    public void follow() {

    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (mouseX >= 290 && mouseX <= 1024) {
            if (scrolledDown) {
                SHELF++;
            } else {
                SHELF--;
                SHELF = Math.max(0, SHELF);
            }
        }
    }
}
