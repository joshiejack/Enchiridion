package joshie.enchiridion.data.book;

import java.util.List;

import org.lwjgl.input.Keyboard;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.gui.book.GuiGrid;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.lib.editables.TextEditor;
import joshie.lib.helpers.ClientHelper;
import joshie.lib.lib.CharacterCodes;

public class FeatureProvider implements IFeatureProvider {
    public int xPos;
    public int yPos;
    public double width;
    public double height;
    public IFeature feature;
    public boolean isLocked;
    public boolean isHidden;
    public int layerIndex;

    public transient int left;
    public transient int right;
    public transient int top;
    public transient int bottom;

    private transient boolean isSelected;
    private transient boolean isEditing;
    private transient boolean isHeld;
    private transient int prevX;
    private transient int prevY;
    private transient int originalX;
    private transient int originalY;
    private transient boolean isDragging;
    private transient boolean dragTopLeft;
    private transient boolean dragTopRight;
    private transient boolean dragBottomLeft;
    private transient boolean dragBottomRight;
    private transient double ratio;
    private transient long timestamp;

    public FeatureProvider() {}

    public FeatureProvider(IFeature feature, int x, int y, double width, double height) {
        this.feature = feature;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.isLocked = true;
        this.isHidden = false;
    }
    

    @Override
    public IFeatureProvider copy() {
        IFeatureProvider copy = new FeatureProvider(feature.copy(), xPos, yPos, width, height);
        copy.setLocked(isLocked);
        copy.setVisible(!isHidden);
        copy.setLayerIndex(layerIndex);
        return copy;
    }
    
