package joshie.enchiridion.wiki;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.wiki.elements.Element;
import joshie.lib.helpers.ClientHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class WikiPage {
    private HashMap<String, WikiContents> translations = new HashMap();
    private final String key;

    public WikiPage(String key, String lang, WikiContents contents) {
        this.key = key;
        setContents(lang, contents);
    }

    public void setContents(String lang, WikiContents contents) {
        translations.put(lang, contents.refreshY());
    }

    public WikiPage(String key) {
        this(key, ClientHelper.getLang(), new WikiContents());
    }

    private WikiCategory category;

    public WikiPage setCategory(WikiCategory category) {
        this.category = category;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getPath() {
        WikiMod mod = category.getTab().getMod();
        WikiTab tab = category.getTab();
        WikiCategory cat = category;
        WikiPage page = this;
        String lang = ClientHelper.getLang();

        return Enchiridion.root + "\\wiki\\" + mod.getKey() + "\\" + tab.getKey() + "\\" + cat.getKey() + "\\" + page.getKey() + "\\" + lang + ".json";
    }

    public String getUnlocalized() {
        return category.getUnlocalized() + "." + getKey();
    }

    public String getTitle() {
        return WikiTitles.instance().translateToLocal(getUnlocalized());
    }

    public static final String s = File.separator;
    private Element selected;
    private int scroll;
    private boolean isEditMode;
    private WikiCategory cat;

    public Element getSelected() {
        return selected;
    }

    public void add(Element component) {
        getContent().add(component);
    }

    public void remove(Element component) {
        getContent().remove(component);
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean isEditMode) {
        if (!isEditMode) {
            if (selected != null) {
                selected.deselect();
            }

            this.selected = null;
        }

        this.isEditMode = isEditMode;
    }

    public WikiContents getContent() {
        String lang = ClientHelper.getLang();
        WikiContents content = translations.get(lang);
        if (content == null) content = translations.get("en_US");
        if (content == null) {
            content = new WikiContents();
            translations.put(lang, content);
        }

        return content;
    }

    public void display() {        
        ArrayList<Element> elements = getContent().getComponents();
        for (int i = 0; i < elements.size(); i++) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0F, 0F, ((elements.size() - i) * 120));
            (elements.get(i)).setWiki(WikiHelper.gui).display((int) getContent().getScroll(), isEditMode);
            GL11.glPopMatrix();
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
                for (Element component : getContent().getComponents()) {
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
                    for (Element component : getContent().getComponents()) {
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
            for (Element component : getContent().getComponents()) {
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
        getContent().scroll(isEditMode(), amount);
    }
}
