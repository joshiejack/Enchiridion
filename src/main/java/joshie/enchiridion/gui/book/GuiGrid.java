package joshie.enchiridion.gui.book;

import joshie.enchiridion.api.EnchiridionAPI;

public class GuiGrid extends AbstractGuiOverlay {
    public static final GuiGrid INSTANCE = new GuiGrid();
    private boolean pixelGrid = true;
    private boolean isVisible;
    private boolean isFullWidth;
    private int gridSize = 5;

    private GuiGrid() {}

    public int getGridSize() {
        return isPixelGrid() ? 3 : gridSize;
    }

    public boolean isActivated() {
        return isVisible;
    }
    
    public boolean isPixelGrid() {
        return pixelGrid;
    }

    public void toggle() {
        if (pixelGrid && !isVisible) {
            isVisible = true;
            gridSize = 5;
        } else if (pixelGrid && isVisible) {
            pixelGrid = false;
            isVisible = false;
        } else if (!isVisible && !pixelGrid) {
            gridSize = 5;
            isVisible = true;
        } else {
            if (!isFullWidth) {
                if (gridSize == 5) {
                    gridSize = 10;
                } else if (gridSize == 10) {
                    gridSize = 5;
                    isFullWidth = true;
                }
            } else {
                if (gridSize == 5) {
                    gridSize = 10;
                } else if (gridSize == 10) {
                    isFullWidth = false;
                    isVisible = false;
                    pixelGrid = true;
                }
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (isActivated()) {
            if (pixelGrid) {
                int xOffset = -1;
                int yOffset = 2;
                for (int x = -10; x < 440; x += getGridSize()) {
                    for (int y = -15; y < 245; y += getGridSize()) {
                        EnchiridionAPI.draw.drawRectangle(x + xOffset, y + yOffset, x + 1 + xOffset, y + yOffset + getGridSize(), 0x22000000);
                        EnchiridionAPI.draw.drawRectangle(x + xOffset, y + yOffset, x + xOffset + getGridSize(), y + 1 + yOffset, 0x22000000);
                    }
                }
            } else if (isFullWidth) {
                for (int x = -10; x < 440; x += getGridSize()) {
                    for (int y = -15; y < 245; y += getGridSize()) {
                        EnchiridionAPI.draw.drawRectangle(x, y, x + 1, y + getGridSize(), 0x22000000);
                        EnchiridionAPI.draw.drawRectangle(x, y, x + getGridSize(), y + 1, 0x22000000);
                    }
                }
            } else {
                for (int x = -10; x < 440; x += getGridSize()) {
                    for (int y = -15; y < 245; y += getGridSize()) {
                        EnchiridionAPI.draw.drawRectangle(x, y, x + 1, y + 1, 0x22000000);
                    }
                }
            }
        }
    }
}
