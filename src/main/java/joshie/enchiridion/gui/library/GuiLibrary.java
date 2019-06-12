package joshie.enchiridion.gui.library;

import com.mojang.blaze3d.platform.GlStateManager;
import joshie.enchiridion.util.ELocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

public class GuiLibrary extends GuiContainer {
    private static final ResourceLocation LOCATION = new ELocation("library");
    public final int xSize = 430;
    public final int ySize = 217;
    public IInventory library;
    public int x, y;

    public GuiLibrary(PlayerInventory playerInventory, IInventory library, Hand hand) {
        super(new ContainerLibrary(playerInventory, library, hand));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        x = (width - xSize) / 2;
        y = (height - ySize) / 2;
        drawImage(LOCATION, -10, -10, 440, 240);
    }

    //Helper
    private void drawImage(ResourceLocation resource, int left, int top, int right, int bottom) {
        //Fix the position in even scale factors
        if (scaledresolution.getScaleFactor() % 2 == 0) {
            top--;
            bottom--;
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double) (x + left), (double) (y + bottom), (double) zLevel).tex(0, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + bottom), (double) zLevel).tex(1, 1).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + right), (double) (y + top), (double) zLevel).tex(1, 0).color(1F, 1F, 1F, 1F).endVertex();
        buffer.pos((double) (x + left), (double) (y + top), (double) zLevel).tex(0, 0).color(1F, 1F, 1F, 1F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}