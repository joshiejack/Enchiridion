package joshie.enchiridion.api;

import java.util.List;

public interface IFeatureProvider {
	public IFeature getFeature();
	
	public int getX();
	public int getY();
	public double getWidth();
	public double getHeight();
	public boolean isVisible();
	public boolean isLocked();
	public int getLayerIndex();
	public long getTimeChanged();
	
	public void setX(int x);
	public void setY(int y);
	public void setWidth(double w);
	public void setHeight(double h);
	public void setVisible(boolean value);
	public void setLocked(boolean value);
	public void setLayerIndex(int  i);

	public boolean mouseClicked(int mouseX, int mouseY);

	public void draw(int mouseX, int mouseY);

	public void addTooltip(List<String> tooltip, int mouseX, int mouseY);

	public void deselect();

	/** Return true if this provider should be deleted **/
	public boolean keyTyped(char character, int key);

	public void select(int mouseX, int mouseY);

	public void mouseReleased(int mouseX, int mouseY);

	public void follow(int mouseX, int mouseY);

	public void scroll(boolean down);

}
