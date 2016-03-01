 package joshie.enchiridion.books;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import joshie.enchiridion.api.IFeature;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.Feature;

public class Page implements IPage {
    public List<IFeatureProvider> features = new ArrayList();
    public String pageName;
	public int pageNumber;
	
	public Page() {}
	public Page(int number) {
		this.pageName = "" + number;
		this.pageNumber = number;
	}
	
	@Override
	public void addFeature(IFeature feature, int x, int y, double width, double height, boolean isLocked, boolean isHidden) {
		Feature provider = new Feature(feature, x, y, width, height);
		provider.isLocked = isLocked;
		provider.isHidden = isHidden;
		provider.feature.update(provider);
		provider.layerIndex = features.size();
		//feature.getAndSetEditMode();
		features.add(provider);
	}
	
	@Override
	public void removeFeature(IFeatureProvider selected) {
		features.remove(selected);
	}
	
	@Override
	public String getPageName() {
		return pageName;
	}
	
	@Override
	public int getPageNumber() {
		return pageNumber;
	}
	
	@Override
	public ArrayList<IFeatureProvider> getFeatures() {
		return new ArrayList(features);
	}
	
	@Override
	public void setPageNumber(int number) {
		pageNumber = number;
	}
	
	private static final SortIndex sorter = new SortIndex();
	private static class SortIndex implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
        	IFeatureProvider provider1 = (IFeatureProvider) o1;
        	IFeatureProvider provider2 = (IFeatureProvider) o2;
        	if (provider1.getLayerIndex() == provider2.getLayerIndex()) {
        		return provider1.getTimeChanged() >= provider2.getTimeChanged() ? 1: -1;
        	} else return provider1.getLayerIndex() > provider2.getLayerIndex() ? 1 : -1;
        }
    }
	
	@Override
	public void sort() {
		Collections.sort(features, sorter); //Sort everything out in to order
		
		int i = 0;
		for (IFeatureProvider provider: features) { //Fix all the id numbers
			provider.setLayerIndex(i);
			i++;
		}
	}
	
	@Override
	public void clear() {
	    features = new ArrayList();
	}
}
