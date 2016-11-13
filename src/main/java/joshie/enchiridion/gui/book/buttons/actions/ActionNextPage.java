package joshie.enchiridion.gui.book.buttons.actions;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionNextPage extends AbstractAction {
    public ActionNextPage() {
        super("next");
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionNextPage());
    }

    @Override
    public IButtonAction create() {
        return new ActionNextPage();
    }

    @Override
    public boolean performAction() {
        try {
            List<IPage> pages = EnchiridionAPI.book.getBook().getPages();
            List<Integer> numbers = pages.stream().map(IPage::getPageNumber).collect(Collectors.toList());

            Collections.sort(numbers, new SortNumerical());

            int number = EnchiridionAPI.book.getPage().getPageNumber();
            for (Integer integer : numbers) {
                if (integer > number) {
                    return EnchiridionAPI.book.jumpToPageIfExists(integer);
                }
            }

            //If we failed to find the next available page, reset the book to page 1
            return EnchiridionAPI.book.jumpToPageIfExists(numbers.get(0));
        } catch (Exception ignored) {
        }
        return false;
    }
}