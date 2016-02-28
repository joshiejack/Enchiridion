package joshie.enchiridion.books.buttons;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IToolbarButton;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractButton implements IToolbarButton {
	private ResourceLocation dflt;
	private ResourceLocation hover;
	private String translate;
	
	public AbstractButton(String name) {
		dflt = new ResourceLocation(EInfo.TEXPATH + name + "_dftl.png");
		hover = new ResourceLocation(EInfo.TEXPATH + name + "_hover.png");
		translate = "button." + name;
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
