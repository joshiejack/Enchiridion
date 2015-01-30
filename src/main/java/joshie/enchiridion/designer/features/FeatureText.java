package joshie.enchiridion.designer.features;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleAll;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.elements.ElementText;

import com.google.gson.annotations.Expose;

public class FeatureText extends FeatureColorable {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int wrap = 500;
    @Expose
    private float size = 1F;

    private boolean editingText;

    @Override
    public void drawFeature() {
        super.drawFeature();
        
        DesignerHelper.drawSplitScaledString(getText(), left, top, wrap, colorI, size);
    }
    
    @Override
    public String getColorText() {
        return !editingText? getText(color) : color;
    }
    
    @Override
    public String getText() {
        return editingText? getText(text): text;
    }

    @Override
    public String getTextField() {
        return editingText ? text : super.getTextField();
    }

    @Override
    public void setTextField(String str) {
        if (editingText) {
            this.text = str;
        } else super.setTextField(str);
    }

    @Override
    public void click(int mouseX, int mouseY) {
        if (DesignerHelper.getGui().canEdit) {
            if (mouseX <= -10) {
                editingText = false;
            } else editingText = true;
            
            position = getTextField().length();
        }

        super.click(mouseX, mouseY);
    }
    
    @Override
    public void keyTyped(char character, int key) {
        if(isSelected) {
            if(ClientHelper.isShiftPressed()) {
                if(key == 78) {
                    size = Math.min(15F, Math.max(1F, size + 0.1F));
                    return;
                } else if (key == 74) {
                    size = Math.min(15F, Math.max(1F, size - 0.1F));
                    return;
                }
            }
        }
        
        super.keyTyped(character, key);
    }
}
