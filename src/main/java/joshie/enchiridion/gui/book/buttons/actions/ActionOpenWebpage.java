package joshie.enchiridion.gui.book.buttons.actions;

import java.awt.Desktop;
import java.net.URI;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.lib.helpers.JSONHelper;

public class ActionOpenWebpage extends AbstractAction {
	public String url;
	
	public ActionOpenWebpage() {
		super("web");
		this.url = "http://www.joshiejack.uk/enchiridion";
	}
	
	@Override
    public ActionOpenWebpage copy() {
	    ActionOpenWebpage action = new ActionOpenWebpage();
        action.url = url;
        return action;
    }
	
	@Override
	public IButtonAction create() {
		return new ActionOpenWebpage();
	}
	
	@Override
	public String[] getFieldNames() {
		return new String[] { "tooltip", "url" };
	}
		
	@Override
	public void performAction() {	
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {}
	}

	@Override
	public void readFromJson(JsonObject json) {
		url = JSONHelper.getStringIfExists(json, "url");
	}

	@Override
	public void writeToJson(JsonObject object) {
		object.addProperty("url", url);
	}
}
