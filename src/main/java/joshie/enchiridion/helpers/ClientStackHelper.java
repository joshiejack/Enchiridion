package joshie.enchiridion.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ClientStackHelper {
    public static void drawStack(ItemStack stack, int x, int y, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
}
