package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.helpers.JSONHelper;

import java.awt.*;
import java.net.URI;

public class ActionOpenWebpage extends AbstractAction {
	public String url;
	
	public ActionOpenWebpage() {
		super("web");
		this.url = "http://www.joshiejack.uk/";
	}
	
	@Override
    public ActionOpenWebpage copy() {
	    ActionOpenWebpage action = new ActionOpenWebpage();
        action.url = url;
		action.tooltip = tooltip;
		action.hoverText = hoverText;
		action.unhoveredText = unhoveredText;
        return action;
    }
	
	@Override
	public IButtonAction create() {
		return new ActionOpenWebpage();
	}
	
	@Override
	public String[] getFieldNames() {
		return new String[] { "tooltip", "hoverText", "unhoveredText", "url" };
	}
		
	@Override
	public void performAction() {	
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {}
	}

	@Override
	public void readFromJson(JsonObject json) {
	    super.readFromJson(json);
		url = JSONHelper.getStringIfExists(json, "url");
	}

	@Override
	public void writeToJson(JsonObject object) {
	    super.writeToJson(object);
		object.addProperty("url", url);
	}
}
