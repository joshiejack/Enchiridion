package joshie.enchiridion.designer.features;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.designer.DesignerHelper;

import com.google.gson.annotations.Expose;

public abstract class FeatureColorable extends FeatureWithText {
    @Expose
    protected String color = "FFFFFFFF";
    protected int colorI;
    private boolean init;
    
    @Override
    public String getTextField() {
        return this.color;
    }

    @Override
    public void setTextField(String str) {
        this.color = str.replace("\n", "");
        parseColor();
    }
    
    public String getColorText() {
        return getText();
    }
    
    @Override
    public void drawFeature() {
        if(!init) {
            init = true;
            parseColor();
        }
        
        if(isSelected) {
            DesignerHelper.drawRect(-102, -55, -100, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(0, -55, 2, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(-102, -57, 2, -55, 0xFFFFFFFF);
            DesignerHelper.drawRect(-100, -55, 0, -37, 0xFF000000);
            DesignerHelper.drawRect(-100, -37, 0, -39, 0xFFFFFFFF);
            DesignerHelper.drawSplitString(getColorText(), -95, -50, 250, 0xFFFFFFFF);
            DesignerHelper.drawRect(-98, -37, -12, 230, 0xFF000000);
            DesignerHelper.drawRect(-100, -37, -98, 230, 0xFFFFFFFF);
            DesignerHelper.drawRect(-14, -37, -12, 230, 0xFFFFFFFF);
            DesignerHelper.drawRect(-98, 230, -12, 232, 0xFFFFFFFF);
            
            int index = 0;
            for(int y = 0; y < 20; y++) {
                for(int x = 0; x < 7; x++) {
                    String color = EConfig.getColor(index);
                    if(color == null) continue;
                    DesignerHelper.drawRect(-90 + (x * 10), -30 + (y * 10), -80 + (x * 10), -20 + (y * 10), (int) Long.parseLong(color, 16));
                    
                    index++;
                }
            }
        }
    }
    
    @Override
    public void click(int mouseX, int mouseY) {
        if(DesignerHelper.getGui().canEdit && isSelected) {
            int index = 0;
            for(int y = 0; y < 20; y++) {
                for(int x = 0; x < 7; x++) {
                    String color = EConfig.getColor(index);
                    if(color == null) continue;
                    if (mouseX >= (x * 10) - 90 && mouseX <= (x * 10) - 80) {
                        if (mouseY >= (y * 10) - 30 && mouseY <= (y * 10) - 20) {
                            setTextField(color);
                        }
                    }
                    
                    index++;
                }
            }
        }
        
        super.click(mouseX, mouseY);
    }
    
    private void parseColor() {
        try {
            this.colorI = (int) Long.parseLong(color, 16);
        } catch (Exception e) {}
    }
}
