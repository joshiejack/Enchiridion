package joshie.enchiridion.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.Book;
import joshie.enchiridion.helpers.JSONHelper;

public class BookAbstractAdapter implements JsonDeserializer<Book> {
    @Override
    public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	JsonObject jsonObject = json.getAsJsonObject();
    	Book book = new Book();
    	book.setColorAsInt(0xFFFFFFFF); //Default the colour loading to white
    	try {
    		//Load the basic fields
	    	Field[] fields = Book.class.getDeclaredFields();
	    	for (Field f: fields) {
	    		f.setAccessible(true); //Screw my own private variables
	    		if (Modifier.isTransient(f.getModifiers())) continue; //Ignore transients
	    		if (f.getType().equals(boolean.class)) f.set(book, JSONHelper.getBooleanIfExists(jsonObject, f.getName()));
	    		if (f.getType().equals(int.class)) f.set(book, JSONHelper.getIntegerIfExists(jsonObject, f.getName()));
	    		if (f.getType().equals(String.class)) f.set(book, JSONHelper.getStringIfExists(jsonObject, f.getName()));
	    	}
    	} catch (Exception e) { e.printStackTrace(); }
    	
    	//Add in the book data
    	book.create();
    	if (EConfig.debugMode) Enchiridion.log(Level.INFO, "=== Preparing to read the book : " + book.getDisplayName() + " ===");
    	JsonArray array = jsonObject.get("book").getAsJsonArray();
    	for (int i = 0; i < array.size(); i++) {
    		JsonObject page = array.get(i).getAsJsonObject();
    		IPage ipage = context.deserialize(page, IPage.class);
    		book.addPage(ipage);
    	}
    	
    	if (jsonObject.get("showArrows") != null) { //READING IN AN OLD BOOK
    		book.setLegacy();
    		book.setArrowVisiblity(JSONHelper.getBooleanIfExists(jsonObject, "showArrows"));
    		book.setIconPass1(JSONHelper.getStringIfExists(jsonObject, "iconPass1"));
    		book.setColorAsInt(JSONHelper.getIntegerIfExists(jsonObject, "color"));
    	} else book.setMadeIn189();
    	
    	if (book.getDisplayName() == null) book.setDisplayName(book.getUniqueName());
    	if (book.getLanguageKey() == null) book.setLanguageKey("en_US");
    	if (book.getSaveName() == null) book.setSaveName(book.getUniqueName());
    	
    	return book;
    }
}