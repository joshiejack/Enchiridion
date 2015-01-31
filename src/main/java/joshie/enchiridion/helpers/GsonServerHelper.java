package joshie.enchiridion.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonServerHelper {
    public static Gson getGson() {
        if (GsonServerHelper.gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation();
            GsonServerHelper.gson = builder.create();
        }

        return GsonServerHelper.gson;
    }

    public static Gson gson = null;
}
