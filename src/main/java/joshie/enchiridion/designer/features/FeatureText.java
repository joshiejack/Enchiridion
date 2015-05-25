package joshie.enchiridion.designer.features;

import joshie.enchiridion.api.EnchiridionAPI;
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

    private boolean editingText;

    @Override
    public void drawFeature() {
        super.drawFeature();

        String display = this.text.contains("translate:") ? getTranslated() : getText();
        EnchiridionAPI.draw.drawText(display, left, top, colorI, wrap, size);
    }

    private String getTranslated() {
        String[] array = text.split("translate:");
        StringBuilder builder = new StringBuilder();
        for (String string : array) {
            String text[] = string.split("((?<=;)|(?=;))");
            for (int j = 0; j < text.length; j++) {
                int k = Math.min(text.length - 1, j + 1);
                String now = text[j];
                String after = text[k];
                if (!now.equals(";")) {
                    if (after.equals(";") || text.length == 1) {
                        builder.append(StringEscapeUtils.unescapeJava(StatCollector.translateToLocal(now)));
                    } else builder.append(now);
                }
            }
        }

        return builder.toString();
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
