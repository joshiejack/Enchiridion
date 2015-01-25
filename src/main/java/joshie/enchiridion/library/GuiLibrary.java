package joshie.enchiridion.library;

import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledStack;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_NEVER;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilMask;
import static org.lwjgl.opengl.GL11.glStencilOp;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiExtension;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GuiLibrary extends GuiExtension {
    private static final int MAX_PER_ROW = 15;
    
    @Override
    public void draw() {
        glPushMatrix();
        glEnable(GL_BLEND);
        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_NEVER, 1, 0xFF);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        glStencilMask(0xFF);
        glClear(GL_STENCIL_BUFFER_BIT);
        drawRect(5, 43, 1020, getHeight() + 10, 0x22000000);
        glStencilMask(0x00);
        glStencilFunc(GL_EQUAL, 0, 0xFF);
        glStencilFunc(GL_EQUAL, 1, 0xFF);
        drawRect(0, 0, 2048, 5000 + 10, 0xEE000000);
        drawRect(630, -45, 910, -10, 0xFF000000);
        glDisable(GL_STENCIL_TEST);
        drawScaledCentredText(2.5F, I18n.format("enchiridion.library", new Object[0], 2F), 510, 14, 0xFFFFFF);
        fixColors();
        drawBooks();
        drawTooltips();
        glDisable(GL_BLEND);
        glPopMatrix();
        fixColors();
    }

    public void drawBooks() {
        int j = 0;
        int k = 0;
        for (ItemStack stack : LibraryRegistry.getBooks()) {
            drawScaledStack(stack, 40 + (j * 74), 50 + (k * 74), 4F);
            j++;

            if (j > MAX_PER_ROW) {
                j = 0;
                k++;
            }
        }
    }
    
    public void drawTooltips() {
        int j = 0;
        int k = 0;
        for (ItemStack stack : LibraryRegistry.getBooks()) {
            //Drawing the tooltips
            int xStart = 40 + (j * 74);
            int yStart = 50 + (k * 74);
            if (getIntFromMouse(xStart - 5, xStart + 69, yStart, yStart + 73, 0, 1) == 1) {
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
    }

    @Override
    public void clicked(int button) {
        int j = 0;
        int k = 0;
        for (ItemStack stack : LibraryRegistry.getBooks()) {
            if (getIntFromMouse(40 + (j * 74), 40 + 74 + (j * 74), 50 + (k * 74), 50 + 74 + (k * 74), 0, 1) == 1) {
                LibraryRegistry.getHandler(stack).handle(stack, ClientHelper.getWorld(), ClientHelper.getPlayer());
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

        }
    }
}
