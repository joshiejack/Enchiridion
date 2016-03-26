package joshie.enchiridion.api.edit;

import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;

public interface IEditHelper {
    /** Put this feature in the simple editor **/
    public void setSimpleEditorFeature(ISimpleEditorFieldProvider feature);

    /** Returns a jump to page button **/
    public IFeature getJumpPageButton(String unhover, String hover, int page);
}
