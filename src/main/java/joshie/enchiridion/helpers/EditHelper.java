package joshie.enchiridion.helpers;

import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.edit.IEditHelper;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorGeneric;
import joshie.enchiridion.gui.book.buttons.actions.ActionJumpPage;
import joshie.enchiridion.gui.book.features.FeatureButton;

public class EditHelper implements IEditHelper {
    @Override
    public void setSimpleEditorFeature(ISimpleEditorFieldProvider feature) {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorGeneric.INSTANCE.setFeature(feature));
    }

    @Override
    public IButtonActionProvider getJumpPageButton(int page) {
        return new FeatureButton(new ActionJumpPage(page));
    }
}