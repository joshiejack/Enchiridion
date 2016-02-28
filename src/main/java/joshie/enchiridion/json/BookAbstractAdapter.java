package joshie.enchiridion.json;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.ELogger;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.Book;
import joshie.lib.helpers.JSONHelper;

public class BookAbstractAdapter implements JsonDeserializer<Book> {
    @Override
    public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	JsonObject jsonObject = json.getAsJsonObject();
    	Book book = new Book();
    	book.color = 0xFFFFFFFF; //Default the colour loading to white
    	try {
    		//Load the basic fields
	    	Field[] fields = Book.class.getFields();
	    	for (Field f: fields) {
	    		if (f.getType().equals(boolean.class)) f.set(book, JSONHelper.getBooleanIfExists(jsonObject, f.getName()));
	    		if (f.getType().equals(int.class)) f.set(book, JSONHelper.getIntegerIfExists(jsonObject, f.getName()));
	    		if (f.getType().equals(String.class)) f.set(book, JSONHelper.getStringIfExists(jsonObject, f.getName()));
	    	}
    	} catch (Exception e) { e.printStackTrace(); }
    	
    	//Add in the book data
    	book.book = new ArrayList();
    	if (EConfig.debugMode) ELogger.log(Level.INFO, "=== Preparing to read the book : " + book.displayName + " ===");
    	JsonArray array = jsonObject.get("book").getAsJsonArray();
    	for (int i = 0; i < array.size(); i++) {
    		JsonObject page = array.get(i).getAsJsonObject();
    		IPage ipage = context.deserialize(page, IPage.class);
    		book.book.add(ipage);
    	}
    	
    	if (jsonObject.get("showArrows") != null) { //READING IN AN OLD BOOK
    		book.mc189book = false;
    		book.legacyTexture = true;
    		book.showArrows = JSONHelper.getBooleanIfExists(jsonObject, "showArrows");
    		book.iconPass1 = JSONHelper.getStringIfExists(jsonObject, "iconPass1");
    		book.color = JSONHelper.getIntegerIfExists(jsonObject, "color");
    		book.colorHex = Integer.toHexString(book.color);
    	} else book.mc189book = true;
    	
    	if (book.displayName == null) book.displayName = book.uniqueName;
    	if (book.language == null) book.language = "en_US";
    	if (book.saveName == null) book.saveName = book.uniqueName;
    	
    	return book;
    }
}