package joshie.enchiridion.gui.book;

import java.util.List;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.lib.editables.ITextEditable;
import joshie.lib.editables.TextEditor;

public class GuiSimpleEditor extends AbstractGuiOverlay implements ITextEditable {
	public static final GuiSimpleEditor INSTANCE = new GuiSimpleEditor();
	private String text = "";
	private boolean clicked = true;
	private int held = 0;
	private int yStart = 0;
    private int layerPosition = 0;
    private IBookEditorOverlay editor = null;
    
    private GuiSimpleEditor() {}
    
    public void setEditor(IBookEditorOverlay editor) {
    	this.editor = editor;
    	this.text = "";
    	if (editor != null) {
    		editor.updateSearch(this.text);
    	}
    }
    
    @Override
    public void draw(int mouseX, int mouseY) {
    	if (editor != null) {
    		/** Draw the Background **/
    		EnchiridionAPI.draw.drawImage(sidebar, EConfig.editorXPos - 3, EConfig.toolbarYPos - 7, EConfig.editorXPos + 87, EConfig.timelineYPos + 13);
        	EnchiridionAPI.draw.drawBorderedRectangle(EConfig.editorXPos, EConfig.toolbarYPos + 7, EConfig.editorXPos + 85, EConfig.timelineYPos + 11, 0xFF312921, 0xFF191511);
        	EnchiridionAPI.draw.drawBorderedRectangle(EConfig.editorXPos + 2, EConfig.toolbarYPos + 9, EConfig.editorXPos + 83, EConfig.timelineYPos + 9, 0xFFE4D6AE, 0x5579725A);
        	EnchiridionAPI.draw.drawBorderedRectangle(EConfig.editorXPos, EConfig.toolbarYPos - 3, EConfig.editorXPos + 84, EConfig.toolbarYPos + 7, 0xFF312921, 0xFF191511);
        	EnchiridionAPI.draw.drawSplitScaledString(TextEditor.INSTANCE.getText(this), EConfig.editorXPos + 5, EConfig.toolbarYPos, 250, 0xFFFFFFFF, 0.5F);
            editor.draw(mouseX, mouseY);
        }
    }
    
    @Override
	public void keyTyped(char character, int key) {
    	 if (editor != null) {
    		 editor.updateSearch(getTextField());
    	 }
    }
    
    @Override
	public boolean mouseClicked(int mouseX, int mouseY) {
    	if (editor != null) {
	    	if (mouseX >= EConfig.editorXPos && mouseX <= EConfig.editorXPos + 84 && mouseY >= EConfig.toolbarYPos - 3 && mouseY <= EConfig.toolbarYPos + 7) {
	    		TextEditor.INSTANCE.setEditable(this);
	    		return true;
	    	} else {
	    		return editor.mouseClicked(mouseX, mouseY);
	    	}
    	}
    	
    	return false;
	}
    
    @Override
   	public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
    	if (editor != null) {
    		editor.addToolTip(tooltip, mouseX, mouseY);
    	}
    }

    @Override
	public void scroll(boolean down, int mouseX, int mouseY) {
    	if (editor != null) {
    		editor.scroll(down, mouseX, mouseY);
    	}
    }

	@Override
	public String getTextField() {
		return text;
	}

	@Override
	public void setTextField(String text) {
		this.text = text;
	}
}
