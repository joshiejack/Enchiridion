package joshie.enchiridion.gui.book.features;

import com.google.gson.JsonObject;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.book.IFeatureProvider;

import java.util.List;

public abstract class FeatureAbstract implements IFeature {
	@Override
	public void update(IFeatureProvider position) {}
	
	@Override
	public void draw(int posX, int posY, double width, double height, boolean isMouseHovering) {}

	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {}

	@Override
	public void keyTyped(char character, int key) {}

	@Override
	public boolean getAndSetEditMode() {
		return false;
	}

	@Override
	public void performClick(int mouseX, int mouseY) {}

	@Override
	public void performRelease(int mouseX, int mouseY) {}

	@Override
	public void follow(int mouseX, int mouseY) {}

	@Override
	public void scroll(boolean down, int amount) {}

	@Override
	public void onDeselected() {}
	
	@Override
	public void readFromJson(JsonObject json) {}
	
	@Override
	public void writeToJson(JsonObject json) {}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
}
