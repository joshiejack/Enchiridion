package joshie.enchiridion.gui.book.buttons.actions;

import com.google.common.collect.Lists;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.IPage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionPreviousPage extends AbstractAction {
    public ActionPreviousPage() {
        super("previous");
    }

    @Override
    public IButtonAction copy() {
        return copyAbstract(new ActionPreviousPage());
    }

    @Override
    public IButtonAction create() {
        return new ActionPreviousPage();
    }

    @Override
    public boolean performAction() {
        try {
            List<IPage> pages = EnchiridionAPI.book.getBook().getPages();
            List<Integer> numbersTemp = pages.stream().map(IPage::getPageNumber).collect(Collectors.toList());

            Collections.sort(numbersTemp, new SortNumerical());
            List<Integer> numbers = Lists.reverse(numbersTemp);

            int number = EnchiridionAPI.book.getPage().getPageNumber();
            for (Integer integer : numbers) {
                if (integer < number) {
                    return EnchiridionAPI.book.jumpToPageIfExists(integer);
                }
            }

            return EnchiridionAPI.book.jumpToPageIfExists(numbers.get(0));
        } catch (Exception ignored) {
        }
        return false;
    }
}