package joshie.enchiridion.books.features;

import joshie.enchiridion.api.IFeatureProvider;

public class AbstractFeatureWithColor extends AbstractFeature {
	public String color;
	public transient int colorI;
	
	@Override
	public void update(IFeatureProvider position) {
		try {
            colorI = (int) Long.parseLong(color, 16);
        } catch (Exception e) {}
	}
}
