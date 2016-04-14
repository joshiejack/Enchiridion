package joshie.enchiridion.api.edit;

import joshie.enchiridion.api.book.IButtonActionProvider;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;

public interface IEditHelper {
    /** Put this feature in the simple editor **/
    public void setSimpleEditorFeature(ISimpleEditorFieldProvider feature);

    /** Returns a jump to page button **/
    public IButtonActionProvider getJumpPageButton(int page);
}
