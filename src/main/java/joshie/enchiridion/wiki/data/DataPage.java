package joshie.enchiridion.wiki.data;

import java.util.ArrayList;

import joshie.enchiridion.wiki.elements.Element;

import com.google.gson.annotations.Expose;

public class DataPage extends Data {
    @Expose
    private ArrayList<Element> components = new ArrayList();
    @Expose
    private int scrollMax = -1;
    private int maxY = 500;
    private int scroll;
    @Expose
    private boolean isPriority;
    
    public DataPage(){}
    public DataPage(String string) {
        super(string);
    }
    
    public DataPage refreshY() {
        maxY = 0;
        for(Element component: components) {
            int y = (int) ((component.y + component.height));
            if (y > maxY) {
                maxY = y;
            }
        }
        
        return this;
    }
    
    public void scroll(boolean isEditMode, int amount) {        
        scroll = Math.min((isEditMode ? Short.MAX_VALUE: scrollMax >= 0? scrollMax: maxY), Math.max(0, scroll - amount));
    }
    
    /* Add an element to the components list */
    public void add(Element element) {
        components.add(element);
    }
    
    /* Remove an element from the components list */
    public void remove(Element element) {
        components.remove(element);
    }
    
    public int getKey(Element element) {
        int key = 0;
        for (key = 0; key < components.size(); key++) {
            if(components.get(key).equals(element)) {
                break;
            }
        }
        
        return key;
    }
    
    public void moveUp(Element element) {        
        setPosition(element, Math.max(0, getKey(element) - 1));
    }
    
    public void moveDown(Element element) {
        setPosition(element, Math.min(components.size() - 1, getKey(element) + 1));
    }
    
    public void setPosition(Element element, int position) {
        components.remove(element);
        components.add(position, element);
    }

    public ArrayList<Element> getComponents() {
        return components;
    }
    
    public int getScroll() {
        return scroll;
    }
    
    public boolean isPrioritised() {
        return isPriority;
    }
    
    public void switchPriority() {
        isPriority = !isPriority;
    }
}
