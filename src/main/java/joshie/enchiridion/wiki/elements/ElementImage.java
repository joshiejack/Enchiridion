package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.wiki.gui.GuiMain;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import com.google.gson.annotations.Expose;

public class ElementImage extends Element {
    private DynamicTexture texture;
    private ResourceLocation resource;
    
    @Expose
    public String type;
    @Expose
    public String path;
    
    @Override
    public ElementImage setToDefault() {
        return this;
    }
    
    @Override
    public void display(boolean isEditMode) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addEditButtons(List list) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onSelected() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onDeselected() {
        // TODO Auto-generated method stub
        
    }
}
