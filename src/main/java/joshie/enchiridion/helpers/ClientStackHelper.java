package joshie.enchiridion.helpers;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ClientStackHelper {
    public static void drawStack(@Nonnull ItemStack stack, int x, int y, float size) {
        GlStateManager.pushMatrix();
        GlStateManager.scalef(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color4f(1F, 1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, null);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableAlphaTest();
        GlStateManager.popMatrix();
    }
}