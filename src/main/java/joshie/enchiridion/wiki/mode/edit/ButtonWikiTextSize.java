package joshie.enchiridion.wiki.mode.edit;

import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.elements.ElementText;
import joshie.enchiridion.wiki.gui.GuiMain;

public class ButtonWikiTextSize extends ButtonWikiTextEdit {
    public final float size;
    public ButtonWikiTextSize(GuiMain wiki, int id, int x, int y, float size, String name) {
        super(wiki, id, x, y, "text." + name);
        this.size = size;
        this.width *= 1.5;
        this.xPosition -= 25;
    }

    @Override
    public void affect(Element component) {
        if(component instanceof ElementText) {
            ((ElementText)component).size = Math.min(15F, Math.max(1F, ((ElementText)component).size + size));
        }
    }
}
