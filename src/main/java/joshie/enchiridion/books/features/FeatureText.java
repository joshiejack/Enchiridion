package joshie.enchiridion.books.features;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.lib.editables.ITextEditable;
import joshie.lib.editables.TextEditor;
import joshie.lib.helpers.ClientHelper;

public class FeatureText extends AbstractFeature implements ITextEditable {
	private transient boolean oneClick = false;
    private transient boolean readTemp = false;
    private transient double cachedWidth = 0;
	public transient int wrap = 0;
	public String text = ""; 
	public float size = 1F;
	
	public FeatureText(){}
	public FeatureText(String text) {
		this.text = text;
	}
	
	@Override
	public String getName() {
		return text;
	}
	
	@Override
	public void update(IFeatureProvider position) {
		cachedWidth = position.getWidth();
		wrap = Math.max(50, (int) (cachedWidth / size) + 4);
	}
	
    @Override
    public void draw(int xPos, int yPos, double width, double height, boolean isMouseHovering) {
    	EnchiridionAPI.draw.drawSplitScaledString(TextEditor.INSTANCE.getText(this), xPos, yPos, wrap, 0x555555, size);
    }
    
	private transient boolean firstClick = false;
    
    @Override
    public boolean getAndSetEditMode() {
    	if (ClientHelper.isShiftPressed()) {
			readTemp = false;
	    	try {
	    		readTemp = true;
	    		
	    		File file = new File("enchiridion.temp.txt");
	    		if (!file.exists()) {
	    			file.createNewFile();
	    		}
	    		
	    		Files.write(Paths.get("enchiridion.temp.txt"), text.getBytes());
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	return false;
		} else {
	    	if (!firstClick) {
	    		firstClick = true;
	    		return false;
	    	} else {
	    		firstClick = false;
	    		if (!ClientHelper.isShiftPressed())TextEditor.INSTANCE.setEditable(this);
	    		
	    		return true;
	    	}
		}
    }
        
    @Override
    public void onDeselected() {
    	if (readTemp) {
	    	byte[] encoded;
			try {
				encoded = Files.readAllBytes(Paths.get("enchiridion.temp.txt"));
		    	text = (new String(encoded, Charset.defaultCharset()));
		    	File file = new File("enchiridion.temp.txt");
		    	file.delete();
		    	readTemp = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		
    	TextEditor.INSTANCE.setEditable(null);
    }
    
    @Override
    public void keyTyped(char character, int key) {
    	if (ClientHelper.isShiftPressed()) {
            if (key == 78) {
                size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            } else if (key == 74) {
                size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            }
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
