package joshie.enchiridion.api.book;

public interface IBookHelper {
    /** GETTERS **/
    /** Whether or not this book is in edit mode **/
    public boolean isEditMode();

    /** Returns the current book being displayed **/
    public IBook getBook();

    /** Returns the current page being displayed **/
    public IPage getPage();

    /** Returns the current feature that is selected **/
    public IFeatureProvider getSelected();

    /** Whether this feature is selected in a group **/
    public boolean isGroupSelected(IFeatureProvider provider);

    /** SETTERS **/
    /** Set the current book **/
    public IBookHelper setBook(IBook book, boolean isEditing);

    /** Set the current page **/
    public void setPage(IPage page);

    /** Set the currently selected feature **/
    public void setSelected(IFeatureProvider provider);

    /** Jumps to the page number if it exists, returns true if we jumped **/
    public boolean jumpToPageIfExists(int number);

    /** Creates the page if it doesn't exist
     *  @number the page number
     *  @return returns null if the page existed,
     *  returns the page if it was created*/
    public IPage getPageIfNotExists(int number);
}
