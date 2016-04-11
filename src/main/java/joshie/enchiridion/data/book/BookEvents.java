package joshie.enchiridion.data.book;

import com.google.common.collect.HashMultimap;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.event.FeatureVisibleEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookEvents {
    public static HashMap<String, HashMultimap<Integer, Pattern>> inverted = new HashMap();

    @SubscribeEvent
    public void onAttemptToRender(FeatureVisibleEvent event) {
        if (inverted.containsKey(event.bookid)) {
            HashMultimap<Integer, Pattern> map = inverted.get(event.bookid);
            if (map.containsKey(event.page)) {
                for (Pattern pattern: map.get(event.page)) {
                    Matcher m = pattern.matcher("" + (event.layer + 1));
                    if (m.matches()) {
                        event.isVisible = !event.isVisible; //Inverted
                    }
                }
            }
        }
    }

    public static boolean invert(IBook book, IPage page, Pattern pattern) {
        //If we already had everything, then we shall remove it from the map
        if (inverted.containsKey(book.getUniqueName())) {
            HashMultimap map = inverted.get(book.getUniqueName());
            if (map.containsKey(page.getPageNumber())) {
                Collection<Pattern> layers = map.get(page.getPageNumber());
                if (layers.contains(pattern)) {
                    return layers.remove(pattern);
                }
            }
        }

        //Otherwise
        HashMultimap map = inverted.get(book.getUniqueName());
        if (map == null) map = HashMultimap.create();
        map.get(page.getPageNumber()).add(pattern);
        inverted.put(book.getUniqueName(), map);
        return true;
    }
}
