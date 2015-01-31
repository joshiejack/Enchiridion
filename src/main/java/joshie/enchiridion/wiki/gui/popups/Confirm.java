package joshie.enchiridion.wiki.gui.popups;
import static joshie.enchiridion.ETranslate.translate;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledCentredText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledSplitText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.verticalGradient;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.ETranslate;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiExtension;

public abstract class Confirm extends GuiExtension implements IPopupIDoItellYou {	
    protected final String descriptor;
	public Confirm(String descriptor) {
		setVisibility(false);
		this.descriptor = descriptor;
	}
	
	@Override
	public void draw() {
		start();
		resetZ();
		
		drawRect(450, 150, 850, 310, 0xEE1B2C43);
		drawRect(450, 310, 850, 313, 0xFFC2C29C);
		drawRect(450, 147, 850, 150, 0xFFC2C29C);
		drawRect(447, 147, 450, 313, 0xFFC2C29C);
		drawRect(850, 147, 853, 313, 0xFFC2C29C);
		verticalGradient(450, 150, 850, 165, 0xFF09111E, 0xFF1B2C43);
        verticalGradient(450, 165, 850, 180, 0xFF1B2C43, 0xFF09111E);
        drawRect(450, 180, 850, 183, 0xFFC2C29C);
		
		drawScaledCentredText(1.75F, "[b]" + getTitle() + "[/b]", 630, 160, 0xFFFFFF);
		drawScaledSplitText(2F, "" + getDescription() + "", 463, 200, 0xFFFFFF, 189);
		
		fixColors();
		//Highlighting the buttons
		int xYes = 0, xNo = 0;
		if (getIntFromMouse(500, 624, 260, 299, 0, 1) == 1) {
            xYes = 130;
        }
		
		if (getIntFromMouse(660, 784, 260, 299, 0, 1) == 1) {
            xNo = 130;
        }
		
		
		drawScaledTexture(texture, 500, 260, xYes, 104, 124, 39, 1F);
		drawScaledTexture(texture, 660, 260, xNo, 104, 124, 39, 1F);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("yes") + "[/b]", 560, 272, 0xFFFFFF);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("no") + "[/b]", 720, 272, 0xFFFFFF);
        end();
	}
	
    @Override
    public void clicked(int button) {
        if (getIntFromMouse(500, 624, 260, 299, 0, 1) == 1) {
            confirm();
            cancel();
        }
        
        if (getIntFromMouse(650, 774, 260, 299, 0, 1) == 1) {
            cancel();
        }
    }
    
	public String getTitle() {
	    return translate("confirm." + descriptor + ".title");
	}
	
	public String getDescription() {
	    return translate("confirm." + descriptor + ".description");
	}
	
	public void cancel() {
		WikiHelper.setVisibility(getClass(), false);
	}
	
	public abstract void confirm();
}
