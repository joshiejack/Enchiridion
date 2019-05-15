package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.features.FeaturePreviewWindow;

public class ButtonInsertPreviewWindow extends ButtonAbstract {
    public ButtonInsertPreviewWindow() {
        super("preview");
    }

    @Override
    public void performAction() {
        IPage current = EnchiridionAPI.book.getPage();
        FeaturePreviewWindow feature = new FeaturePreviewWindow(0);
        current.addFeature(feature, 0, current.getScroll(), 100D, 100D, false, false, false);
        GuiSimpleEditor.INSTANCE.setEditor(null);
    }
}