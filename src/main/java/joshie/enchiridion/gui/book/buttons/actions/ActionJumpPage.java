package joshie.enchiridion.gui.book.buttons.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.helpers.JSONHelper;
import joshie.enchiridion.helpers.JumpHelper;

public class ActionJumpPage extends AbstractAction {
	public transient IPage page;
	public transient String bookID;
	public transient String name;
	public transient int pageNumber;
	
	public ActionJumpPage() {
		super("jump");
	}
	
	@Override
	public ActionJumpPage copy() {
	    ActionJumpPage action = new ActionJumpPage();
	    action.bookID = bookID;
	    action.name = name;
	    action.pageNumber = pageNumber;
	    return action;
	}
	
	@Override
	public IButtonAction create() {
		ActionJumpPage jump = new ActionJumpPage(EnchiridionAPI.book.getPage());
		jump.bookID = EnchiridionAPI.book.getBook().getUniqueName();
		return jump;
	}
	
	public ActionJumpPage(IPage page) {
		this();
		this.name = page.getPageName();
		this.pageNumber = page.getPageNumber();
		this.page = page;
	}
	
	@Override
	public String[] getFieldNames() {
		return new String[] { "tooltip", "hoverText", "unhoveredText", "bookID", "pageNumber" };
	}
	
	@Override
	public void performAction() {	
		if (bookID != null && !bookID.equals("") && !bookID.equals(EnchiridionAPI.book.getBook().getUniqueName())) {
			GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBookByName(bookID), EnchiridionAPI.book.isEditMode());
		}
		
		if (page == null || page.getPageNumber() != pageNumber) {
			page = JumpHelper.getPageByNumber(pageNumber);
			if (page == null) page = JumpHelper.getPageByName(name);
		}
				
		EnchiridionAPI.book.setPage(page);
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		bookID = JSONHelper.getStringIfExists(json, "bookID");
		pageNumber = JSONHelper.getIntegerIfExists(json, "number");
		name = JSONHelper.getStringIfExists(json, "name");
	}

	@Override
	public void writeToJson(JsonObject object) {
		super.writeToJson(object);
		object.addProperty("bookID", bookID);
		if (page != null) {
			object.addProperty("number", page.getPageNumber());
			object.addProperty("name", page.getPageName());
		}
	}
}