    private boolean isOverFeature(int x, int y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    private boolean isOverTopLeftCorner(int x, int y) {
        return x >= left && x <= left + 2 && y >= top && y <= top + 2;
    }

    private boolean isOverTopRightCorner(int x, int y) {
        return x >= right - 2 && x <= right && y >= top && y <= top + 2;
    }

    private boolean isOverBottomRightCorner(int x, int y) {
        return x >= right - 2 && x <= right && y >= bottom - 2 && y <= bottom;
    }

    private boolean isOverBottomLeftCorner(int x, int y) {
        return x >= left && x <= left + 2 && y >= bottom - 2 && y <= bottom;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        left = xPos;
        right = (int) (xPos + width);
        top = yPos;
        bottom = (int) (yPos + height);
        if (!isHidden) {
            feature.draw(xPos, yPos, width, height, isOverFeature(mouseX, mouseY));
            if (isSelected) {
                int color = isEditing ? 0xCCFFFF00 : 0xCC007FFF;
                EnchiridionAPI.draw.drawRectangle(right - 2, top, right, top + 2, color);
                EnchiridionAPI.draw.drawRectangle(left, top, left + 2, top + 2, color);
                EnchiridionAPI.draw.drawRectangle(right - 2, bottom - 2, right, bottom, color);
                EnchiridionAPI.draw.drawRectangle(left, bottom - 2, left + 2, bottom, color);
            }
        }
    }
    
    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        if (isOverFeature(mouseX, mouseY)) {
            feature.addTooltip(tooltip, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyTyped(char character, int key) {
        if (isEditing) {
            feature.keyTyped(character, key);
        } else if (isSelected && key == CharacterCodes.DELETE_KEY && !TextEditor.INSTANCE.isEditing()) {
            GuiSimpleEditor.INSTANCE.setEditor(null); //Reset the editor
            TextEditor.INSTANCE.clearEditable();
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (EnchiridionAPI.book.isEditMode()) {
            GuiSimpleEditor.INSTANCE.setEditor(null); //Reset the editor
            TextEditor.INSTANCE.clearEditable();
        }

        if (isHidden) return false;
        if (isOverFeature(mouseX, mouseY)) {
            if (EnchiridionAPI.book.isEditMode()) {
                isEditing = feature.getAndSetEditMode();
            }

            //Perform clicks
            if (!EnchiridionAPI.book.isEditMode() || (EnchiridionAPI.book.isEditMode() && ClientHelper.isShiftPressed())) {
                feature.performAction(mouseX, mouseY);
            }

            if (!isLocked) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        isHeld = false;
        isDragging = false;
        dragTopLeft = false;
        dragTopRight = false;
        dragBottomLeft = false;
        dragBottomRight = false;
    }
    
    @Override
    public void select(int mouseX, int mouseY) {
        isSelected = true;
        prevX = mouseX;
        prevY = mouseY;
        originalX = mouseX;
        originalY = mouseY;

        if (isOverTopLeftCorner(mouseX, mouseY)) {
            isDragging = true;
            dragTopLeft = true;
        } else if (isOverTopRightCorner(mouseX, mouseY)) {
            isDragging = true;
            dragTopRight = true;
        } else if (isOverBottomRightCorner(mouseX, mouseY)) {
            isDragging = true;
            dragBottomRight = true;
        } else if (isOverBottomLeftCorner(mouseX, mouseY)) {
            isDragging = true;
            dragBottomLeft = true;
        } else isHeld = true;
    }

    @Override
    public void deselect() {
        isEditing = false;
        isSelected = false;
        isHeld = false;
        isDragging = false;
        dragTopLeft = false;
        dragTopRight = false;
        dragBottomLeft = false;
        dragBottomRight = false;
        feature.onDeselected();
    }

    @Override
    public void follow(int mouseX, int mouseY, boolean force) {
        if (isHeld || force) {
            if (force) {
                isSelected = true;
            }
            
            int changeX = (mouseX - prevX);
            int changeY = (mouseY - prevY);
            xPos += changeX;
            yPos += changeY;
            
            if (GuiGrid.INSTANCE.isActivated()) {
                int large = GuiGrid.INSTANCE.getGridSize();
                int small = large - 1;

                if (changeX < 0) {
                    xPos = (xPos - small) / large * large;
                } else if (changeX > 0) {
                    xPos = (xPos + small) / large * large;
                }

                if (changeY < 0) {
                    yPos = (yPos - small) / large * large;
                } else if (changeY > 0) {
                    yPos = (yPos + small) / large * large;
                }
            }
        } else if (isDragging) {
            int changeX = (mouseX - prevX);
            int changeY = (mouseY - prevY);
            double originalHeight = height;
            double originalWidth = width;
            int originalY = yPos;

            if (dragTopLeft) {
                xPos += changeX;
                updateWidth(-changeX);
                yPos += changeY;
                updateHeight(-changeY);
            } else if (dragTopRight) {
                updateWidth(changeX);
                yPos += changeY;
                updateHeight(-changeY);
            } else if (dragBottomRight) {
                updateWidth(changeX);
                updateHeight(changeY);
            } else if (dragBottomLeft) {
                xPos += changeX;
                updateWidth(-changeX);
                updateHeight(changeY);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (dragBottomLeft || dragBottomRight) height = (width * originalHeight) / originalWidth;
                else if (dragTopRight) width = (height * originalWidth) / originalHeight;
                else if (dragTopLeft) {
                    //TODO: FIX DRAGGING FROM TOP LEFT WHEN KEEPING RATIO
                    height = (width * originalHeight) / originalWidth;
                    yPos = (int) (originalY);
                }
            }
        }

        if (isHeld || isDragging || force) {
            prevX = mouseX;
            prevY = mouseY;
            feature.update(this);
        }
    }

    public void updateWidth(int change) {
        width += change;
        if (width <= 2) {
            width = 2;
        }
    }

    public void updateHeight(int change) {
        height += change;
        if (height <= 1) {
            height = 1;
        }
    }

    @Override
    public IFeature getFeature() {
        return feature;
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public boolean isVisible() {
        return !isHidden;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public long getTimeChanged() {
        return timestamp;
    }

    @Override
    public void setX(int x) {
        xPos = x;
    }

    @Override
    public void setY(int y) {
        yPos = y;
    }

    @Override
    public void setWidth(double w) {
        width = w;
    }

    @Override
    public void setHeight(double h) {
        height = h;
    }

    @Override
    public void setVisible(boolean v) {
        isHidden = !v;
    }

    @Override
    public void setLocked(boolean b) {
        isLocked = b;
    }

    @Override
    public void setLayerIndex(int i) {
        layerIndex = i;
        timestamp = System.currentTimeMillis();
    }
}
