package joshie.enchiridion.data.book;

import com.google.common.collect.HashMultimap;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.event.FeatureVisibleEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EventBusSubscriber
public class BookEvents {
    public static final HashMap<String, HashMultimap<Integer, Pattern>> INVERTED = new HashMap<>();

    @SubscribeEvent
    public static void onAttemptToRender(FeatureVisibleEvent event) {
        if (INVERTED.containsKey(event.bookID)) {
            HashMultimap<Integer, Pattern> map = INVERTED.get(event.bookID);
            if (map.containsKey(event.page)) {
                for (Pattern pattern : map.get(event.page)) {
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
        if (INVERTED.containsKey(book.getUniqueName())) {
            HashMultimap<Integer, Pattern> map = INVERTED.get(book.getUniqueName());
            if (map.containsKey(page.getPageNumber())) {
                Collection<Pattern> layers = map.get(page.getPageNumber());
                if (layers.contains(pattern)) {
                    return layers.remove(pattern);
                }
            }
        }

        //Otherwise
        HashMultimap<Integer, Pattern> map = INVERTED.get(book.getUniqueName());
        if (map == null) map = HashMultimap.create();
        map.get(page.getPageNumber()).add(pattern);
        INVERTED.put(book.getUniqueName(), map);
        return true;
    }
}