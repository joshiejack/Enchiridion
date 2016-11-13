package joshie.enchiridion.api.gui;

import net.minecraft.util.ResourceLocation;

public interface IToolbarButton {
    /** @return the resource location for this button **/
    ResourceLocation getResource();

    /** @return the resource location for when this button is hovered over **/
    ResourceLocation getHoverResource();

    /** On Click **/
    void performAction();

    /** @return the the tooltip for hovering over this button **/
    String getTooltip();

    /** If this button is left aligned,
     *  otherwise it'll be assigned to the right instead  */
    boolean isLeftAligned();
}