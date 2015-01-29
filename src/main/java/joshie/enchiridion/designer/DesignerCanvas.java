package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;

import java.util.ArrayList;

import joshie.enchiridion.designer.features.Feature;

import com.google.gson.annotations.Expose;

public class DesignerCanvas {
    @Expose
    public ArrayList<Feature> features = new ArrayList();
    public Feature selected;

    //Draws all the features on the canvas
    public void draw(int x, int y) {
        for(Feature feature: features) {
            start();
            resetZ();
            feature.draw(x, y);
            end();
        }
    }

    public void clicked(int x, int y, boolean isEditMode) {
        for(Feature feature: features) {
            feature.click(x, y, isEditMode);
        }
    }
    
    public void release(int x, int y) {
        for(Feature feature: features) {
            feature.release(x, y);
        }
    }
    
    public void follow(int x, int y) {
        for(Feature feature: features) {
            feature.follow(x, y);
        }
    }
}
