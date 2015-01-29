package joshie.enchiridion.designer.features;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.gson.annotations.Expose;

public class FeatureItem extends Feature {
    @Expose
    public String item;
    public ItemStack stack;
    public float size;
    
    public FeatureItem() {
        width = 32;
        height = 32;
        item = "minecraft:stone";
    }
    
    @Override
    public void recalculate(int x, int y) {
        super.recalculate(x, y);
        size = (float) (width / 8D);
    }

    @Override
    public void drawFeature() {
        if(stack == null) {
            stack = StackHelper.getStackFromString(item);
        }
        
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        FeatureItem.itemRenderer.renderItemAndEffectIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, (int)(left / size), (int)(top / size));
        FeatureItem.itemRenderer.renderItemOverlayIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, (int)(left / size), (int)(top / size));
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);  
        GL11.glPopMatrix();
    }
}
