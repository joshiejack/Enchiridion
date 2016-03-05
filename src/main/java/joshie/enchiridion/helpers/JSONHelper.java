package joshie.enchiridion.helpers;

import com.google.gson.JsonObject;

public class JSONHelper {
	public static String getStringIfExists(JsonObject json, String name) {
		if (json.get(name) != null) {
			return json.get(name).getAsString();
		} else return "";
	}
	
	public static int getIntegerIfExists(JsonObject json, String name) {
		if (json.get(name) != null) {
			return json.get(name).getAsInt();
		} else return 0;
	}

	public static boolean getBooleanIfExists(JsonObject json, String name) {
		if (json.get(name) != null) {
			return json.get(name).getAsBoolean();
		} else return false;
	}
}
