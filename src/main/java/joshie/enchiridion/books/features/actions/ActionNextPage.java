package joshie.enchiridion.books.features.actions;

import java.util.List;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IPage;

public class ActionNextPage extends AbstractAction {
	public ActionNextPage() {
		super("next");
	}
	
	@Override
    public ActionNextPage copy() {
        return new ActionNextPage();
    }

	@Override
	public IButtonAction create() {
		return new ActionNextPage();
	}

	@Override
	public void performAction() {
		try {
			List<IPage> pages = EnchiridionAPI.book.getBook().getPages();
			int number = EnchiridionAPI.book.getPage().getPageNumber() + 1;
			boolean success = false;
			IPage maxPage = null;
			for (IPage page: pages) {
				if (maxPage == null || page.getPageNumber() > maxPage.getPageNumber()) {
					maxPage = page;
				}
			}
			
			
			topLoop:
			while (number <= maxPage.getPageNumber()) {
				for (IPage page : pages) {
					if (page.getPageNumber() == number) {
						EnchiridionAPI.book.setPage(page);
						success = true;
						break topLoop;
					}
				}
				
				number++;
			}
			
			//If we failed to find the next available page, reset the book to page 1
			if (!success) {
				EnchiridionAPI.book.setPage(EnchiridionAPI.book.getBook().getPages().get(0));
			}
		} catch (Exception e) {
		}
	}
}
