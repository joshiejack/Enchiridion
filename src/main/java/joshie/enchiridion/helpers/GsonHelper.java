package joshie.enchiridion.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IFeature;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.Book;
import joshie.enchiridion.books.features.Feature;
import joshie.enchiridion.json.BookAbstractAdapter;
import joshie.enchiridion.json.FeatureAbstractAdapter;
import joshie.enchiridion.json.IButtonActionAbstractAdapter;
import joshie.enchiridion.json.IFeatureAbstractAdapter;
import joshie.enchiridion.json.IFeatureProviderAbstractAdapter;
import joshie.enchiridion.json.IPageAbstractAdapter;

public class GsonHelper {
    public static Gson getModifiedGson() {
        if (GsonHelper.gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            builder.registerTypeAdapter(IFeature.class, new IFeatureAbstractAdapter());
            builder.registerTypeAdapter(Feature.class, new FeatureAbstractAdapter());
            builder.registerTypeAdapter(IButtonAction.class, new IButtonActionAbstractAdapter());
            builder.registerTypeAdapter(IFeatureProvider.class, new IFeatureProviderAbstractAdapter());
            builder.registerTypeAdapter(IPage.class, new IPageAbstractAdapter());
            builder.registerTypeAdapter(Book.class, new BookAbstractAdapter());
            GsonHelper.gson = builder.create();
        }

        return GsonHelper.gson;
    }
    
    public static Gson gson = null;
}
