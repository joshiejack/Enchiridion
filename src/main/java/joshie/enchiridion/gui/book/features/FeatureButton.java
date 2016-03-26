package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorButton;

import java.util.List;

public class FeatureButton extends FeatureJump {
	public FeatureImage deflt;
	public FeatureImage hover;
	public IButtonAction action;
	public float size = 1F;
	public transient FeatureText textHover;
	public transient FeatureText textUnhover;
	public transient IFeatureProvider provider;
	private transient boolean isInit = false;
	
	public FeatureButton(){}
	public FeatureButton(String deflt, String hover, IButtonAction action) {
		this.deflt = new FeatureImage(deflt);
		this.hover = new FeatureImage(hover);
		this.action = action;
	}
	
	@Override
	public FeatureButton copy() {
	    FeatureButton button = new FeatureButton(deflt.path, hover.path, action.copy());
        button.size = size;
        button.textHover = textHover == null ? null : textHover.copy();
        button.textUnhover = textUnhover == null ? null : textUnhover.copy();
        return button;
	}
	
	@Override
	public void update(IFeatureProvider position) {
		if (deflt == null || hover == null) return;
		else {
		    isInit = false; //Reset the init when something changes
		    provider = position;
			deflt.update(position);
			hover.update(position);
			textHover = new FeatureText(action.getHoverText());
			textHover.size = this.size;
			textHover.update(position);
			textUnhover = new FeatureText(action.getUnhoverText());
			textUnhover.update(position);
			textHover.size = this.size;
		}
	}

    @Override
    public void keyTyped(char character, int key) {
        if (textHover != null) {
            textHover.keyTyped(character, key);
            this.size = textHover.size;
        }

        if (textUnhover != null){
            textUnhover.keyTyped(character, key);
            this.size = textHover.size;
        }
    }
	
	@Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
		if (deflt == null || hover == null) return;
		if (!isInit && action != null) { //Called here because action needs everything to be loaded, where as update doesn't
		    action.initAction();
		    isInit = true;
		}
		
		if (isMouseHovering) {
		    hover.draw(xPos, yPos, width, height, isMouseHovering);
		    if (textHover != null) textHover.draw(xPos, yPos, width, height, isMouseHovering);
		} else {
			deflt.draw(xPos, yPos, width, height, isMouseHovering);
			if (textUnhover != null) textUnhover.draw(xPos, yPos, width, height, isMouseHovering);
		}
	}
	
	@Override
	public boolean getAndSetEditMode() {
		if (deflt == null || hover == null) return false;
		GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorButton.INSTANCE.setButton(this));		
		return false;
	}
	
	@Override
	public void performClick(int mouseX, int mouseY) {
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
