package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorColor;
import joshie.enchiridion.util.IColorable;

public class FeatureBox extends FeatureAbstract implements IColorable {
    public String color;
    public transient int colorI;

    public FeatureBox() {
    }

    public FeatureBox(String color) {
        this.color = color;
    }

    @Override
    public FeatureBox copy() {
        return new FeatureBox(color);
    }

    @Override
    public String getName() {
        return "Box: " + Integer.toHexString(colorI);
    }

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorColor.INSTANCE.setColorable(this));
        return false;
    }

    private boolean attemptToParseColor() {
        int previousColor = this.colorI;
        if (!attemptToParseString(color)) {
            String doubled = color.replaceAll(".", "$0$0");
            if (!attemptToParseString(doubled)) {
                if (!attemptToParseString(doubled.replace("#", ""))) {
                    this.colorI = previousColor;
                    return false;
                } else return true;
            } else return true;
        } else return true;
    }

    private boolean attemptToParseString(String string) {
        try {
            colorI = (int) Long.parseLong(string, 16);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        attemptToParseColor();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        EnchiridionAPI.draw.drawRectangle(position.getLeft(), position.getTop(), position.getRight(), position.getBottom(), colorI);
    }

    @Override
    public String getColorAsHex() {
        return color;
    }

    @Override
    public void setColorAsHex(String color) {
        String previous = this.color;
        this.color = color;
        if (attemptToParseColor()) {
            this.color = color;
        } else this.color = previous;
    }
}