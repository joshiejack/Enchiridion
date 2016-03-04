package joshie.enchiridion.data.library;

import java.util.ArrayList;

public class ModdedBooks {
    private ArrayList<ModdedBook> books = new ArrayList();

    public ModdedBooks() {}
    
    public static class ModdedBook {
        private String item;
        private String handlerType;
        private boolean matchDamage;
        private boolean matchNBT;
        
        private ModdedBook() {}
        public ModdedBook(String item, String handlerType, boolean matchDamage, boolean matchNBT) {
            this.item = item;
            this.handlerType = handlerType;
            this.matchDamage = matchDamage;
            this.matchNBT = matchNBT;
        }
        
        public String getItem() {
            return item;
        }
        
        public String getHandler() {
            return handlerType;
        }
        
        public boolean shouldMatchDamage() {
            return matchDamage;
        }
        
        public boolean shouldMatchNBT() {
            return matchNBT;
        }
    }
    
    public void add(String handlerType, String itemString, boolean matchDamage, boolean matchNBT) {
        books.add(new ModdedBook(itemString, handlerType, matchDamage, matchNBT));
    }

    public ArrayList<ModdedBook> getList() {
        return books;
    }
}
