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
}
