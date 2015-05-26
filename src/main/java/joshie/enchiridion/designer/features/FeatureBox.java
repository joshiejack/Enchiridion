package joshie.enchiridion.designer.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.designer.editor.EditorColorable.IColorable;

public class FeatureBox extends FeatureColorable implements IColorable {
    @Override
    public void drawFeature() {
        super.drawFeature();
        EnchiridionAPI.draw.drawRect(left, top, right, bottom, colorI);
    }
    
    @Override
    public void updateWidth(int change) {
        if (height <= 1) height = 1;
        
        width += change;
        if (width <= 1) {
            width = 1;
        }
    }

    @Override
    public void setColor(int color) {
        colorI = color;
    }
    
    @Override
    public int getColor() {
        return colorI;
    }
}
