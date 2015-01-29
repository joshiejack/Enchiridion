package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.fixLighting;
public class GuiLighting extends GuiExtension {
    @Override
    public void draw() {
        fixLighting();
    }
}
