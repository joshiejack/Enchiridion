package joshie.enchiridion.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.Page;

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