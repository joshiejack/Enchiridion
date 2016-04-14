package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.gui.book.GuiSimpleEditorGeneric.WrappedEditable;
import joshie.enchiridion.gui.book.features.FeatureButton;
import joshie.enchiridion.helpers.FileCopier;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.lib.EInfo;
import joshie.enchiridion.util.ELocation;
import joshie.enchiridion.util.PenguinFont;
import joshie.enchiridion.util.TextEditor;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiSimpleEditorButton extends GuiSimpleEditorAbstract {
	public static final GuiSimpleEditorButton INSTANCE = new GuiSimpleEditorButton();
	private static final ResourceLocation arrow_right_on = new ELocation("arrow_right_on");
	private static final ResourceLocation arrow_right_off = new ELocation("arrow_right_off");
	private static final ResourceLocation arrow_left_on = new ELocation("arrow_left_on");
	private static final ResourceLocation arrow_left_off = new ELocation("arrow_left_off");
	
	private final ArrayList<IButtonAction> actions = new ArrayList();
	private ArrayList<IButtonAction> sorted = new ArrayList();
    private HashMap<String, WrappedEditable> fieldCache = new HashMap();
	private FeatureButton button = null;
    private String[] fieldNameCache;
	
	
	protected GuiSimpleEditorButton() {}

	public void registerAction(IButtonAction action) {
		actions.add(action);
	}

	public IBookEditorOverlay setButton(FeatureButton button) {
		this.button = button;
		this.fieldCache = new HashMap();
        this.fieldNameCache = null;
		return this;
	}
	
	private boolean isOverAction(int xPos, int yPos, int mouseX, int mouseY) {
		if (mouseX >= EConfig.editorXPos + xPos && mouseX <= EConfig.editorXPos + xPos + 9) {
			if (mouseY >= EConfig.toolbarYPos + yPos + 7 && mouseY <= EConfig.toolbarYPos + yPos + 17) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isOverPosition(int x1, int y1, int x2, int y2, int mouseX, int mouseY) {
		if (mouseX >= EConfig.editorXPos + x1 && mouseX <= EConfig.editorXPos + x2) {
			if (mouseY >= EConfig.toolbarYPos + y1 && mouseY <= EConfig.toolbarYPos + y2) {
				return true;
			}
		}
		
		return false;
	}
    
	//CONVERT IN TO BUTTONS
	
	private static final int xPosStart = 4;
	
	private void drawBoxLabel(String name, int yPos) {
		drawBorderedRectangle(xPosStart - 2, yPos, 83, yPos + 10, 0xFFB0A483, 0xFF48453C);
		drawSplitScaledString("[b]" + name + "[/b]", xPosStart, yPos + 3, 0xFF48453C, 0.5F);
	}

    public boolean isTransient(String fieldName) {
        try {
            Field f = button.action.getClass().getField(fieldName);
            return (Modifier.isTransient(f.getModifiers()));
        } catch (Exception e) {}

        return false;
    }

    public String[] getFieldNames() {
        if (fieldNameCache != null) return fieldNameCache;
        fieldNameCache = new String[button.action.getClass().getFields().length];
        int i = 0;
        for (Field field : button.action.getClass().getFields()) {
            fieldNameCache[i] = field.getName();
            i++;
        }

        return fieldNameCache;
    }
	
    @Override
    public void draw(int mouseX, int mouseY) {
        if (button == null || button.action == null) return;; //NO WORK!!!
    	drawBoxLabel(Enchiridion.translate("select.action"), 9);
    	int xPos = xPosStart;
    	int yPos = 13;
    	for (IButtonAction action: sorted) {
    		drawImage(action.getResource(false), xPos + 1, yPos + 8, xPos + 9, yPos + 16);
    		if (action.getName().equals(button.action.getName())) {
    			drawBorderedRectangle(xPos, yPos + 7, xPos + 10, yPos + 17, 0x00000000, 0xFF48453C);
    		}
    		
    		xPos += 10;
    		if (xPos > 70) {
    			xPos = xPosStart;
    			yPos += 10;
    		}
    	}
    	
    	//Draw the unhovered button selector
    	int colorI = 0x00000000;
    	int colorB = 0xFFB0A483;
    	drawBoxLabel(Enchiridion.translate("select.unhover"), yPos + 20);
    	drawImage(arrow_left_off, 4, yPos + 32, 22, yPos + 42);
    	drawImage(arrow_right_off, 24, yPos + 32, 42, yPos + 42);
    	if (arrow_left_off.equals(button.action.getResource(false))) drawBorderedRectangle(3, yPos + 31, 23, yPos + 43, 0x00000000, 0xFF48453C);
    	else if (arrow_right_off.equals(button.action.getResource(false))) drawBorderedRectangle(23, yPos + 31, 43, yPos + 43, 0x00000000, 0xFF48453C);
    	else {
    		//colorI = 0xFF312921;
    		colorB = 0xFF191511;
    		drawImage(button.action.getResource(false), 45, yPos + 31, 80, yPos + 43);
    	}
    		
    	drawBorderedRectangle(45, yPos + 31, 80, yPos + 43, colorI, colorB);
    	drawSplitScaledString("[b]" + Enchiridion.translate("select.custom") + "[/b]", 53, yPos + 35, 0xFFFFFFFF, 0.5F);
    	
    	//Draw the hovered button selector
    	yPos+= 25;
    	colorI = 0x00000000;
    	colorB = 0xFFB0A483;
    	drawBoxLabel(Enchiridion.translate("select.hover"), yPos + 20);
    	drawImage(arrow_left_on, 4, yPos + 32, 22, yPos + 42);
    	drawImage(arrow_right_on, 24, yPos + 32, 42, yPos + 42);
    	if (arrow_left_on.equals(button.action.getResource(true))) drawBorderedRectangle(3, yPos + 31, 23, yPos + 43, 0x00000000, 0xFF48453C);
    	else if(arrow_right_on.equals(button.action.getResource(true))) drawBorderedRectangle(23, yPos + 31, 43, yPos + 43, 0x00000000, 0xFF48453C);
    	else {
    		//colorI = 0xFF312921;
    		colorB = 0xFF191511;
    		drawImage(button.action.getResource(true), 45, yPos + 31, 80, yPos + 43);
    	}
    		
    	drawBorderedRectangle(45, yPos + 31, 80, yPos + 43, colorI, colorB);
    	drawSplitScaledString("[b]" + Enchiridion.translate("select.custom") + "[/b]", 53, yPos + 35, 0xFFFFFFFF, 0.5F);
    	
    	yPos+= 25;
    	//Draw the extra information for the actions
    	drawBoxLabel("Extra Fields", yPos + 20);
    	for (String f: getFieldNames()) {
            if (isTransient(f)) continue;
    		drawBorderedRectangle(2, yPos + 30, 83, yPos + 37, 0xFF312921, 0xFF191511);
    		String name = Enchiridion.translate("button.action.field." + f);
    		drawSplitScaledString("[b]" + name + "[/b]", 4, yPos + 32, 0xFFFFFFFF, 0.5F);

			WrappedEditable editable = null;
    		if (!fieldCache.containsKey(f)) {
    			editable = new WrappedEditable(button.action, f);
    			fieldCache.put(f, editable);
    		} else editable = fieldCache.get(f);
    		
    		String text = TextEditor.INSTANCE.getText(editable);
    		if (text == null) {
    		    TextEditor.INSTANCE.setText("");
    		    text = TextEditor.INSTANCE.getText(editable);
    		}
    		
    		int lines = getLineCount(text) - 1;
	    	drawSplitScaledString(text, 4, yPos + 39, 0xFF191511, 0.5F);
			yPos = yPos + 6 + lines;
    	}
    	
    	drawBorderedRectangle(2, yPos + 30, 83, yPos + 31, 0xFF312921, 0xFF191511);
    }
    
    public int getLineCount (String text) {
		text = PenguinFont.INSTANCE.replaceFormatting(text);
		while (text != null && text.endsWith("\n")) {
			text = text.substring(0, text.length() - 1);
		}

		return PenguinFont.INSTANCE.splitStringWidth(text, 155);
    }
    
    @Override
	public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
    	int xPos = xPosStart;
    	int yPos = 13;
    	for (IButtonAction action: sorted) {
    		if (isOverAction(xPos, yPos, mouseX, mouseY)) {
    			tooltip.add(action.getName());
    		}
    		
    		xPos += 10;
    		if (xPos > 70) {
    			xPos = xPosStart;
    			yPos += 10;
    		}
    	}
    }

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
        if (button == null || button.action == null) return false;; //NO WORK!!!

		int xPos = xPosStart;
		int yPos = 13;
    	for (IButtonAction action: sorted) {
    		if (isOverAction(xPos, yPos, mouseX, mouseY)) {
                IButtonAction old = button.action;
    			button.action = action.copy();
                button.action.setTooltip(old.getTooltip());
                button.action.setText(true, old.getText(true));
                button.action.setText(false, old.getText(false));
                button.action.setResourceLocation(true, old.getResource(true));
                button.action.setResourceLocation(false, old.getResource(false));
                button.action.onFieldsSet(""); //CREATE!
                setButton(button);
    			return true;
    		}
    		
    		xPos += 10;
    		if (xPos > 70) {
    			xPos = xPosStart;
    			yPos += 10;
    		}
    	}
    	
    	//Update the resource for arrows in the unhovered position
    	if (isOverPosition(4, yPos + 32, 22, yPos + 42, mouseX, mouseY)) {
    		button.action.setResourceLocation(false, arrow_left_off);
    		return true;
    	} else if (isOverPosition(24, yPos + 32, 42, yPos + 42, mouseX, mouseY)) {
    		button.action.setResourceLocation(false, arrow_right_off);
    		return true;
    	} else if (isOverPosition(45, yPos + 31, 80, yPos + 43, mouseX, mouseY)) {
            ResourceLocation resource = loadResource();
    		if (resource != null) {
				button.action.setResourceLocation(false, resource);
    		}
    		
    		return true;
    	}
    	
    	//Update the resources for arrows in the hovered position
    	yPos += 25;
    	if (isOverPosition(4, yPos + 32, 22, yPos + 42, mouseX, mouseY)) {
    		button.action.setResourceLocation(true, arrow_left_on);
    		return true;
    	} else if (isOverPosition(24, yPos + 32, 42, yPos + 42, mouseX, mouseY)) {
    		button.action.setResourceLocation(true, arrow_right_on);
    		return true;
    	} else if (isOverPosition(45, yPos + 31, 80, yPos + 43, mouseX, mouseY)) {
            ResourceLocation resource = loadResource();
    		if (resource != null) {
                button.action.setResourceLocation(true, resource);
    		}
    		
    		return true;
    	}
    	
    	yPos+= 25;
    	//Draw the extra information for the actions
    	drawBoxLabel("Extra Fields", yPos + 20);
    	for (String f: getFieldNames()) {
            if (isTransient(f)) continue;
			WrappedEditable editable = null;
    		if (!fieldCache.containsKey(f)) {
    			editable = new WrappedEditable(button.action, f);
    			fieldCache.put(f, editable);
    		} else editable = fieldCache.get(f);
    		String text = TextEditor.INSTANCE.getText(editable);
    		int lines = getLineCount(text) - 1;
    		
    		
    		if (isOverPosition(2, yPos + 37, 83, yPos + 44 + lines, mouseX, mouseY)) {
				editable.click();
        		return true;
    		}


			yPos = yPos + 6 + lines;
    	}
    	
    	return false;
	}
	
	private ResourceLocation loadResource() {
		File file = FileCopier.copyFileFromUser(FileHelper.getImageSaveDirectory());
		if (file != null) {
			try {
				BufferedImage buffered = ImageIO.read(file);
                ResourceLocation location = new ResourceLocation(EInfo.MODID + ":images/" + EnchiridionAPI.book.getBook().getSaveName() + "/" + file.getName());
				EnchiridionAPI.book.getSelected().setWidth(buffered.getWidth());
				EnchiridionAPI.book.getSelected().setHeight(buffered.getHeight());
				return location;
			} catch (Exception e) {}
		}
		
		return null;
	}
	
	@Override
	public void updateSearch(String search) {
		sorted = new ArrayList();
		if (search == null || search.equals("")) {
            sorted.addAll(actions);
		} else {
			for (IButtonAction action: actions) {
				if (action.getName().toLowerCase().contains(search.toLowerCase())) {
					sorted.add(action);
				}
			}
		}
    }
}
