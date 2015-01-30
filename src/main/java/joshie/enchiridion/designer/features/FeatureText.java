package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;

import com.google.gson.annotations.Expose;

public class FeatureText extends FeatureColorable {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int wrap = 500;

    private boolean editingText;

    @Override
    public void drawFeature() {
        super.drawFeature();

        DesignerHelper.drawSplitString(getText(), left, top, wrap, colorI);
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
}
