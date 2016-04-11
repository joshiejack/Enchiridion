package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.data.book.BookEvents;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.util.ELocation;

import java.util.regex.Pattern;

public class ActionToggleLayer extends AbstractAction {
	public transient String layer = "1";
    public transient boolean comma = true;
    public transient boolean regex = false;

	public ActionToggleLayer() {
		super("toggle");
        hovered = new ELocation("layer_hover");
        unhovered = new ELocation("layer_dftl");
	}

	@Override
	public ActionToggleLayer copy() {
	    ActionToggleLayer action = new ActionToggleLayer();
        action.comma = comma;
        action.regex = regex;
	    action.layer = layer;
	    return action;
	}

	@Override
	public IButtonAction create() {
		ActionToggleLayer action = new ActionToggleLayer();
        action.comma = true;
        action.regex = false;
		action.layer = "1";
		return action;
	}

	@Override
	public String[] getFieldNames() {
		return new String[] { "layer", "comma", "regex" };
	}
	
	@Override
	public void performAction() {
        try {
            if (regex) {
                Pattern p = Pattern.compile(layer);
                if (p != null) {
                    BookEvents.invert(EnchiridionAPI.book.getBook(), EnchiridionAPI.book.getPage(), p);
                }
            } else if (comma) {
                String[] ss = layer.replace(" ", "").split(",");
                for (String s: ss) {
                    BookEvents.invert(EnchiridionAPI.book.getBook(), EnchiridionAPI.book.getPage(), Pattern.compile(s));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
 	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
        layer = JSONHelper.getStringIfExists(json, "layer");
        comma = JSONHelper.getBooleanIfExists(json, "comma");
        regex = JSONHelper.getBooleanIfExists(json, "regex");
	}

	@Override
	public void writeToJson(JsonObject object) {
		super.writeToJson(object);
		object.addProperty("layer", layer);
        object.addProperty("comma", comma);
        object.addProperty("regex", regex);
	}
}
