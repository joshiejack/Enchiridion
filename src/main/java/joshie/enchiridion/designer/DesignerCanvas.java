package joshie.enchiridion.designer;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;

import java.util.ArrayList;

import joshie.enchiridion.designer.features.Feature;
import joshie.enchiridion.helpers.ClientHelper;

import com.google.gson.annotations.Expose;

public class DesignerCanvas {
    @Expose
    public ArrayList<Feature> features = new ArrayList();
    @Expose
    public String pageName;
    
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

    public void keyTyped(char character, int key) {
        Feature remove = null;
        for(Feature feature: features) {
            feature.keyTyped(character, key);
            if (ClientHelper.isShiftPressed() && key == 211) {
                if(feature.isSelected) {
                    remove = feature;
                }
            }
        }
                        
        if (remove != null) {
            features.remove(remove);
            selected = null;
        }
    }

    public void scroll(boolean scrolledDown) {
        for(Feature feature: features) {
            feature.scroll(scrolledDown);
        }
    }
}
