package joshie.enchiridion.json;

import com.google.gson.*;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.Page;

import java.lang.reflect.Type;

public class IPageAbstractAdapter implements JsonSerializer<IPage>, JsonDeserializer<IPage> {
    @Override
    public JsonElement serialize(IPage src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, Page.class);
    }

    @Override
    public IPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, Page.class);
    }
}