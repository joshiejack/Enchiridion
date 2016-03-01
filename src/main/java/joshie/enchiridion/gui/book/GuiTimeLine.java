package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.JumpHelper;

public class GuiTimeLine extends AbstractGuiOverlay {
	public static final GuiTimeLine INSTANCE = new GuiTimeLine();
	private IPage dragged = null;
	private int held = 0;
	private int startPage = 0;
	
	private GuiTimeLine() {}
	
	private boolean isValid(int i) {
		return i == 0 || (i + 1) % 5 == 0;
	}
	
	private int getOffsetX(int index, int real) {
		if (index == 0) {
			return +1;
		} else if (index == 109) {
			if (real >= 10000) return -10;
			if (real >= 1000) return -8;
			 return -5;
		} else {
			if (index == 4 && real >= 1000) return -1;
			if (real >= 10000) return -6;
			if (real >= 1000) return -4;
			if (real >= 100) return -2;
			if (real >= 10) return -1;
		}

		return 0;
 	}
	
	private boolean isOverTimeLine(int xPos, int mouseX, int mouseY) {
        return mouseX >= xPos && mouseX <= xPos + 3 && mouseY >= EConfig.timelineYPos && mouseY <= EConfig.timelineYPos + 10;
    }
	
	@Override
	public void draw(int mouseX, int mouseY) {
		EnchiridionAPI.draw.drawImage(toolbar, -9, EConfig.timelineYPos - 9, 440, EConfig.timelineYPos + 13);
		//EnchiridionAPI.draw.drawBorderedRectangle(-6, EConfig.timelineYPos - 7, 437, EConfig.timelineYPos, 0xFF312921, 0xFF191511);
		EnchiridionAPI.draw.drawBorderedRectangle(-6, EConfig.timelineYPos, 437, EConfig.timelineYPos + 11, 0x00000000, 0xFF191511);
		int currentPageNumber = EnchiridionAPI.book.getPage().getPageNumber();
		int hoverX = 0;
		
		for (int j = 0; j < 110; j++) {
			int thisNumber = startPage + j;
			IPage page = JumpHelper.getPageByNumber(thisNumber);
			int positionX = -5 + (j * 4);
			int fill = 0xFFE6D4A7;
			boolean exists = page != null;
			if (exists) fill = 0xFFFFFFFF;
				
			if (isValid(j)) {
				EnchiridionAPI.draw.drawSplitScaledString("" + (thisNumber + 1), positionX + getOffsetX(j, thisNumber + 1), EConfig.timelineYPos - 5, 199, 0xFFDDDDDD, 0.5F);
				fill = exists ? 0xFFEEEEEE: 0xFFB0A483;
			}
			
			if (currentPageNumber == thisNumber)  fill = 0xFF8C0000; 
			if (isOverTimeLine(positionX, mouseX, mouseY)) {
				hoverX = positionX;
				fill = 0xFFFFFF00;
			}
			
			EnchiridionAPI.draw.drawRectangle(positionX, EConfig.timelineYPos, positionX + 5, EConfig.timelineYPos + 10, fill);
		}
		
		//Dragging!
		if (dragged != null) {
			if (held < 30) {
				held++;
			} else EnchiridionAPI.draw.drawRectangle(hoverX, EConfig.timelineYPos, hoverX + 4, EConfig.timelineYPos + 10, 0xFFFF9326);
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
		for (int i = 0; i < 110; i++) {
			int positionX = -5 + (i * 4);
			if (isOverTimeLine(positionX, mouseX, mouseY)) {
				//If we don't succeed at jumping to the page because it doesn't exist
				//Then we should create it, and then jump to it;
				dragged = JumpHelper.getPageByNumber(startPage + i);
				return true;
			}
		}
		
		dragged = null;
		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		boolean placing = held >= 30;
		for (int i = 0; i < 110; i++) {
			int positionX = -5 + (i * 4);
			if (isOverTimeLine(positionX, mouseX, mouseY)) {
				//If we don't succeed at jumping to the page because it doesn't exist
				//Then we should create it, and then jump to it;
				int thisNumber = startPage + i;
				if (placing) {
					JumpHelper.insertPage(thisNumber, dragged);
				} else if (!JumpHelper.jumpToPageByNumber(thisNumber)) {
					IPage page = DefaultHelper.addDefaults(new Page(thisNumber));
					EnchiridionAPI.book.getBook().addPage(page);
					JumpHelper.jumpToPageByNumber(thisNumber);
				}
			}
		}
		
		//Reset everything
		dragged = null;
		held = 0;
	}

	@Override
	public void scroll(boolean down, int mouseX, int mouseY) {
		if (mouseX >= -5 && mouseX <= 431 && mouseY >= EConfig.timelineYPos && mouseY <= EConfig.timelineYPos + 10) {
			if (down) {
				startPage += 5;
			} else {
				startPage -= 5;
				if (startPage <= 0) {
					startPage = 0;
				}
			}
		}
	}
}
