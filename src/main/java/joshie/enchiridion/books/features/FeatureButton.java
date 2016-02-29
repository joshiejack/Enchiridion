package joshie.enchiridion.books.features;

import java.util.List;

import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.books.gui.GuiSimpleEditor;
import joshie.enchiridion.books.gui.GuiSimpleEditorButton;

public class FeatureButton extends FeatureJump {
	public FeatureImage deflt;
	public FeatureImage hover;
	public IButtonAction action;
	
	public FeatureButton(){}
	public FeatureButton(String deflt, String hover, IButtonAction action) {
		this.deflt = new FeatureImage(deflt);
		this.hover = new FeatureImage(hover);
		this.action = action;
	}
	
	@Override
	public void update(IFeatureProvider position) {
		if (deflt == null || hover == null) return;
		else {
			deflt.update(position);
			hover.update(position);
		}
	}
	
	@Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
		if (deflt == null || hover == null) return;
		if (isMouseHovering) hover.draw(xPos, yPos, width, height, isMouseHovering);
		else {
			deflt.draw(xPos, yPos, width, height, isMouseHovering);
		}
	}
	
	@Override
	public boolean getAndSetEditMode() {
		if (deflt == null || hover == null) return false;
		GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorButton.INSTANCE.setButton(this));		
		return false;
	}
	
	@Override
	public void performAction(int mouseX, int mouseY) {
		if (action != null) action.performAction();
	}
	
	@Override
	public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
		if (action != null) {
			String[] title = action.getTooltip().split("\n");
			if (title != null) {
				boolean first = false;
				for (String t: title) {
					if (first || !t.equals("")) {
						tooltip.add(t);
					} else first = true;
				}
			}
		}
	}
}
