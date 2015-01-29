package joshie.enchiridion.designer.features;

import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.wiki.elements.ElementItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.gson.annotations.Expose;

public class FeatureItem extends Feature {
    @Expose
    public String item;
    public ItemStack stack;
    
    public FeatureItem() {
        width = 64;
        height = 64;
        item = "minecraft:stone";
    }

    @Override
    public void drawFeature() {
        if(stack == null) {
            stack = StackHelper.getStackFromString(item);
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ElementItem.itemRenderer.renderItemAndEffectIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, x, y);
        ElementItem.itemRenderer.renderItemOverlayIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, x, y);
        GL11.glDisable(GL11.GL_LIGHTING);  
    }
}
