package joshie.enchiridion.json;

import com.google.gson.*;
import joshie.enchiridion.api.book.IFeature;
import joshie.enchiridion.gui.book.features.FeatureError;
import joshie.enchiridion.lib.EInfo;

import java.lang.reflect.Type;

public class IFeatureAbstractAdapter implements JsonSerializer<IFeature>, JsonDeserializer<IFeature> {
    @Override
    public JsonElement serialize(IFeature src, Type typeOfSrc, JsonSerializationContext context) {
    	JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        JsonObject element = result.get("properties").getAsJsonObject();
        src.writeToJson(element);
        return result;
    }

    @Override
    public IFeature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	JsonObject jsonObject = json.getAsJsonObject();
        JsonElement type = jsonObject.get("type"); //Compatiblity with previous books
        
        String clazz = "";
        if (type != null) {
            clazz = EInfo.JAVAPATH + "books.features." + type.getAsString();
        } else {
        	clazz = jsonObject.get("class").getAsString();
        }
        
        //For no reason except i changed the name during development
        if (clazz.startsWith("joshie.enchiridion.books.features")) {
            clazz = clazz.replace("joshie.enchiridion.books.features", "joshie.enchiridion.gui.book.features");
        }
        
        JsonElement element = jsonObject.get("properties");
        try {
        	IFeature feature = context.deserialize(element, Class.forName(clazz));
        	feature.readFromJson(element.getAsJsonObject());
            return feature;
        } catch (ClassNotFoundException cnfe) {
            return new FeatureError(); //Return an error feature
            //We're doing this so that the whole book doesn't become broken just because one part did
        }
    }
}
