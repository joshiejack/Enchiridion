package joshie.enchiridion.wiki;

import joshie.enchiridion.wiki.elements.ElementItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class WikiRender extends RenderItem {
    @Override
    public void renderItemIntoGUI(FontRenderer font, TextureManager tm, ItemStack stack, int x, int y, boolean renderEffect) {
        int k = stack.getItemDamage();
        Object object = stack.getIconIndex();
        int l;
        float f;
        float f3;
        float f4;

        if (stack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
            tm.bindTexture(TextureMap.locationBlocksTexture);
            Block block = Block.getBlockFromItem(stack.getItem());
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (block.getRenderBlockPass() != 0) {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            } else {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            l = stack.getItem().getColorFromItemStack(stack, 0);
            f3 = (float) (l >> 16 & 255) / 255.0F;
            f4 = (float) (l >> 8 & 255) / 255.0F;
            f = (float) (l & 255) / 255.0F;

            if (this.renderWithColor) {
                GL11.glColor4f(f3, f4, f, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            ElementItem.renderer.useInventoryTint = this.renderWithColor;
            ElementItem.renderer.renderBlockAsItem(block, k, 1.0F);
            ElementItem.renderer.useInventoryTint = true;

            if (block.getRenderBlockPass() == 0) {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            }

            GL11.glPopMatrix();
        } else if (stack.getItem().requiresMultipleRenderPasses()) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            tm.bindTexture(TextureMap.locationItemsTexture);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(0, 0, 0, 0);
            GL11.glColorMask(false, false, false, true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            Item item = stack.getItem();
            for (l = 0; l < item.getRenderPasses(k); ++l) {
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                tm.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
                IIcon iicon = item.getIcon(stack, l);
                int i1 = stack.getItem().getColorFromItemStack(stack, l);
                f = (float) (i1 >> 16 & 255) / 255.0F;
                float f1 = (float) (i1 >> 8 & 255) / 255.0F;
                float f2 = (float) (i1 & 255) / 255.0F;

                if (this.renderWithColor) {
                    GL11.glColor4f(f, f1, f2, 1.0F);
                }

                GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, ad renderEffect can derp them up.
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                this.renderIcon(x, y, iicon, 16, 16);

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_LIGHTING);

                if (renderEffect && stack.hasEffect(l)) {
                    renderEffect(tm, x, y);
                }
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            ResourceLocation resourcelocation = tm.getResourceLocation(stack.getItemSpriteNumber());
            tm.bindTexture(resourcelocation);

            if (object == null) {
                object = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
            }

            l = stack.getItem().getColorFromItemStack(stack, 0);
            f3 = (float) (l >> 16 & 255) / 255.0F;
            f4 = (float) (l >> 8 & 255) / 255.0F;
            f = (float) (l & 255) / 255.0F;

            if (this.renderWithColor) {
                GL11.glColor4f(f3, f4, f, 1.0F);
            }

            GL11.glDisable(GL11.GL_LIGHTING); //Forge: Make sure that render states are reset, a renderEffect can derp them up.
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);

            this.renderIcon(x, y, (IIcon) object, 16, 16);

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            if (renderEffect && stack.hasEffect(0)) {
                renderEffect(tm, x, y);
            }
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
