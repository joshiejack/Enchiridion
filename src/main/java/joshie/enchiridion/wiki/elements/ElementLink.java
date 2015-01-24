package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.mode.edit.PageEditLink;

import com.google.gson.annotations.Expose;

public class ElementLink extends Element {
    @Expose
    private String mod;
    @Expose
    private String tab;
    @Expose
    private String cat;
    @Expose
    private String page;

    @Override
    public ElementLink setToDefault() {
        this.width = 100;
        this.height = 20;
        this.mod = "Enchiridion 2";
        this.tab = "Enchiridion 2";
        this.cat = "Enchiridion 2";
        this.page = "About";
        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        if (isEditMode) {
        	WikiHelper.drawRect(BASE_X + left - 2, BASE_Y + top, BASE_X + left, BASE_Y + bottom, 0xFFFFFF00);
        	WikiHelper.drawRect(BASE_X + right, BASE_Y + top, BASE_X + right + 2, BASE_Y + bottom, 0xFFFFFF00);
        	WikiHelper.drawRect(BASE_X + left, BASE_Y + top - 2, BASE_X + right, BASE_Y + top, 0xFFFFFF00);
        	WikiHelper.drawRect(BASE_X + left, BASE_Y + bottom, BASE_X + right, BASE_Y + bottom + 2, 0xFFFFFF00);
        }
    }

    //Edit mode return doesn't matter
    @Override
    public boolean releaseButton(int x, int y, int button, boolean isEditMode) {
        if (!isEditMode && button == 0) {
            if (isMouseOver(x, y)) {
                if (mod != null && tab != null && cat != null && page != null) {
                    wiki.setPage(mod, tab, cat, page);
                }
            }
        }

        return super.releaseButton(x, y, button, isEditMode);
    }

    @Override
    public void addEditButtons(List list) {
        return;
    }

    @Override
    public void onSelected(int x, int y, int button) {
        ((PageEditLink)(WikiHelper.getInstance(PageEditLink.class))).setEditing(this);
    }
    
    public String getMod() {
        return mod;
    }
    
    public String getTab() {
        return tab;
    }
    
    public String getCat() {
        return cat;
    }
    
    public String getPage() {
        return page;
    }
    
    public void set(String mod, String tab, String cat, String page) {
        this.mod = mod;
        this.tab = tab;
        this.cat = cat;
        this.page = page;
        this.markDirty();
    }
}
