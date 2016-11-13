package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorTemplateSave;

public class ButtonSaveTemplate extends ButtonAbstract {
    public ButtonSaveTemplate() {
        super("template.save");
    }

    @Override
    public boolean isLeftAligned() {
        return false;
    }

    @Override
    public void performAction() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorTemplateSave.INSTANCE);
    }
}