package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.helpers.JumpHelper;

public class ActionJumpPage extends AbstractAction {
	public transient IPage page;
	public String bookID;
	public int pageNumber;
	
	public ActionJumpPage() {
		super("jump");
	}
	
	@Override
	public ActionJumpPage copy() {
	    ActionJumpPage action = new ActionJumpPage();
	    action.bookID = bookID;
	    action.name = name;
	    action.pageNumber = pageNumber;
		action.tooltip = tooltip;
		action.hoverText = hoverText;
		action.unhoveredText = unhoveredText;
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
		this.pageNumber = page.getPageNumber();
		this.page = page;
	}

    public ActionJumpPage(int pageNumber) {
        this();
        this.name = "" + pageNumber;
        this.pageNumber = pageNumber;
        this.page = null;
    }

	@Override
	public void performAction() {	
		if (bookID != null && !bookID.equals("") && !bookID.equals(EnchiridionAPI.book.getBook().getUniqueName())) {
			GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBookByName(bookID), EnchiridionAPI.book.isEditMode());
		}
		
		if (page == null || page.getPageNumber() != pageNumber) {
			page = JumpHelper.getPageByNumber(GuiBook.INSTANCE.getBook(), pageNumber);
		}
				
		EnchiridionAPI.book.setPage(page);
	}
}
