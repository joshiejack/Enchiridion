package joshie.enchiridion.data.book;

import com.google.common.collect.HashMultimap;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.event.FeatureVisibleEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;

public class BookEvents {
    public static HashMap<String, HashMultimap<Integer, Integer>> inverted = new HashMap();

    @SubscribeEvent
    public void onAttemptToRender(FeatureVisibleEvent event) {

        if (inverted.containsKey(event.bookid)) {

            HashMultimap<Integer, Integer> map = inverted.get(event.bookid);
            for (Integer i: map.keySet()) {
                //System.out.println("CONTAINER KEY: " + i);
            }

            //System.out.println("PASSING PAGE " + event.page);
            if (map.containsKey(event.page)) {


                //System.out.println("MAP FOUND");
                if (map.get(event.page).contains(event.layer)) {
                    //System.out.println("CHANGE");
                    event.isVisible = !event.isVisible; //Inverted
                }
            }
        }
    }

    public static boolean invert(IBook book, IPage page, int layer) {
        //If we already had everything, then we shall remove it from the map
        if (inverted.containsKey(book.getUniqueName())) {
            HashMultimap map = inverted.get(book.getUniqueName());
            if (map.containsKey(page.getPageNumber())) {
                Collection<Integer> layers = map.get(page.getPageNumber());
                if (layers.contains(layer)) {
                    return layers.remove(layer);
                }
            }
        }

        //Otherwise
        HashMultimap map = inverted.get(book.getUniqueName());
        if (map == null) map = HashMultimap.create();
        map.get(page.getPageNumber()).add(layer);
        inverted.put(book.getUniqueName(), map);
        return true;
    }
}
