package joshie.enchiridion.json;

import com.google.gson.*;
import joshie.enchiridion.api.book.IButtonAction;

import java.lang.reflect.Type;

public class IButtonActionAbstractAdapter implements JsonSerializer<IButtonAction>, JsonDeserializer<IButtonAction> {
    @Override
    public JsonElement serialize(IButtonAction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(src.getClass().getCanonicalName()));
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

        //Update actions
        if (clazz.startsWith("joshie.enchiridion.books.features.actions")) {
            clazz = clazz.replace("joshie.enchiridion.books.features.actions", "joshie.enchiridion.gui.book.buttons.actions");
        }

        try {
            IButtonAction action = context.deserialize(element, Class.forName(clazz));
            action.readFromJson(element.getAsJsonObject());
            return action;
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + clazz, cnfe);
        }
    }
}