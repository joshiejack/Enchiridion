package joshie.enchiridion.api.book;

import net.minecraft.util.ResourceLocation;

public interface IButtonActionProvider extends IFeature {
    public IButtonActionProvider copy();
    public IButtonAction getAction();

    /** Reduce this stuff **/
    public ResourceLocation getResource(boolean isHovered);
    public String getText(boolean isHovered);
    public int getTextOffsetX(boolean isHovered);
    public int getTextOffsetY(boolean isHovered);

    /** Return the tooltip **/
    public boolean processesClick(int button);
    public String getTooltip();

    /** Setters **/
    public IButtonActionProvider setTextOffsetX(boolean isHovered, int x);
    public IButtonActionProvider setTextOffsetY(boolean isHovered, int y);
    public IButtonActionProvider setResourceLocation(boolean isHovered, ResourceLocation resource);
    public IButtonActionProvider setText(boolean isHovered, String text);
    public IButtonActionProvider setTooltip(String tooltip);
    public IButtonActionProvider setProcessesClick(int button, boolean value);
}
