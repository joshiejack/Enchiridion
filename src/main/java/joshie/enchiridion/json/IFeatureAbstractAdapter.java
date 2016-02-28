package joshie.enchiridion.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import joshie.enchiridion.api.IFeature;
import joshie.enchiridion.lib.EInfo;

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
        
        JsonElement element = jsonObject.get("properties");
        try {
        	IFeature feature = context.deserialize(element, Class.forName(clazz));
        	feature.readFromJson(element.getAsJsonObject());
            return feature;
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + clazz, cnfe);
        }
    }
}
