package joshie.enchiridion.api.book;

import java.util.ArrayList;

/** Do not create your own version of IPage, 
 * this is here for helper reasons;
 * @author joshie
 */
public interface IPage {
    /** @return the number of this page **/
    int getPageNumber();

    /** Returns all the features,
     *  do not add using this list **/
    ArrayList<IFeatureProvider> getFeatures();

    /** Sets the page number **/
    void setPageNumber(int number);

    /** Add a new feature to this page with the default stats **/
    void addFeature(IFeature feature, int x, int y, double width, double height, boolean isLocked, boolean isHidden, boolean isFromTemplate);

    /** Removes a feature from the page **/
    void removeFeature(IFeatureProvider selected);

    /** Call this to resort all the layers on the page by their index value **/
    void sort();

    /** Removes all the features **/
    void clear();

    /** Returns the book this page is in **/
    IBook getBook();

    /** Set the book this page is attached to **/
    IPage setBook(IBook book);

    /** Sets the page to be scrollable type or not **/
    void toggleScroll();

    /** Called to scroll this page if applicable **/
    void scroll(boolean down, int amount);

    /** How far down this page is scrolled **/
    int getScroll();

    /** Whether scrolling is enabled for this page **/
    boolean isScrollingEnabled();

    /** Adjust the maximum scroll for this page **/
    void updateMaximumScroll(int screenTop);

    /** Gets the maximum height of a scrollbar **/
    int getScrollbarMax(int screenTop);

    /** Force a scroll position **/
    void setScrollPosition(int scrollPosition);
}