package joshie.enchiridion.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class BookRegistry {
    public static class BookData {
        public ArrayList<DesignerCanvas> book;
        public String displayName;
        public List information;
        public int color;
        public boolean bookBackground = true;
        
        public BookData(String display, List info, int color) {
            this.displayName = display;
            this.information = info;
            this.color = color;
            this.book = new ArrayList();
        }
    }

    private static final HashMap<Integer, BookData> books = new HashMap();
    private static int lastID = 1;
    public static Integer getID(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) return null;
        Integer identifier = stack.stackTagCompound.getInteger("identifier");
        return identifier;
    }

    public static BookData getData(ItemStack stack) {
        Integer identifier = getID(stack);
        if (identifier == null) return null;
        return getData(identifier);
    }

    public static BookData getData(int ID) {
        return books.get(ID);
    }

    public static void register(BookData data) {
        books.put(lastID, data);
        lastID++;
    }
    
    public static Set<Integer> getIDs() {
        return books.keySet();
    }
}
