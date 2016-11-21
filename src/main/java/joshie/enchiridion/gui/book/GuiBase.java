package joshie.enchiridion.gui.book;

import joshie.enchiridion.api.gui.IDrawHelper;
import joshie.enchiridion.api.recipe.IItemStack;
import joshie.enchiridion.helpers.ClientStackHelper;
import joshie.enchiridion.util.PenguinFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiBase extends GuiScreen implements IDrawHelper {
    public static final GuiBase INSTANCE = new GuiBase();
    protected final int xSize = 430;
    protected final int ySize = 217;
    protected ScaledResolution res;

    public final List<String> TOOLTIP = new ArrayList<>();
    public int mouseX = 0;
    public int mouseY = 0;
    public int x;
    public int y;
    private int renderX;
    private int renderY;
    private double renderWidth;
    private double renderHeight;
    private float renderSize;

    protected GuiBase() {
    }

    @Override
    public void drawScreen(int x2, int y2, float partialTicks) {
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        res = new ScaledResolution(mc);
        TOOLTIP.clear();
    }

    public ScaledResolution getRes() {
        return res;
    }

    @Override
    public void handleMouseInput() throws IOException {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x - (width - xSize) / 2;
        mouseY = y - (height - ySize) / 2;
        super.handleMouseInput(); //Call the super
    }


    @Override
    public void setRenderData(int xPos, int yPos, double width, double height, float size) {
        renderX = xPos;
        renderY = yPos;
        renderWidth = width;
        renderHeight = height;
        renderSize = size;
    }

    private int getLeft(double x) {
        return (int) (renderX + ((x / 150D) * renderWidth));
    }

    private int getTop(double y) {
        return (int) (renderY + ((y / 100D) * renderHeight));
    }

    @Override
    public boolean isMouseOverIItemStack(IItemStack stack) {
        if (stack == null || stack.getItemStack().isEmpty()) return false;
        int left = getLeft(stack.getX());
        int top = getTop(stack.getY());
        int scaled = (int) (16 * stack.getScale() * renderSize);
        int right = left + scaled;
        int bottom = top + scaled;
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
    }

    @Override
    public boolean isMouseOverArea(double x2, double y2, int width, int height, float scale) {
        int left = getLeft(x2);
        int top = getTop(y2);
        int scaledX = (int) (width * scale * renderSize);
        int scaledY = (int) (height * scale * renderSize);
        int right = left + scaledX;
        int bottom = top + scaledY;
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
    }

    @Override
    public void drawIItemStack(IItemStack stack) {
        stack.onDisplayTick(); //Update the display ticker
        drawStack(stack.getItemStack(), getLeft(stack.getX()), getTop(stack.getY()), renderSize * stack.getScale());
    }

    @Override
    public void drawTexturedRectangle(double left, double top, int u, int v, int w, int h, float scale) {
        float size = renderSize * scale;
        int x2 = (int) Math.floor(((x + getLeft(left)) / size));
        int y2 = (int) Math.floor(((y + getTop(top)) / size));

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableAlpha();
        GlStateManager.scale(size, size, 1.0F);
        drawTexturedModalRect(x2, y2, u, v, w, h);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawTexturedReversedRectangle(double left, double top, int u, int v, int w, int h, float scale) {
        float size = renderSize * scale;
        int x2 = (int) Math.floor(((x + getLeft(left)) / size)) - w;
        int y2 = (int) Math.floor(((y + getTop(top)) / size)) - h;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableAlpha();
        GlStateManager.scale(size, size, 1.0F);
        drawTexturedModalRect(x2, y2, u, v, w, h);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        PenguinFont.INSTANCE.drawSplitString(text, (int) ((x + xPos) / scale), (int) ((y + yPos) / scale), wrap, color);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawRectangle(int left, int top, int right, int bottom, int colorI) {
        drawRect(x + left, y + top, x + right, y + bottom, colorI);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void drawLine(int left, int top, int right, int bottom, int thickness, int color) {
        //Fix these numbers
        left += x;
        top += y;
        right += x;
        bottom += y;

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);

        int posX;
        if (right > left) {
            posX = thickness;
        } else {
            posX = -thickness;
        }

        int posY;
        if (bottom > top) {
            posY = thickness;
        } else {
            posY = -thickness;
        }

        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos((double) left, (double) top + posX, 0.0D).endVertex();
        buffer.pos((double) right, (double) bottom + posX, 0.0D).endVertex();
        buffer.pos((double) right + posY, (double) bottom, 0.0D).endVertex();
        buffer.pos((double) left + posY, (double) top, 0.0D).endVertex();
        tessellator.draw();

        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos((double) left, (double) top, 0.0D).color(f, f1, f2, f3).endVertex();
        buffer.pos((double) left + 5, (double) top, 0.0D).color(f, f1, f2, f3).endVertex();
        buffer.pos((double) left + 5, (double) top + 5, 0.0D).color(f, f1, f2, f3).endVertex();
        buffer.pos((double) left, (double) top + 5, 0.0D).color(f, f1, f2, f3).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB) {
        drawRect(x + left, y + top, x + right, y + bottom, colorI);
        drawRect(x + left, y + top, x + right, y + top + 1, colorB);
        drawRect(x + left, y + bottom - 1, x + right, y + bottom, colorB);
        drawRect(x + left, y + top, x + left + 1, y + bottom, colorB);
        drawRect(x + right - 1, y + top, x + right, y + bottom, colorB);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void drawStack(@Nonnull ItemStack stack, int left, int top, float size) {
        if (stack.isEmpty()) return; //Don't draw stacks that don't exist
        int x2 = (int) Math.floor(((x + left) / size));
        int y2 = (int) Math.floor(((y + top) / size));
        ClientStackHelper.drawStack(stack, x2, y2, size);
    }

    @Override
    public void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F);
        mc.getTextureManager().bindTexture(resource);
        GlStateManager.scale(scaleX, scaleY, 1.0F);
        drawTexturedModalRect((int) ((x + left) / scaleX), (int) ((y + top) / scaleY), 0, 0, width, height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawImage(ResourceLocation resource, int left, int top, int right, int bottom) {
        if (resource == null) {
            return; //DON'T YOU DARE RENDER BROKEN STUFF!!!
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double) (x + left), (double) (y + bottom), (double) zLevel).tex(0, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + bottom), (double) zLevel).tex(1, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + top), (double) zLevel).tex(1, 0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + left), (double) (y + top), (double) zLevel).tex(0, 0).color(1F, 1F, 1F, 1F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override //From vanilla, switching to my font renderer though
    protected void drawHoveringText(List<String> textLines, int x, int y, @Nonnull FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = PenguinFont.INSTANCE.getStringWidth(s);

                if (j > i) {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.width) {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > this.height) {
                i2 = this.height - k - 6;
            }

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            int l = 0xCC312921;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 0xFF191511;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                PenguinFont.INSTANCE.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}