package joshie.enchiridion.gui.book.buttons;

import com.google.common.collect.Lists;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;

import java.util.List;
import java.util.stream.Collectors;

public class ButtonDeletePage extends ButtonAbstract {
    public ButtonDeletePage() {
        super("delete");
    }

    @Override
    public void performAction() {
        IPage currentPage = EnchiridionAPI.book.getPage();
        int numberOfPages = EnchiridionAPI.book.getBook().getPages().size();
        int pageNumber;
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
        List<Integer> numbersTemp = pages.stream().map(IPage::getPageNumber).sorted(Integer::compareTo).collect(Collectors.toList());

        List<Integer> numbers = Lists.reverse(numbersTemp);
        int number = EnchiridionAPI.book.getPage().getPageNumber();
        for (Integer integer : numbers) {
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