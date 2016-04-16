package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAction implements IButtonAction {
    protected transient ResourceLocation hovered;
    protected transient ResourceLocation unhovered;
    protected transient String name;
    public String tooltip = "";
    public String hoverText = "";
    public int hoverXOffset = 0;
    public int hoverYOffset = 0;
    public String unhoveredText = "";
    public int unhoveredXOffset;
    public int unhoveredYOffset;

    public AbstractAction() {}
    public AbstractAction(String name) {
        this.hovered = new ELocation(name + "_hover");
        this.unhovered = new ELocation(name + "_dftl");
        this.name = name;
    }

    public IButtonAction copyAbstract(AbstractAction action) {
        action.hovered = hovered;
        action.unhovered = unhovered;
        action.name = name;
        action.tooltip = tooltip;
        action.hoverText = hoverText;
        action.unhoveredText = unhoveredText;
        action.hoverXOffset = hoverXOffset;
        action.hoverYOffset = hoverYOffset;
        action.unhoveredXOffset = unhoveredXOffset;
        action.unhoveredYOffset = unhoveredYOffset;
        return this;
    }

    @Override
    public void onFieldsSet(String field) {
        if (field.equals("")) initAction();
    }

    public void initAction() {}

    @Override
    public String getTooltip() {
        return tooltip;
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

    @Override
    public String getName() {
        return Enchiridion.translate("action." + name);
    }

    @Override
    public String getText(boolean isHovered) {
        return isHovered ? hoverText : unhoveredText;
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
    public IButtonAction setResourceLocation(boolean isHovered, ResourceLocation resource) {
        if (isHovered) hovered = resource;
        else unhovered = resource;
        return this;
    }

    @Override
    public IButtonAction setText(boolean isHovered, String text) {
        if (isHovered) hoverText = text;
        else unhoveredText = text;
        return this;
    }

    @Override
    public IButtonAction setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public IButtonAction setTextOffsetX(boolean isHovered, int x) {
        if (isHovered) hoverXOffset = x;
        else unhoveredXOffset = x;
        return this;
    }

    @Override
    public IButtonAction setTextOffsetY(boolean isHovered, int y) {
        if (isHovered) hoverYOffset = y;
        else unhoveredYOffset = y;
        return this;
    }
}
