package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.data.book.BookEvents;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.util.ELocation;

public class ActionToggleLayer extends AbstractAction {
	public transient int layer = 1;

	public ActionToggleLayer() {
		super("toggle");
        hovered = new ELocation("layer_hover");
        unhovered = new ELocation("layer_dftl");
	}

	@Override
	public ActionToggleLayer copy() {
	    ActionToggleLayer action = new ActionToggleLayer();
	    action.layer = layer;
	    return action;
	}

	@Override
	public IButtonAction create() {
		ActionToggleLayer action = new ActionToggleLayer();
		action.layer = 0;
		return action;
	}

	@Override
	public String[] getFieldNames() {
		return new String[] { "layer" };
	}
	
	@Override
	public void performAction() {
        BookEvents.invert(EnchiridionAPI.book.getBook(), EnchiridionAPI.book.getPage(), layer - 1);
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
        layer = JSONHelper.getIntegerIfExists(json, "layer");
	}

	@Override
	public void writeToJson(JsonObject object) {
		super.writeToJson(object);
		object.addProperty("layer", layer);
	}
}
