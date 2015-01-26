package joshie.enchiridion.wiki;

import java.util.ArrayList;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.helpers.OpenGLHelper;
import joshie.enchiridion.wiki.data.DataPage;
import joshie.enchiridion.wiki.data.WikiData;
import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.gui.GuiLayers;
import joshie.enchiridion.wiki.gui.GuiMenu;

import org.lwjgl.opengl.GL11;

import static joshie.enchiridion.helpers.OpenGLHelper.*;

public class WikiPage extends WikiPart {    
    public WikiPage(String key, String lang, DataPage contents) {
        super(key);
        WikiData.instance().addData(key, contents);
    }

    public WikiPage(String key) {
        this(key, ClientHelper.getLang(), new DataPage());
    }

    private WikiCategory category;
    public WikiPage setCategory(WikiCategory category) {
        this.category = category;
        return this;
    }

    public WikiCategory getCategory() {
        return category;
    }

    @Override
    public String getPath() {
        WikiMod mod = category.getTab().getMod();
        WikiTab tab = category.getTab();
        WikiCategory cat = category;
        WikiPage page = this;
        String lang = ClientHelper.getLang();
                
        String dir = getData().getSaveDirectory();
        if(dir.equals("")) {        	
        	return Enchiridion.root + "\\wiki\\" + mod.getKey() + "\\" + tab.getKey() + "\\" + cat.getKey() + "\\" + page.getKey() + "\\" + lang + ".json";
        } else {
        	String root = Enchiridion.root.getParentFile().getParentFile().getParentFile().toString();
        	return root + "\\src\\main\\resources\\assets\\" + dir + "\\wiki\\" + mod.getKey() + "\\" + tab.getKey() + "\\" + cat.getKey() + "\\" + page.getKey() + "\\" + lang + ".json";
        }
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        category.markDirty();
    }

    @Override
    public String getUnlocalized() {
        return category.getUnlocalized() + "." + getKey();
    }

    private Element selected;
    private int scroll;
    private boolean isEditMode;

    public Element getSelected() {
        return selected;
    }

    public void add(Element component) {
    	if(isEditMode()) {
	        getData().add(component);
	        this.markDirty();
    	}
    }

    public void remove(Element component) {
    	if(isEditMode()) {
	        getData().remove(component);
	        this.markDirty();
    	}
    }
    
    public boolean shouldSave() {
        return isEditMode;
    }

    public boolean isEditMode() {
        return shouldSave() && getData().canEdit();
    }

    public void setEditMode(boolean isEditMode) {
        if (!isEditMode) {
            if (selected != null) {
                selected.deselect();
            }

            this.selected = null;
        }

        this.isEditMode = isEditMode;
        WikiHelper.clearEditGUIs();
        GuiLayers.setActive(false);
        GuiMenu.setEditing();
    }

    @Override
    public DataPage getData() {
        return WikiData.instance().getPage(getUnlocalized() + "." + ClientHelper.getLang());
    }

    public void display() {       
        //Render a null stack, to fix stuff
        WikiHelper.renderStack(null, 0, 0);
        ArrayList<Element> elements = getData().getComponents();
        for (int i = (elements.size() - 1); i >= 0; i--) {
            start();
            resetZ();
            (elements.get(i)).setWiki(WikiHelper.gui).display((int) getData().getScroll(), isEditMode);
            end();
        }
    }

    public void keyTyped(char character, int key) {
        if (isEditMode()) {
            if (selected != null) {
                selected.keyTyped(character, key);
                if (ClientHelper.isShiftPressed() && key == 211) {
                    selected.onDeselected();
                    remove(selected);
                    selected = null;
                }
            }
        }

        switch (key) {
            case 200:
                scroll(25);
                break; //Up Key
            case 208:
                scroll(-25);
                break; //Down Key
            case 201:
                scroll(500);
                break; // Page Up
            case 209:
                scroll(-500);
                break; //Page Down
        }
    }

    public void updateY() {

    }

    //Called whenever a button is clicked
    public void clickButton(int x, int y, int button) {
        if (isEditMode()) {
            //If we currently have nothing selected
            if (selected == null) {
                //Loop through all the components, to 'select one'
                for (Element component : getData().getComponents()) {
                    //If the click returns true, then we will set the currently selected item to it
                    if (component.clickButton(x, y, button)) {
                        selected = component;
                        break;
                    }
                }
            } else {
                //If the second attempt at selection fails, try for a new selection
                if (!selected.clickButton(x, y, button)) {
                    selected = null;
                    //Loop through all the components, to 'select one'
                    for (Element component : getData().getComponents()) {
                        //If the click returns true, then we will set the currently selected item to it
                        if (component.clickButton(x, y, button)) {
                            selected = component;
                            break;
                        }
                    }
                }
            }
        }
    }

    //Called whenever a button is released
    public void releaseButton(int x, int y, int button) {
        if (isEditMode()) {
            if (selected != null) {
                //If the component has been deselected return true
                if (selected.releaseButton(x, y, button, true)) {
                    selected = null;
                }
            }
        } else {
            //If not edit mode, loop through everything and call release button on them
            for (Element component : getData().getComponents()) {
                component.releaseButton(x, y, button, false);
            }
        }
    }

    /** Causes stuff to follow the cursor **/
    public void follow(int x, int y) {
        if (isEditMode()) {
            if (selected != null) {
                selected.follow(x, y);
            }
        }
    }

    public void scroll(int amount) {
        getData().scroll(isEditMode(), amount);
    }

    public void switchPriority() {
        getData().switchPriority();
        this.markDirty();
    }
}
