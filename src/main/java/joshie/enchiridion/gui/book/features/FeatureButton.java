package joshie.enchiridion.gui.book.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorButton;
import joshie.enchiridion.helpers.MCClientHelper;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class FeatureButton extends FeatureJump implements IButtonActionProvider {
    protected transient boolean isInit = false;
    public IButtonAction action;
    public float size = 1F;

    public FeatureButton(){}
    public FeatureButton(IButtonAction action) {
        this.action = action;
    }

    @Override
    public IButtonAction getAction() {
        return action;
    }

    @Override
    public FeatureButton copy() {
        FeatureButton button = new FeatureButton(action.copy());
        button.size = size;
        return button;
    }

    @Override
    public void keyTyped(char character, int key) {
        if (MCClientHelper.isShiftPressed()) {
            if (key == 78) {
                size = Math.min(15F, Math.max(0.5F, size + 0.1F));
            } else if (key == 74) {
                size = Math.min(15F, Math.max(0.5F, size - 0.1F));
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (action == null) return;
        if (!isInit && action != null) { //Called here because action needs everything to be loaded, where as update doesn't
            action.onFieldsSet("");
            isInit = true;
        }

        boolean isHovered = position.isOverFeature(mouseX, mouseY);
        ResourceLocation location = action.getResource(isHovered);
        if (location != null) {
            EnchiridionAPI.draw.drawImage(location, position.getLeft(), position.getTop(), position.getRight(), position.getBottom());
        }

        EnchiridionAPI.draw.drawSplitScaledString(action.getText(isHovered), position.getLeft() + action.getTextOffsetX(isHovered), position.getTop() + action.getTextOffsetY(isHovered), 200, 0x555555, size);
    }

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorButton.INSTANCE.setButton(this));
        return true;
    }

    @Override
    public void performClick(int mouseX, int mouseY) {
        if (action != null) action.performAction();
    }

    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        if (action != null) {
            String[] title = action.getTooltip().split("\n");
            if (title != null) {
                boolean first = false;
                for (String t: title) {
                    if (first || !t.equals("")) {
                        tooltip.add(t);
                    } else first = true;
                }
            }
        }
    }

    //Let's fix the broken json
    public FeatureButton fix(JsonObject json) {
        if (json.get("deflt") != null) {
            String dflt = json.get("deflt").getAsJsonObject().get("path").getAsString();
            if (action != null) action.setResourceLocation(false, new ResourceLocation(dflt));
        }

        if (json.get("hover") != null) {
            String hover = json.get("hover").getAsJsonObject().get("path").getAsString();
            if (action != null) action.setResourceLocation(true, new ResourceLocation(hover));
        }

        return this;
    }
}
