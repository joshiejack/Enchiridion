package joshie.enchiridion.wiki.gui.buttons;

import joshie.enchiridion.ETranslate;
import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.elements.ElementText;
import joshie.enchiridion.wiki.gui.GuiMain;

public class ButtonWikiTextMode extends ButtonWikiTextEdit {
    public ButtonWikiTextMode(GuiMain wiki, int id, int x, int y, String name) {
        super(wiki, id, x, y, "text." + name);
        this.width *= 1.5;
        this.xPosition -= 25;
        this.displayString = ElementText.showBBCode? ETranslate.translate("text.mode.bb.on"): ETranslate.translate("text.mode.bb.off");
    }

    @Override
    public void affect(Element component) {
        ElementText.showBBCode = !ElementText.showBBCode;
        this.displayString = ElementText.showBBCode? ETranslate.translate("text.mode.bb.on"): ETranslate.translate("text.mode.bb.off");
    }
}
