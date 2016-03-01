package joshie.enchiridion.api.gui;

import net.minecraft.util.ResourceLocation;

public interface IToolbarButton {
	/** Return the resource location for this button **/
	public ResourceLocation getResource();

	/** Return the resource location for when this button is hovered over **/
	public ResourceLocation getHoverResource();
	
	/** On Click **/
	public void performAction();

	/** Return the the tooltip for hovering over this button **/
	public String getTooltip();

	/** If this button is left aligned,
	 *  otherwise it'll be assigned to the right instead  */
	boolean isLeftAligned();
}
