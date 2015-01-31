package joshie.enchiridion.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import joshie.enchiridion.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.gson.annotations.Expose;

public class LibraryStorage {
    @Expose
    private HashMap<String, String> handlers = new HashMap(); //List of the books and which handler they correlate to
    @Expose
    private ArrayList<ItemStack> books = new ArrayList(); ///List of all the books on display in the library

    private ArrayList<Integer> toRemove = new ArrayList();

    public LibraryStorage() {}
    public LibraryStorage(LibraryStorage copy) {
        this.handlers = copy.handlers;
        this.books = copy.books;
    }

    public String getHandler(String key) {
        return handlers.get(key);
    }

    public ArrayList<ItemStack> getBooks() {
        return books;
    }

    //Overwrite the data stored for this
    public void overwrite(ItemStack stack) {
        int index = 0;
        for (index = 0; index < books.size(); index++) {
            ItemStack book = books.get(index);
            if (book.isItemEqual(stack)) {
                break;
            }
        }

        books.set(index, stack);
    }

    //Loops through the books and cleans them up and removes ones that are no longer allowed, Passes the default
    public void sanitize(LibraryStorage library) {
        int index = 0;
        //Loop through the books
        for (ItemStack book : books) {
            boolean toRemove = true;
            for (ItemStack libBook : library.books) { //Loop through the default library
                if (libBook.isItemEqual(book)) toRemove = false;
                break;
            }

            if (toRemove) {
                this.toRemove.add(index);
            }

            index++;
        }

        //Now that we have cached all the items that need to be removed Let's do it
        for (Integer i : toRemove) {
            ItemStack stack = books.get(i);
            String key = StackHelper.getStringFromStack(stack);
            handlers.remove(key);
            books.remove(i);
        }
    }

    //Adds this book in to handlers and books
    public void add(ItemStack stack, String type) {
        //Remove the nbt to set the correct handling for this book type
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null;

        //Get the key from the removed_nbt
        String key = StackHelper.getStringFromStack(removed_nbt);
        books.add(stack);
        handlers.put(key, type);
    }

    //Called ServerSide to read this data
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList handles = nbt.getTagList("Handles", 10);
        for (int i = 0; i < handles.tagCount(); i++) {
            NBTTagCompound tag = handles.getCompoundTagAt(i);
            String key = tag.getString("Key");
            String value = tag.getString("Value");
            handlers.put(key, value);
        }

        NBTTagList stacks = nbt.getTagList("BooksList", 10);
        for (int i = 0; i < stacks.tagCount(); i++) {
            NBTTagCompound tag = stacks.getCompoundTagAt(i);
            ItemStack stack = StackHelper.getItemStackFromNBT(tag);
            books.add(stack);
        }
    }

    //Called ServerSide to save this data
    public void writeToNBT(NBTTagCompound nbt) {
        //Saving the handlers
        NBTTagList handles = new NBTTagList();
        for (Map.Entry<String, String> entry : handlers.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Key", entry.getKey());
            tag.setString("Value", entry.getValue());
            handles.appendTag(tag);
        }

        nbt.setTag("Handles", handles);

        //Saving the books
        NBTTagList stacks = new NBTTagList();
        for (ItemStack stack : books) {
            stacks.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), stack));
        }

        nbt.setTag("BooksList", stacks);
    }
}
