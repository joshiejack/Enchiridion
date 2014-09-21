package joshie.enchiridion.wiki.mode.edit;

import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.elements.ElementText;
import joshie.enchiridion.wiki.gui.GuiMain;
import joshie.lib.util.Text;

public class ButtonWikiTextEffects extends ButtonWikiTextEdit {
    public final String effect;
    public ButtonWikiTextEffects(GuiMain wiki, int id, int x, int y, String effect, String name) {
        super(wiki, id, x, y, "text." + name);
        this.effect = effect;
    }

    @Override
    public void affect(Element component) {
        if(component instanceof ElementText) {
            ((ElementText)component).add(effect);
        }
    }
}
