 package joshie.enchiridion.data.book;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Page implements IPage {
    public List<IFeatureProvider> features = new ArrayList();
	public int pageNumber;
	public boolean isScrollable;
	public transient int scrollAmount;
	public transient int maximumScroll;
	public transient IBook book;
	
	public Page() {}
	public Page(int number) {
		this.pageNumber = number;
	}

	@Override
	public IBook getBook() {
		return book;
	}

	@Override
	public IPage setBook(IBook book) {
		this.book = book;
		return this;
	}

    @Override
    public boolean isScrollingEnabled() {
        return isScrollable;
    }

	@Override
	public void toggleScroll() {
		this.isScrollable = !isScrollable;
	}

    private void validateScrollPosition() {
        if (this.scrollAmount < 0) {
            this.scrollAmount = 0;
        }

        if (this.scrollAmount > maximumScroll) {
            this.scrollAmount = maximumScroll;
        }

        for (IFeatureProvider provider: getFeatures()) {
            provider.update(this);
        }
    }

    @Override
    public void setScrollPosition(int position) {
        if (isScrollable) {
            this.scrollAmount = position;
            validateScrollPosition();
        }
    }

	@Override
	public void scroll(boolean down, int amount) {
		if (isScrollable) {
			if (down) {
				this.scrollAmount += amount;
			} else this.scrollAmount -= amount;

            validateScrollPosition();
		}
	}

	@Override
	public int getScroll() {
		return isScrollable ? scrollAmount : 0;
	}

	@Override
	public void addFeature(IFeature feature, int x, int y, double width, double height, boolean isLocked, boolean isHidden) {
		FeatureProvider provider = new FeatureProvider(feature, x, y, width, height);
		provider.isLocked = isLocked;
		provider.isHidden = isHidden;
		provider.update(this);
		provider.layerIndex = features.size();
		features.add(provider);
	}
	
	@Override
	public void removeFeature(IFeatureProvider selected) {
		features.remove(selected);
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
    public int getScrollbarMax(int screenTop) {
        updateMaximumScroll(screenTop);
        return maximumScroll;
    }

    @Override
    public void updateMaximumScroll(int screenTop) {
        int maxY = 0;
        for (IFeatureProvider provider: features) {
            if (provider.getTop() + provider.getHeight() > maxY){
                maxY = (int) (provider.getTop() + provider.getHeight());
            }
        }

        maximumScroll = maxY - screenTop;
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
