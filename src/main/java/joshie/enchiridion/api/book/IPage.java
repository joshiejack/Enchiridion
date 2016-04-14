package joshie.enchiridion.api.book;

import java.util.ArrayList;

/** Do not create your own version of IPage, 
 * this is here for helper reasons;
 * @author joshie
 *
 */
public interface IPage {
	/** Returns the number of this page **/
	public int getPageNumber();
	
	/** Returns all the features,
	 *  do not add using this list **/
	public ArrayList<IFeatureProvider> getFeatures();

	/**Sets the page number **/
	public void setPageNumber(int number);

	/** Add a new feature to this page with the default stats **/
	public void addFeature(IFeature feature, int x, int y, double width, double height, boolean isLocked, boolean isHidden);

	/** Removes a feature from the page **/
	public void removeFeature(IFeatureProvider selected);

	/** Call this to resort all the layers on the page by their index value **/
	public void sort();

	/** Removes all the features **/ 
    public void clear();

	/** Returns the book this page is in **/
	public IBook getBook();

	/** Set the book this page is attached to **/
	public IPage setBook(IBook book);

	/** Sets the page to be scrollable type or not **/
	public void toggleScroll();

	/** Called to scroll this page if applicable **/
	public void scroll(boolean down, int amount);

	/** How far down this page is scrolled **/
	public int getScroll();

	/** Whether scrolling is enabled for this page **/
	public boolean isScrollingEnabled();

	/** Adjust the maximum scroll for this page **/
	public void updateMaximumScroll(int screenTop);

	/** Gets the maximum height of a scrollbar **/
	public int getScrollbarMax(int screenTop);

    /** Force a scroll position **/
    public void setScrollPosition(int scrollPosition);
}
