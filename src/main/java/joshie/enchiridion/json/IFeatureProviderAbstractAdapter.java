package joshie.enchiridion.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.data.book.FeatureProvider;

public class IFeatureProviderAbstractAdapter implements JsonSerializer<IFeatureProvider>, JsonDeserializer<IFeatureProvider> {
    @Override
    public JsonElement serialize(IFeatureProvider src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, FeatureProvider.class);
    }

    @Override
    public IFeatureProvider deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, FeatureProvider.class);
    }
}
