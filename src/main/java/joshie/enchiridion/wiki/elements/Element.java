package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.designer.DrawHelper;
import joshie.enchiridion.designer.DrawHelper.DrawType;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiMain;

import com.google.gson.annotations.Expose;

public abstract class Element {
    public static final int BASE_X = 280;
    public static final int BASE_Y = 65;

    @Expose
    public int x = 0;
    @Expose
    public int y = 0;
    @Expose
    public int width = 365;
    @Expose
    public int height = 500;
    @Expose
    public float size = 2F;
    protected int scroll;

    protected int left;
    protected int right;
    protected int top;
    protected int bottom;
    protected int scaledX;
    protected int scaledY;

    public void recalculate() {
        left = x;
        right = x + (width * 2);
        top = y - scroll;
        bottom = y + (height * 2) - scroll;
        scaledX = (int) ((WikiHelper.theLeft + BASE_X + left) / size);
        scaledY = ((int) ((WikiHelper.theTop + BASE_Y + top) / size));
    }

    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    //Whether the box is currently being selected
    protected boolean isSelected;
    //Whether the box is currently being moved
    protected boolean isMoving;

    public abstract Element setToDefault();

    public abstract void display();

    public void display(int scroll, boolean isEditMode) {
        this.scroll = scroll;
        recalculate();

        DrawHelper.update(DrawType.WIKI, left, top, height, width, size);
        if (isEditMode && isSelected) {
            WikiHelper.drawRect((int) (BASE_X + left - (size * 4)), (int) (BASE_Y + top - (size * 4)), BASE_X + left, BASE_Y + top, 0xFF2693FF);
            WikiHelper.drawRect(BASE_X + right, (int) (BASE_Y + top - (size * 4)), (int) (BASE_X + right + (size * 4)), BASE_Y + top, 0xFF2693FF);
            WikiHelper.drawRect((int) (BASE_X + left - (size * 4)), BASE_Y + bottom, BASE_X + left, (int) (BASE_Y + bottom + (size * 4)), 0xFF2693FF);
            WikiHelper.drawRect(BASE_X + right, BASE_Y + bottom, (int) (BASE_X + right + (size * 4)), (int) (BASE_Y + bottom + (size * 4)), 0XFFFFFF00);
        }

        display();
    }

    public void markDirty() {
        WikiHelper.getPage().markDirty();
    }

    /** Called when the gui is initialized **/
    public void addEditButtons(List list) {
        return;
    }

    public void whileSelected() {
        return;
    }

    public void onSelected(int x, int y, int button) {
        onSelected(x, y);
    }

    public void onSelected(int x, int y) {
        return;
    }

    public void onDeselected() {
        return;
    }

    protected GuiMain wiki;

    public Element setWiki(GuiMain wiki) {
        this.wiki = wiki;
        return this;
    }

    public void keyTyped(char character, int key) {
        return;
    }

    protected int getX(int x2) {
        return wiki.getLeft(size, (BASE_X + x) + x2);
    }

    protected int getY(int y2) {
        return wiki.getTop(size, (BASE_Y + y) + y2 - scroll);
    }

    int lastX = 0;
    int lastY = 0;

    private boolean isDragging;

    protected boolean selectedCorner(int x, int y) {
        if ((x >= left - (size * 4) && x <= left) || (x >= right && x <= right + (size * 4))) {
            if ((y >= top - (size * 4) && y <= top) || (y >= bottom && y <= bottom + (size * 4))) {
                return true;
            }
        }

        return false;
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

    protected boolean isMouseOver(int x, int y) {
        return y > -250 && x >= left && x <= right && y >= top && y <= bottom;
    }

    /** The stuff shall follow the almighty cursor **/
    public void follow(int x, int y) {
        if (isMoving) {
            this.x += (x - lastX);
            this.y += (y - lastY);
            this.lastX = x;
            this.lastY = y;
        } else if (isDragging) {
            int changeX = ((x - lastX) / 2);
            int changeY = ((y - lastY) / 2);
            updateWidth(changeX);
            updateHeight(changeY);
            lastX = x;
            lastY = y;
        }
    }

    //Return true if the item has been deselected
    public boolean releaseButton(int x, int y, int button, boolean isEditMode) {
        if (isEditMode && button == 0) {
            if (isDragging || isMoving) {
                isDragging = isMoving = false;
                return false;
            } else return false;
        } else return false;
    }

    //Return true if the item has been selected, Return false if it has been deselected
    public boolean clickButton(int x, int y, int button) {
        int relX = x - this.x;
        int relY = y - this.y;

        if (button == 0) {
            if (isSelected) {
                if (x <= 0) {
                    return true;
                } else if (selectedCorner(x, y)) {
                    isSelected = isDragging = true;
                    lastX = x;
                    lastY = y;
                    onSelected(relX, relY, button);
                    return true;
                } else if (isMouseOver(x, y)) {
                    isSelected = isMoving = true;
                    lastX = x;
                    lastY = y;
                    onSelected(relX, relY, button);
                    return true;
                } else {
                    isSelected = false;
                    onDeselected();
                    return false;
                }
            } else {
                if (selectedCorner(x, y)) {
                    isSelected = isDragging = true;
                    lastX = x;
                    lastY = y;
                    onSelected(relX, relY, button);
                    return true;
                } else if (isMouseOver(x, y)) {
                    isSelected = isMoving = true;
                    lastX = x;
                    lastY = y;
                    onSelected(relX, relY, button);
                    return true;
                } else return false;
            }
        } else return false;
    }

    public void deselect() {
        isSelected = isDragging = isMoving = false;
        recalculate();
        onDeselected();
    }
}
