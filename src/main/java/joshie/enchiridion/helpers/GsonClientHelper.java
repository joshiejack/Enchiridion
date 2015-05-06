package joshie.enchiridion.helpers;

import joshie.enchiridion.designer.features.Feature;
import joshie.enchiridion.designer.features.FeatureAbstractAdapter;
import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.elements.ElementAbstractAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonClientHelper {
    public static Gson getGson() {
        if (GsonClientHelper.gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation();
            builder.registerTypeAdapter(Element.class, new ElementAbstractAdapter());
            builder.registerTypeAdapter(Feature.class, new FeatureAbstractAdapter());
            GsonClientHelper.gson = builder.create();
        }

        return GsonClientHelper.gson;
    }
    
    public static Gson gson = null;
}
