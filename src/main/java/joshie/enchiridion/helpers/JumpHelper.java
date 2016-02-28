package joshie.enchiridion.helpers;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;

public class JumpHelper {

	public static boolean jumpToPageByName(String name) {
		for (IPage page: EnchiridionAPI.draw.getBookPages()) {
			if (page.getPageName().equals(name)) {
				EnchiridionAPI.draw.setPage(page);
				return true;
			}
		}
		
		return false;
	}

	public static IPage getPageByName(String name) {
		for (IPage page: EnchiridionAPI.draw.getBookPages()) {
			if (page.getPageName().equals(name)) {
				return page;
			}
		}
		
		return null;
	}

	public static boolean jumpToPageByNumber(int number) {
		for (IPage page: EnchiridionAPI.draw.getBookPages()) {
			if (page.getPageNumber() == number) {
				EnchiridionAPI.draw.setPage(page);
				return true;
			}
		}
		
		return false;
	}
	
	public static IPage getPageByNumber(int number) {
		for (IPage page: EnchiridionAPI.draw.getBookPages()) {
			if (page.getPageNumber() == number) {
				return page;
			}
		}
		
		return null;
	}

	public static void insertPage(int pageNumber, IPage dragged) {
		if (dragged.getPageNumber() != pageNumber) {
			if (getPageByNumber(pageNumber) != null) {
				dragged.setPageNumber(pageNumber);
				for (IPage page: EnchiridionAPI.draw.getBookPages()) {
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
