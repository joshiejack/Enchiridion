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
	public transient String tooltip = "";
	public transient String hoverText = "";
	public transient String unhoveredText = "";

	public AbstractAction() {}
	public AbstractAction(String name) {
		this.hovered = new ELocation(name + "_hover");
		this.unhovered = new ELocation(name + "_dftl");
		this.name = name;
	}
	
	@Override
	public void initAction() {}
	
	@Override
	public String[] getFieldNames() {
		return new String[] { "tooltip", "hoverText", "unhoveredText" };
	}
	
	@Override
	public String getTooltip() {
		return tooltip;
	}
	
	@Override
	public void readFromJson(JsonObject object) {
		tooltip = JSONHelper.getStringIfExists(object, "tooltip");
		hoverText = JSONHelper.getStringIfExists(object, "hoverText");
		unhoveredText = JSONHelper.getStringIfExists(object, "unhoveredText");
        hovered = new ResourceLocation(JSONHelper.getStringIfExists(object, "hoveredResource"));
        unhovered = new ResourceLocation(JSONHelper.getStringIfExists(object, "unhoveredResource"));
	}

	@Override
	public void writeToJson(JsonObject object) {
		if (tooltip != null && !tooltip.equals("")) object.addProperty("tooltip", tooltip);
		if (hoverText != null && !hoverText.equals("")) object.addProperty("hoverText", hoverText);
		if (unhoveredText != null && !unhoveredText.equals("")) object.addProperty("unhoveredText", unhoveredText);
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
	public ResourceLocation getResource(boolean isHovered) {
        return isHovered ? hovered : unhovered;
	}

    @Override
    public IButtonAction setResourceLocation(String type, ResourceLocation resource) {
        if (type.equals("hovered")) hovered = resource;
        else if (type.equals("unhovered")) unhovered = resource;
        return this;
    }

    @Override
    public IButtonAction setText(String type, String text) {
        if (type.equals("hovered")) hoverText = text;
        else if (type.equals("unhovered")) unhoveredText = text;
        return this;
    }
}
