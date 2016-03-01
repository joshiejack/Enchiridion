package joshie.enchiridion.gui.book.features;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.lib.helpers.JSONHelper;

public class FeatureJump extends FeatureAbstract {
	public transient IPage page;
	private transient String name;
	private transient int number;
	private transient String jumpTo;
	
	public FeatureJump(){}
	public FeatureJump(String name, int number, String jumpTo) {
	    this.name = name;
	    this.number = number;
	    this.jumpTo = jumpTo;
	}
	
	@Override
	public FeatureJump copy() {
	    return new FeatureJump(name, number, jumpTo);
	}
	
	@Override
	public void draw(int posX, int posY, double width, double height, boolean isMouseHovering) {
		if (page == null) {
			if (jumpTo != null && !jumpTo.equals("#LEGACY#")) {
				page = JumpHelper.getPageByName(jumpTo);
				if (page == null) {
					try {
						page = JumpHelper.getPageByNumber(Integer.parseInt(jumpTo));
					} catch (Exception e) {}
				}
			} else {
				page = JumpHelper.getPageByNumber(number);
				if (page == null) page = JumpHelper.getPageByName(name);
			}
		}
	}
	
	@Override
	public void performAction (int mouseX, int mouseY) {
		EnchiridionAPI.book.setPage(page);
	}
	
	@Override
	public void readFromJson(JsonObject json) {
		number = JSONHelper.getIntegerIfExists(json, "number");
		name = JSONHelper.getStringIfExists(json, "name");
		if (json.get("jumpTo") != null) {
			jumpTo = JSONHelper.getStringIfExists(json, "jumpTo");
		} else jumpTo = "#LEGACY#";
	}

	@Override
	public void writeToJson(JsonObject object) {
		if (page != null) {
			object.addProperty("number", page.getPageNumber());
			object.addProperty("name", page.getPageName());
		}
	}
}
