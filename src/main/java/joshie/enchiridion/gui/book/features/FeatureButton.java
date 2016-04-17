package joshie.enchiridion.gui.book.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorButton;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class FeatureButton extends FeatureJump implements IButtonActionProvider {
    protected transient ResourceLocation hovered;
    protected transient ResourceLocation unhovered;
    protected transient boolean isInit = false;
    private IButtonAction action; //Serialize these
    private float size = 1F; //Serialize but hide

    //Readables
    public boolean leftClick = true;
    public boolean rightClick = true;
    public boolean otherClick = false;
    public String tooltip = "";
    public String hoverText = "";
    public int hoverXOffset = 0;
    public int hoverYOffset = 0;
    public String unhoveredText = "";
    public int unhoveredXOffset;
    public int unhoveredYOffset;

    public FeatureButton(){}
    public FeatureButton(IButtonAction action) {
        this.action = action;
        setResourceLocation(true, new ELocation("arrow_left_on")); //Default
        setResourceLocation(false, new ELocation("arrow_left_off")); //Default
    }

    @Override
    public IButtonAction getAction() {
        return action;
    }

    public void setAction(IButtonAction action) {
        this.action = action;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public FeatureButton copy() {
        FeatureButton button = new FeatureButton(action.copy());
        button.hovered = hovered;
        button.unhovered = unhovered;
        button.size = size;
        button.leftClick = leftClick;
        button.rightClick = rightClick;
        button.otherClick = otherClick;
        button.tooltip = tooltip;
        button.hoverText = hoverText;
        button.hoverXOffset = hoverXOffset;
        button.hoverYOffset = hoverYOffset;
        button.unhoveredText = unhoveredText;
        button.unhoveredXOffset = unhoveredXOffset;
        button.unhoveredYOffset = unhoveredYOffset;
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
        if (action == null || !action.isVisible()) return;
        if (!isInit && action != null) { //Called here because action needs everything to be loaded, where as update doesn't
            action.onFieldsSet("");
            isInit = true;
        }

        boolean isHovered = position.isOverFeature(mouseX, mouseY);
        ResourceLocation location = getResource(isHovered);
        if (location != null) {
            EnchiridionAPI.draw.drawImage(location, position.getLeft(), position.getTop(), position.getRight(), position.getBottom());
        }

        EnchiridionAPI.draw.drawSplitScaledString(getText(isHovered), position.getLeft() + getTextOffsetX(isHovered), position.getTop() + getTextOffsetY(isHovered), 200, 0x555555, size);
    }

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorButton.INSTANCE.setButton(this));
        return true;
    }

    @Override
    public void performClick(int mouseX, int mouseY, int button) {
        if (action != null && action.isVisible() && processesClick(button)) action.performAction();
    }

    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        if (action != null && action.isVisible()) {
            String[] title = getTooltip().split("\n");
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
            setResourceLocation(false, new ResourceLocation(dflt));
        }

        if (json.get("hover") != null) {
            String hover = json.get("hover").getAsJsonObject().get("path").getAsString();
            setResourceLocation(true, new ResourceLocation(hover));
        }

        //Fix some more
        if (json.get("action") != null && json.get("action").getAsJsonObject().get("properties") != null) {
            JsonObject properties = json.get("action").getAsJsonObject().get("properties").getAsJsonObject();
            if (properties.get("hoveredResource") != null) {
                String hover = properties.get("hoveredResource").getAsString();
                setResourceLocation(true, new ResourceLocation(hover));
            }

            if (properties.get("unhoveredResource") != null) {
                String hover = properties.get("unhoveredResource").getAsString();
                setResourceLocation(false, new ResourceLocation(hover));
            }

            if (properties.get("tooltip") != null) {
                setTooltip(properties.get("tooltip").getAsString());
            }

            if (properties.get("hoverText") != null) {
                setText(true, properties.get("hoverText").getAsString());
            }

            if (properties.get("unhoveredText") != null) {
                setText(false, properties.get("unhoveredText").getAsString());
            }

            if (properties.get("hoverXOffset") != null) {
                setTextOffsetX(true, properties.get("hoverXOffset").getAsInt());
            }

            if (properties.get("hoverYOffset") != null) {
                setTextOffsetY(true, properties.get("hoverYOffset").getAsInt());
            }

            if (properties.get("unhoveredXOffset") != null) {
                setTextOffsetX(false, properties.get("unhoveredXOffset").getAsInt());
            }

            if (properties.get("unhoveredYOffset") != null) {
                setTextOffsetY(false, properties.get("unhoveredYOffset").getAsInt());
            }
        }

        if (!leftClick && !rightClick && !otherClick) {
            leftClick = true;
            rightClick = true;
            otherClick = true;
        }

        return this;
    }

    @Override
    public String getText(boolean isHovered) {
        return isHovered ? hoverText : unhoveredText;
    }

    @Override
    public String getTooltip() {
        return tooltip;
    }

    @Override
    public boolean processesClick(int button) {
        return button == 0 ? leftClick : button == 1 ? rightClick : otherClick;
    }

    @Override
    public int getTextOffsetX(boolean isHovered) {
        return isHovered ? hoverXOffset: unhoveredXOffset;
    }

    @Override
    public int getTextOffsetY(boolean isHovered) {
        return isHovered ? hoverYOffset : unhoveredYOffset;
    }

    @Override
    public ResourceLocation getResource(boolean isHovered) {
        return isHovered ? hovered : unhovered;
    }

    @Override
    public IButtonActionProvider setResourceLocation(boolean isHovered, ResourceLocation resource) {
        if (isHovered) hovered = resource;
        else unhovered = resource;
        return this;
    }

    @Override
    public IButtonActionProvider setText(boolean isHovered, String text) {
        if (isHovered) hoverText = text;
        else unhoveredText = text;
        return this;
    }

    @Override
    public IButtonActionProvider setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public IButtonActionProvider setTextOffsetX(boolean isHovered, int x) {
        if (isHovered) hoverXOffset = x;
        else unhoveredXOffset = x;
        return this;
    }

    @Override
    public IButtonActionProvider setTextOffsetY(boolean isHovered, int y) {
        if (isHovered) hoverYOffset = y;
        else unhoveredYOffset = y;
        return this;
    }

    @Override
    public IButtonActionProvider setProcessesClick(int button, boolean value) {
        if (button == 0) leftClick = value;
        else if (button == 1) rightClick = value;
        else otherClick = value;
        return this;
    }

    @Override
    public void readFromJson(JsonObject object) {
        hovered = new ResourceLocation(JSONHelper.getStringIfExists(object, "hoveredResource"));
        unhovered = new ResourceLocation(JSONHelper.getStringIfExists(object, "unhoveredResource"));
    }

    @Override
    public void writeToJson(JsonObject object) {
        if (hovered != null) object.addProperty("hoveredResource", hovered.toString());
        if (unhovered != null) object.addProperty("unhoveredResource", unhovered.toString());
    }
}
