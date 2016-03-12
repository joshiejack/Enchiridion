package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAction implements IButtonAction {
	private transient ResourceLocation hovered;
	private transient ResourceLocation unhovered;
	private transient String name;
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
	}

	@Override
	public void writeToJson(JsonObject object) {
		if (tooltip != null && !tooltip.equals("")) object.addProperty("tooltip", tooltip);
		if (hoverText != null && !hoverText.equals("")) object.addProperty("hoverText", hoverText);
		if (unhoveredText != null && !unhoveredText.equals("")) object.addProperty("unhoveredText", unhoveredText);
	}
	
	@Override
	public String getName() {
		return Enchiridion.translate("action." + name);
	}
	
	@Override
	public String getHoverText() {
	    if (hoverText == null) hoverText = "";
	    return hoverText;
	}
	
	@Override
    public String getUnhoverText() {
        if (unhoveredText == null) unhoveredText = "";
        return unhoveredText;
    }
	
	@Override
	public ResourceLocation getHovered() {
		return hovered;
	}
	
	@Override
	public ResourceLocation getUnhovered() {
		return unhovered;
	}
}
