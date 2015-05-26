package joshie.enchiridion.designer.editor;

import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.isEditMode;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.designer.editor.EditorText.IText;

public class EditorColorable extends AbstractEditor {
    public static final EditorColorable instance = new EditorColorable();
    private IColorable selected;
    
    public void select(IColorable colorable) {
        super.clear();
        selected = colorable;
    }
    
    @Override
    public void deselect() {
        selected = null;
    }
    
    @Override
    public boolean isActive() {
        return selected != null;
    }
    
    private int getColor(String color) {
        try {
            return (int) Long.parseLong(color, 16);
        } catch (Exception e) { return selected.getColor(); }
    }
    
    /** Handle the Book **/
    
    @Override
    public void drawBook() {
        DesignerHelper.drawRect(-102, -55, -100, -37, 0xFFFFFFFF);
        DesignerHelper.drawRect(0, -55, 2, -37, 0xFFFFFFFF);
        DesignerHelper.drawRect(-102, -57, 2, -55, 0xFFFFFFFF);
        DesignerHelper.drawRect(-100, -55, 0, -37, 0xFF000000);
        DesignerHelper.drawRect(-100, -37, 0, -39, 0xFFFFFFFF);
        DesignerHelper.drawSplitString(String.valueOf(selected.getColor()), -95, -50, 250, 0xFFFFFFFF);
        DesignerHelper.drawRect(-98, -37, -12, 230, 0xFF000000);
        DesignerHelper.drawRect(-100, -37, -98, 230, 0xFFFFFFFF);
        DesignerHelper.drawRect(-14, -37, -12, 230, 0xFFFFFFFF);
        DesignerHelper.drawRect(-98, 230, -12, 232, 0xFFFFFFFF);

        int index = 0;
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 7; x++) {
                String color = EConfig.getColor(index);
                if (color == null) continue;
                DesignerHelper.drawRect(-90 + (x * 10), -30 + (y * 10), -80 + (x * 10), -20 + (y * 10), (int) Long.parseLong(color, 16));
                index++;
            }
        }
    }
    
    @Override
    public void clickBook(int mouseX, int mouseY, int button) {
        int index = 0;
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 7; x++) {
                String color = EConfig.getColor(index);
                if (color == null) continue;
                if (mouseX >= (x * 10) - 90 && mouseX <= (x * 10) - 80) {
                    if (mouseY >= (y * 10) - 30 && mouseY <= (y * 10) - 20) {
                        selected.setColor(getColor(color));
                    }
                }

                index++;
            }
        }
    }
    
    /** Handle the Wiki **/
    
    @Override
    public void drawWiki() {
      //Draw the Search box
        drawScaledTexture(texture, 10, 1, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 126, 1, 100, 147, 154, 39, 1F);
        drawScaledText(2F, String.valueOf(selected.getColor()), 19, 13, 0xFFFFFFFF);

        int index = 0;
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 7; x++) {
                String color = EConfig.getColor(index);
                if (color == null) continue;
                drawRect(20 + (x * 34), 50 + (y * 34), 50 + (x * 34), 80 + (y * 34), (int) Long.parseLong(color, 16));

                index++;
            }
        }
    }
    
    @Override
    public void clickWiki(int mouseX, int mouseY, int button) {
        if (button == 0) {
            //Draw Colour selection boxes
            int index = 0;
            for (int y = 0; y < 20; y++) {
                for (int x = 0; x < 7; x++) {
                    String color = EConfig.getColor(index);
                    if (color == null) continue;
                    if (getIntFromMouse(20 + (x * 34), 50 + (x * 34), 50 + (y * 34), 80 + (y * 34), 0, 1) == 1) {
                        selected.setColor(getColor(color));
                    }

                    index++;
                }
            }

            if (isEditMode()) {
                if (getIntFromMouse(0, 299, 0, 45, 0, 1) == 1) {
                    //GuiTextEdit.select(this);
                }
            }
        }
    }
    
    /** Colorable Interface **/
    public static interface IColorable extends IText {
        public int getColor();
        public void setColor(int color);
    }
}
