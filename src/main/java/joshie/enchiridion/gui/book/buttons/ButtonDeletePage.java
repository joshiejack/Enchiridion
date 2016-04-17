package joshie.enchiridion.gui.book.buttons;

import com.google.common.collect.Lists;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ButtonDeletePage extends ButtonAbstract {
    public ButtonDeletePage() {
        super("delete");
    }

    @Override
    public void performAction() {
        IPage currentPage = EnchiridionAPI.book.getPage();
        int numberOfPages = EnchiridionAPI.book.getBook().getPages().size();
        int pageNumber = 1;
        if (numberOfPages > 1) {
            pageNumber = getPreviousPage();
            EnchiridionAPI.book.jumpToPageIfExists(pageNumber); //Jump to the previous page
            //Delete the older page
            EnchiridionAPI.book.getBook().removePage(currentPage);
        } else {
            EnchiridionAPI.book.getPage().clear();
        }
    }

    public int getPreviousPage() {
        List<IPage> pages = EnchiridionAPI.book.getBook().getPages();
        List<Integer> numbersTemp = new ArrayList<Integer>();
        for (IPage page: pages) {
            numbersTemp.add(page.getPageNumber());
        }

        Collections.sort(numbersTemp, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer)o1).compareTo((Integer)o2);
            }
        });

        List<Integer> numbers = Lists.reverse(numbersTemp);
        int number = EnchiridionAPI.book.getPage().getPageNumber();
        for (Integer integer: numbers) {
            if (integer < number) {
                return integer;
            }
        }

        return numbers.get(0);
    }

    @Override
    public boolean isLeftAligned() {
        return false;
    }
}
