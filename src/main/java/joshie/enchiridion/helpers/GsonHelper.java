package joshie.enchiridion.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.Book;
import joshie.enchiridion.data.book.FeatureProvider;
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
            builder.registerTypeAdapter(FeatureProvider.class, new FeatureAbstractAdapter());
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
