package joshie.enchiridion.data.library;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ModdedBooks {
    private List<ModdedBook> books = new ArrayList<>();
    private List<String> freeBooks = new ArrayList<>();

    public ModdedBooks() {
    }

    public void mergeIn(ModdedBooks outer) {
        books.addAll(outer.books);
        freeBooks.addAll(outer.freeBooks);
    }

    public static class ModdedBook {
        private String item;
        private String handlerType;
        private boolean matchDamage;
        private boolean matchNBT;

        private ModdedBook() {}
        public ModdedBook(String item, String handlerType, boolean matchNBT) {
            this.item = item;
            this.handlerType = handlerType;
            this.matchNBT = matchNBT;
        }

        public String getItem() {
            return item;
        }

        public String getHandler() {
            return handlerType;
        }

        public boolean shouldMatchNBT() {
            return matchNBT;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((item == null) ? 0 : item.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ModdedBook other = (ModdedBook) obj;
            if (item == null) {
                if (other.item != null) return false;
            } else if (!item.equals(other.item)) return false;
            return true;
        }
    }

    public void add(String handlerType, String itemString, boolean matchNBT) {
        books.add(new ModdedBook(itemString, handlerType, matchNBT));
    }

    public List<ModdedBook> getList() {
        return books;
    }

    public ItemStack[] getFreeBooks() {
        ItemStack[] books = new ItemStack[freeBooks.size()];
        for (int i = 0; i < freeBooks.size(); i++) {
            books[i] = StackHelper.getStackFromString(freeBooks.get(i));
        }

        return books;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((books == null) ? 0 : books.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ModdedBooks other = (ModdedBooks) obj;
        if (books == null) {
            if (other.books != null) return false;
        } else if (!books.equals(other.books)) return false;
        return true;
    }
}