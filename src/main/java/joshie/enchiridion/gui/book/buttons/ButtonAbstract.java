package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.gui.IToolbarButton;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

public abstract class ButtonAbstract implements IToolbarButton {
	protected ResourceLocation dflt;
	protected ResourceLocation hover;
	protected String translate;
	protected String name;
	
	public ButtonAbstract(String name) {
		dflt = new ResourceLocation(EInfo.TEXPATH + name + "_dftl.png");
		hover = new ResourceLocation(EInfo.TEXPATH + name + "_hover.png");
		translate = "button." + name;
		this.name = name;
	}
	
	@Override
	public boolean isLeftAligned() {
		return true;
	}
	
	@Override
	public ResourceLocation getResource() {
		return dflt;
	}

	@Override
	public ResourceLocation getHoverResource() {
		return hover;
	}
	
	@Override
	public String getTooltip() {
		return Enchiridion.translate(translate);
	}
}
