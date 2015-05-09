package joshie.enchiridion.designer.features;

import static joshie.enchiridion.designer.DesignerHelper.drawRect;
import static joshie.enchiridion.designer.DesignerHelper.getGui;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.designer.DesignerHelper;
import net.minecraft.client.renderer.entity.RenderItem;

import com.google.gson.annotations.Expose;

public abstract class Feature {
    public static final RenderItem itemRenderer = new RenderItem();

    @Expose
    protected int xPos;
    @Expose
    protected int yPos;
    @Expose
    protected double width;
    @Expose
    protected double height;

    public boolean isSelected;
    private boolean isHeld;
    private boolean isDragging;
    private int prevX;
    private int prevY;
    protected int left;
    protected int right;
    protected int top;
    protected int bottom;

    public Feature() {
        this.width = 50;
        this.height = 50;
    }

    public Feature(Feature feature) {
        this.xPos = feature.xPos;
        this.yPos = feature.yPos;
        this.width = feature.width;
        this.height = feature.height;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void recalculate(int x, int y) {
        left = xPos;
        right = (int) (xPos + width);
        top = yPos;
        bottom = (int) (yPos + height);
    }

    public void draw(int x, int y) {
        recalculate(x, y);
        drawFeature();
        //If We are in edit mode draw the boxes around the feature
        if (getGui().canEdit && isSelected) {
            drawRect(left - 4, top - 4, left, top, 0xFF2693FF);
            drawRect(right, top - 4, right + 4, top, 0xFF2693FF);
            drawRect(left - 4, bottom, left, bottom + 4, 0xFF2693FF);
            drawRect(right, bottom, right + 4, bottom + 4, 0XFFFFFF00);
        }
    }

    private boolean noOtherSelected() {
        return getGui().canvas.selected == null;
    }

    public void clearSelected() {
        getGui().canvas.selected = null;
    }

    public void setSelected() {
        getGui().canvas.selected = this;
    }

    public void drawFeature() {}

    public boolean isOverFeature(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public boolean isOverCorner(int x, int y) {
        if ((x >= left - 4 && x <= left) || (x >= right && x <= right + 4)) {
            if ((y >= top - 4 && y <= top) || (y >= bottom && y <= bottom + 4)) {
                return true;
            }
        }

        return false;
    }

    public void click(int x, int y) {
        //If it is editmode let's select or deselect this item whenever we click on it
        if (getGui().canEdit) {
            //If you click inside the box, then we will set the held to true
            if (isOverFeature(x, y) && noOtherSelected()) {
                isHeld = true;
                isSelected = true;
                prevX = x;
                prevY = y;
                setSelected();
            } else if (isOverCorner(x, y) && noOtherSelected()) {
                isSelected = true;
                isDragging = true;
                prevX = x;
                prevY = y;
                setSelected();
            } else {
                if (isSelected && x >= 0) {
                    clearSelected();

                    isSelected = false;
                }
            }
        }
    }

    public void release(int x, int y) {
        if (isHeld) {
            isHeld = false;
            clearSelected();
        } else if (isDragging) {
            isDragging = false;
            clearSelected();
        }
    }

    public void updateWidth(int change) {
        width += change;
        if (width <= 16) {
            width = 16;
        }
    }

    public void updateHeight(int change) {
        height += change;
    }

    public void follow(int x, int y) {
        if (isHeld) {
            xPos += x - prevX;
            yPos += y - prevY;
            prevX = x;
            prevY = y;
        } else if (isDragging) {
            int changeX = (x - prevX);
            int changeY = (y - prevY);
            updateWidth(changeX);
            updateHeight(changeY);
            prevX = x;
            prevY = y;
        }
        
        if (isOverFeature(x, y)) {
            List<String> tooltip = new ArrayList();
            addTooltip(tooltip);
            DesignerHelper.addTooltip(tooltip);
        }
    }
    
    public void addTooltip(List list) { }

    //Do nothing by default
    public void keyTyped(char character, int key) {
        return;
    }

    public void scroll(boolean scrolledDown) {
        return;
    }
}
