package joshie.enchiridion.library;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.IBookHandler;
import joshie.enchiridion.api.ILibraryHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.handlers.DefaultBookHandler;
import joshie.enchiridion.library.handlers.NetworkSwitchHandler;
import joshie.enchiridion.library.handlers.SwitchBookHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class LibraryRegistry implements ILibraryHelper {
    public static LibraryRegistry INSTANCE = new LibraryRegistry();
    private static HashMap<String, IBookHandler> handlers = new HashMap();
    private static ArrayList<ItemStack> books = new ArrayList();
    public static IBookHandler default_handler;
    public static IBookHandler switch_handler;
    public static IBookHandler network_handler;

    public void initRegistry() {
        default_handler = new DefaultBookHandler();
        switch_handler = new SwitchBookHandler();
        network_handler = new NetworkSwitchHandler();
    }

    @Override
    public void registerDefault(ItemStack stack) {
        register(stack, default_handler);
    }

    @Override
    public void registerSwitch(ItemStack stack) {
        register(stack, switch_handler);
    }

    @Override
    public void registerNetworkSwitch(ItemStack stack) {
        register(stack, network_handler);
    }

    @Override
    public void register(ItemStack stack, IBookHandler handler) {
      //Remove the nbt to set the correct handling for this book type
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null;
        
        //Get the key from the removed_nbt
        String key = StackHelper.getStringFromStack(removed_nbt);
        books.add(stack);
        handlers.put(key, handler);
    }

    public static ArrayList<ItemStack> getBooks() {
        return books;
    }

    public static IBookHandler getHandler(ItemStack stack) {
        ItemStack removed_nbt = stack.copy();
        removed_nbt.stackTagCompound = null;
        //Remove the nbt to get the correct handling for this book type
        String key = StackHelper.getStringFromStack(removed_nbt);
        IBookHandler handler = handlers.get(key);
        return handler == null ? default_handler : handler;
    }

    /** Called to load book data **/
    public void load() {
        try {
            new File(Enchiridion.root, "/library").mkdir();

            NBTTagCompound tag = CompressedStreamTools.read(new File(Enchiridion.root, "/library/data.dat"));
            if (tag != null) {
                NBTTagList list = tag.getTagList("BooksList", 10);
                for (int i = 0; i < list.tagCount(); i++) {
                    overwrite(StackHelper.getItemStackFromNBT(list.getCompoundTagAt(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Called whenever a books data changed to save it to the client **/
    public void save() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (ItemStack book : books) {
            list.appendTag(StackHelper.writeItemStackToNBT(new NBTTagCompound(), book));
        }

        tag.setTag("BooksList", list);

        try {
            CompressedStreamTools.write(tag, new File(Enchiridion.root, "/library/data.dat"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Overwrites an existing equal book with a saved/updated version **/
    public void overwrite(ItemStack stack) {
        int index = 0;
        for (int i = 0; i < books.size(); i++) {
            if (stack.isItemEqual(books.get(i))) {
                index = i;
                break;
            }
        }

        books.set(index, stack);
        save();
    }

    public ItemStack get(Item item) {
        for (int i = 0; i < books.size(); i++) {
            if (new ItemStack(item).isItemEqual(books.get(i))) {
                return books.get(i);
            }
        }
        
        return new ItemStack(Items.book);
    }
}
