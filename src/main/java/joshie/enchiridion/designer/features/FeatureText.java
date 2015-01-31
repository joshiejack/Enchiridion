package joshie.enchiridion.designer.features;

import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.annotations.Expose;

public class FeatureText extends FeatureColorable {
    @Expose
    private String text = "Lorem ipsum";
    @Expose
    private int wrap = 0;
    @Expose
    private float size = 1F;

    private boolean editingText;

    @Override
    public void drawFeature() {
        super.drawFeature();

        String display = this.text.startsWith("translate:") ? StringEscapeUtils.unescapeJava(StatCollector.translateToLocal(text.replaceFirst("translate:", ""))) : getText();
        if (wrap >= 50) {
            DesignerHelper.drawSplitScaledString(display, left, top, wrap, colorI, size);
        } else DesignerHelper.drawSplitScaledString(display, left, top, Math.max(50, (int) ((width) / size) + 4), colorI, size);
    }

    @Override
    public String getColorText() {
        return !editingText ? getText(color) : color;
    }

    @Override
    public String getText() {
        return editingText ? getText(text) : text;
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
        if (isSelected) {
            if (ClientHelper.isShiftPressed()) {
                if (key == 78) {
                    size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                    return;
                } else if (key == 74) {
                    size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                    return;
                }
            }
        }

        super.keyTyped(character, key);
    }
}
