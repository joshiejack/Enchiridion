package joshie.enchiridion.helpers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IPage;

public class JumpHelper {
	public static boolean jumpToPageByNumber(int number) {
		for (IPage page: EnchiridionAPI.book.getBook().getPages()) {
			if (page.getPageNumber() == number) {
				EnchiridionAPI.book.setPage(page);
				return true;
			}
		}
		
		return false;
	}
	
	public static IPage getPageByNumber(IBook book, int number) {
		for (IPage page: book.getPages()) {
			if (page.getPageNumber() == number) {
				return page;
			}
		}
		
		return null;
	}

	public static void insertPage(IBook book, int pageNumber, IPage dragged) {
		if (dragged.getPageNumber() != pageNumber) {
			if (getPageByNumber(book, pageNumber) != null) {
				dragged.setPageNumber(pageNumber);
				for (IPage page: book.getPages()) {
					if (page == dragged) continue;
					else {
						//Increase any pagenumbers to come after the new insertion to their new value
						int original = page.getPageNumber();
						if (original >= pageNumber) {
							page.setPageNumber(original + 1);
						}
					}
				}
			} else dragged.setPageNumber(pageNumber);
		}
	}
}
