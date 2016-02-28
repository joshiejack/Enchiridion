package joshie.enchiridion.api;

import java.util.ArrayList;

/** Do not create your own version of IPage, 
 * this is here for helper reasons;
 * @author joshie
 *
 */
public interface IPage {
	/** Returns the name of this page **/
	public String getPageName();

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
}
