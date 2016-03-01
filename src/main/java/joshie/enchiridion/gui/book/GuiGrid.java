package joshie.enchiridion.gui.book;

import joshie.enchiridion.api.EnchiridionAPI;

public class GuiGrid extends AbstractGuiOverlay {   
	public static final GuiGrid INSTANCE = new GuiGrid();
	private boolean isVisible;
	private boolean isFullWidth;
	private int gridSize = 5;
	
    private GuiGrid() {}
	
	public int getGridSize() {
		return gridSize;
	}
    
    public boolean isActivated() {
    	return isVisible;
    }
    
    public void toggle() {
    	if (!isVisible) {
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
        		}
        	}
    	}
    }
    
    @Override
    public void draw(int mouseX, int mouseY) { 
    	if (isActivated()) {
    		if (isFullWidth) {
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
