package joshie.enchiridion.designer;

import java.util.ArrayList;

import joshie.enchiridion.ETranslate;
import joshie.enchiridion.designer.features.Feature;
import joshie.enchiridion.helpers.ClientHelper;

public class DesignerLayers {
    private int layerPosition = 0;
    
    public void draw(int mouseX, int mouseY) {
        DesignerHelper.drawRect(558, -55, 560, -37, 0xFFFFFFFF);
        DesignerHelper.drawRect(440, -55, 442, -37, 0xFFFFFFFF);
        DesignerHelper.drawRect(440, -57, 560, -55, 0xFFFFFFFF);
        DesignerHelper.drawRect(442, -55, 558, -37, 0xFF000000);
        DesignerHelper.drawRect(440, -37, 560, -39, 0xFFFFFFFF);
        DesignerHelper.drawSplitString(ETranslate.translate("layers"), 470, -50, 250, 0xFFFFFFFF);
        DesignerHelper.drawRect(450, -37, 550, 230, 0xFF000000);
        DesignerHelper.drawRect(448, -37, 450, 230, 0xFFFFFFFF);
        DesignerHelper.drawRect(550, -37, 552, 230, 0xFFFFFFFF);
        DesignerHelper.drawRect(448, 230, 552, 232, 0xFFFFFFFF);
        
        int layerY = 0;
        ArrayList<Feature> features = DesignerHelper.getGui().canvas.features;
        for (int i = layerPosition; i < Math.min(features.size(), layerPosition + 20); i++) {
            Feature feature = features.get(i);
            DesignerHelper.drawRect(452, -35 + layerY, 548, -17 + layerY, 0xFFFFFFFF);
            if(mouseX >= 452 && mouseX <= 548) {
                if(mouseY >= -35 + layerY && mouseY <= -17 + layerY) {
                    DesignerHelper.drawRect(452, -35 + layerY, 548, -17 + layerY, 0xFFCCCCCC);
                }
            }
                        
            DesignerHelper.drawSplitString(feature.getName(), 455, -29 + layerY, 250, 0xFF000000);
            layerY += 20;
        }
    }

    public void clicked(int mouseX, int mouseY) {        
        int layerY = 0;
        ArrayList<Feature> features = DesignerHelper.getGui().canvas.features;
        for (int i = layerPosition; i < Math.min(features.size(), layerPosition + 20); i++) {
            if(mouseX >= 452 && mouseX <= 548) {
                if(mouseY >= -35 + layerY && mouseY <= -17 + layerY) {
                    Feature feature = features.get(i);
                    //If we have clicked on this box then let's change it
                    if(ClientHelper.isShiftPressed()) {
                        moveDown(feature);
                    } else moveUp(feature);
                }
            }
            
            layerY += 20;
        }
    }
    
    public int getKey(Feature element) {
        int key = 0;
        for (key = 0; key < DesignerHelper.getGui().canvas.features.size(); key++) {
            if(DesignerHelper.getGui().canvas.features.get(key).equals(element)) {
                break;
            }
        }
        
        return key;
    }
    
    public void moveUp(Feature element) {        
        setPosition(element, Math.max(0, getKey(element) - 1));
    }
    
    public void moveDown(Feature element) {
        System.out.println("DOWN");
        
        setPosition(element, Math.min(DesignerHelper.getGui().canvas.features.size() - 1, getKey(element) + 1));
    }
    
    public void setPosition(Feature element, int position) {
        DesignerHelper.getGui().canvas.features.remove(element);
        DesignerHelper.getGui().canvas.features.add(position, element);
    }

    public void scroll(boolean scrolledDown) {
        if(DesignerHelper.getGui().mouseX >= 450) {
            if (scrolledDown) {
                this.layerPosition++;
                this.layerPosition = Math.min(layerPosition, DesignerHelper.getGui().canvas.features.size() - 1);
            } else {
                this.layerPosition--;
                this.layerPosition = Math.max(layerPosition, 0);
            }
        }
    }
}
