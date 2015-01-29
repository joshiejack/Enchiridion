package joshie.enchiridion.designer;

import java.util.ArrayList;

import joshie.enchiridion.designer.features.Feature;
import joshie.enchiridion.designer.gui.GuiDesigner;

import com.google.gson.annotations.Expose;

public class DesignerCanvas {
    @Expose
    public ArrayList<Feature> features = new ArrayList();

    //Draws all the features on the canvas
    public void draw(GuiDesigner gui, int x, int y) {
        for(Feature feature: features) {
            feature.draw(gui, x, y);
        }
    }

    public void clicked(int x, int y, boolean isEditMode) {
        for(Feature feature: features) {
            feature.click(x, y, isEditMode);
        }
    }
}
