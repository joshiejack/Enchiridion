package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IPage;

public class ActionJumpPage extends AbstractAction {
    public String bookID;
    public int pageNumber;

    public ActionJumpPage() {
        super("jump");
    }

    @Override
    public IButtonAction copy() {
        ActionJumpPage action = new ActionJumpPage();
        action.bookID = bookID;
        action.pageNumber = pageNumber;
        copyAbstract(action);
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
    }

    public ActionJumpPage(int pageNumber) {
        this();
        this.name = "" + pageNumber;
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean performAction() {
        if (bookID != null){
            IBook book = EnchiridionAPI.instance.getBook(bookID);
            if (book != null) EnchiridionAPI.book.setBook(book, EnchiridionAPI.book.isEditMode());
        }

        return EnchiridionAPI.book.jumpToPageIfExists(pageNumber);
    }
}
