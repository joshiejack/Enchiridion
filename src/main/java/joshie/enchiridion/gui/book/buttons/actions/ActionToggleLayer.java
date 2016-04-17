package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.data.book.BookEvents;
import joshie.enchiridion.util.ELocation;

import java.util.regex.Pattern;

public class ActionToggleLayer extends AbstractAction {
    public String layer = "1";
    public boolean comma = true;
    public boolean regex = false;

    public ActionToggleLayer() {
        super("toggle");
        resource = new ELocation("layer_dftl");
    }

    @Override
    public IButtonAction copy() {
        ActionToggleLayer action = new ActionToggleLayer();
        action.comma = comma;
        action.regex = regex;
        action.layer = layer;
        copyAbstract(action);
        return action;
    }

    @Override
    public IButtonAction create() {
        ActionToggleLayer action = new ActionToggleLayer();
        action.comma = true;
        action.regex = false;
        action.layer = "1";
        return action;
    }

    @Override
    public void performAction() {
        try {
            if (regex) {
                Pattern p = Pattern.compile(layer);
                if (p != null) {
                    BookEvents.invert(EnchiridionAPI.book.getBook(), EnchiridionAPI.book.getPage(), p);
                }
            } else if (comma) {
                String[] ss = layer.replace(" ", "").split(",");
                for (String s: ss) {
                    BookEvents.invert(EnchiridionAPI.book.getBook(), EnchiridionAPI.book.getPage(), Pattern.compile(s));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
     }
}
