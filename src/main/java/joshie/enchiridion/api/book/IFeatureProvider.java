package joshie.enchiridion.api.book;

import java.util.List;

public interface IFeatureProvider {
    //Getters
    public IFeature getFeature();
    public int getLeft();
    public int getRight();
    public int getTop();
    public int getBottom();
    public double getWidth();
    public double getHeight();
    public boolean isVisible();
    public boolean isLocked();
    public int getLayerIndex();
    public long getTimeChanged();

    //Setters
    public void setX(int x);
    public void setY(int y);
    public void setWidth(double w);
    public void setHeight(double h);
    public void setVisible(boolean value);
    public void setLocked(boolean value);
    public void setLayerIndex(int  i);

    //Rendering and functions
    public void update(IPage page);
    public void draw(int mouseX, int mouseY);
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY);
    public boolean mouseClicked(int mouseX, int mouseY, int button);
    public void mouseReleased(int mouseX, int mouseY, int button);
    public void select(int mouseX, int mouseY);
    public void deselect();
    public void follow(int mouseX, int mouseY, boolean force);
    public boolean keyTyped(char character, int key);
    public void scroll(int mouseX, int mouseY, boolean down);
    public boolean isOverFeature(int mouseX, int mouseY);

    // Returns a new copy of this provider
    public IFeatureProvider copy();
    // Returns the page this feature is in
    public IPage getPage();
}
