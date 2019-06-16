package joshie.enchiridion.gui.book;

import com.mojang.blaze3d.platform.GlStateManager;
import joshie.enchiridion.api.gui.IDrawHelper;
import joshie.enchiridion.api.recipe.IItemStack;
import joshie.enchiridion.helpers.ClientStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiBase extends Screen implements IDrawHelper {
    public static final GuiBase INSTANCE = new GuiBase();
    protected final int xSize = 430;
    protected final int ySize = 217;

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
        super(new TranslationTextComponent("enchiridion.guiBase.title"));
    }

    @Override
    public void render(int x2, int y2, float partialTicks) {
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        TOOLTIP.clear();
    }

    @Override
    public void mouseMoved(double mX, double mY) {
        Minecraft mc = Minecraft.getInstance();
        final double x = mc.mouseHelper.getMouseX() * ((double) mc.mainWindow.getScaledWidth() / mc.mainWindow.getWidth());
        final double y = mc.mouseHelper.getMouseY() * ((double) mc.mainWindow.getScaledHeight() / mc.mainWindow.getHeight());

        mouseX = (int) (x - (width - xSize) / 2);
        mouseY = (int) (y - (height - ySize) / 2);
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
        GlStateManager.color4f(1F, 1F, 1F, 1F);
        GlStateManager.enableAlphaTest();
        GlStateManager.scalef(size, size, 1.0F);
        blit(x2, y2, u, v, w, h);
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
        GlStateManager.color4f(1F, 1F, 1F, 1F);
        GlStateManager.scalef(size, size, 1.0F);
        blit(x2, y2, u, v, w, h);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale, scale, scale);
        GuiBase.this.font.drawSplitString(text, (int) ((x + xPos) / scale), (int) ((y + yPos) / scale), wrap, color);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawRectangle(int left, int top, int right, int bottom, int colorI) {
        fill(x + left, y + top, x + right, y + bottom, colorI);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color4f(f, f1, f2, f3);

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

        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB) {
        fill(x + left, y + top, x + right, y + bottom, colorI);
        fill(x + left, y + top, x + right, y + top + 1, colorB);
        fill(x + left, y + bottom - 1, x + right, y + bottom, colorB);
        fill(x + left, y + top, x + left + 1, y + bottom, colorB);
        fill(x + right - 1, y + top, x + right, y + bottom, colorB);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
        GlStateManager.color3f(1F, 1F, 1F);
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        GlStateManager.scalef(scaleX, scaleY, 1.0F);
        blit((int) ((x + left) / scaleX), (int) ((y + top) / scaleY), 0, 0, width, height);
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
        BufferBuilder buffer = tessellator.getBuffer();
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double) (x + left), (double) (y + bottom), (double) blitOffset).tex(0, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + bottom), (double) blitOffset).tex(1, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + top), (double) blitOffset).tex(1, 0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + left), (double) (y + top), (double) blitOffset).tex(0, 0).color(1F, 1F, 1F, 1F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override //From vanilla, switching to my font renderer though
    public void renderTooltip(List<String> textLines, int x, int y, @Nonnull FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            int i = 0;

            for (String s : textLines) {
                int j = GuiBase.this.font.getStringWidth(s);

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

            this.blitOffset = 300;
            this.itemRenderer.zLevel = 300.0F;
            int l = 0xCC312921;
            this.fillGradient(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.fillGradient(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.fillGradient(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.fillGradient(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.fillGradient(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 0xFF191511;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.fillGradient(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.fillGradient(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.fillGradient(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.fillGradient(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                GuiBase.this.font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.blitOffset = 0;
            this.itemRenderer.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}