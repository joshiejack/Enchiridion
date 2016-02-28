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

import joshie.enchiridion.api.IButtonAction;

public class IButtonActionAbstractAdapter implements JsonSerializer<IButtonAction>, JsonDeserializer<IButtonAction> {
    @Override
    public JsonElement serialize(IButtonAction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class",  new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        JsonObject element = result.get("properties").getAsJsonObject();
        src.writeToJson(element);
        return result;
    }

    @Override
    public IButtonAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String clazz = jsonObject.get("class").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            IButtonAction action = context.deserialize(element, Class.forName(clazz));
            action.readFromJson(element.getAsJsonObject());
            return action;
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + clazz, cnfe);
        }
    }
}
