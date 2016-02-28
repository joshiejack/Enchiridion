package joshie.enchiridion.books.features.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.ELocation;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IButtonAction;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAction implements IButtonAction {
	private transient ResourceLocation hovered;
	private transient ResourceLocation unhovered;
	private transient String name;
	
	public AbstractAction() {}
	public AbstractAction(String name) {
		this.hovered = new ELocation(name + "_hover");
		this.unhovered = new ELocation(name + "_dftl");
		this.name = name;
	}
	
	@Override
	public String[] getFieldNames() {
		return new String[0];
	}
	
	@Override
	public void readFromJson(JsonObject object) {}

	@Override
	public void writeToJson(JsonObject object) {}
	
	@Override
	public String getName() {
		return Enchiridion.translate("action." + name);
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
