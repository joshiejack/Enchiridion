package joshie.enchiridion.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import joshie.enchiridion.api.IFeature;
import joshie.enchiridion.books.features.Feature;
import joshie.lib.helpers.JSONHelper;

public class FeatureAbstractAdapter implements JsonDeserializer<Feature> {
    @Override
    public Feature deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	Feature feature = new Feature();
    	JsonObject jsonObject = json.getAsJsonObject();
    	if (jsonObject.get("type") != null && jsonObject.get("type").getAsString() != null) { //OLD SYSTEM
    		JsonObject properties = jsonObject.get("properties").getAsJsonObject();
    		feature.xPos = properties.get("xPos").getAsInt();
    		feature.yPos = properties.get("yPos").getAsInt();
    		feature.width = properties.get("width").getAsInt();
    		feature.height = properties.get("height").getAsInt();
    		feature.feature = context.deserialize(jsonObject, IFeature.class);
    	} else { //NEW System
    		feature.xPos = jsonObject.get("xPos").getAsInt();
    		feature.yPos = jsonObject.get("yPos").getAsInt();
    		feature.width = jsonObject.get("width").getAsInt();
    		feature.height = jsonObject.get("height").getAsInt();
    		feature.isLocked = JSONHelper.getBooleanIfExists(jsonObject, "isLocked");
    		feature.isHidden = JSONHelper.getBooleanIfExists(jsonObject, "isHidden");
    		feature.feature = context.deserialize(jsonObject.get("feature"), IFeature.class);
    	}
    	
    	return feature;
    }
}