package joshie.enchiridion.books.features.actions;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IPage;

public class ActionPreviousPage extends AbstractAction {
    public ActionPreviousPage() {
        super("previous");
    }
    
    @Override
    public ActionPreviousPage copy() {
        return new ActionPreviousPage();
    }

    @Override
    public IButtonAction create() {
        return new ActionPreviousPage();
    }

    @Override
    public void performAction() {
        try {
            List<IPage> pages = EnchiridionAPI.book.getBook().getPages();
            int number = EnchiridionAPI.book.getPage().getPageNumber() - 1;
            while (number >= 0) {
                for (IPage page : pages) {
                    if (page.getPageNumber() == number) {
                        EnchiridionAPI.book.setPage(page);
                        return; //Cancel further operations
                    }
                }

                number--;
            }

            //If we failed to find the next available page, reset the book to page 1

            IPage maxPage = null;
            for (IPage page : pages) {
                if (maxPage == null || page.getPageNumber() > maxPage.getPageNumber()) {
                    maxPage = page;
                }
            }

            EnchiridionAPI.book.setPage(maxPage);
        } catch (Exception e) {}
    }
}
