package joshie.enchiridion.json;

import com.google.gson.*;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.data.book.FeatureProvider;

import java.lang.reflect.Type;

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