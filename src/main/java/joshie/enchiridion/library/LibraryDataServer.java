package joshie.enchiridion.library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.helpers.GsonServerHelper;
import joshie.enchiridion.helpers.StackHelper;
import joshie.enchiridion.library.ModBooks.ModBookData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.Loader;

public class LibraryDataServer extends WorldSavedData {
    static final String DATA_NAME = "Library-Data";
    private HashMap<UUID, LibraryStorage> data = new HashMap();
    public static LibraryStorage DEFAULT;

    public LibraryDataServer(String string) {
        super(string);
    }

    /** Overwrites this book for this player **/
    public void overwrite(EntityPlayerMP player, ItemStack stack) {
        getStorageFor(player).overwrite(stack);
        markDirty();
    }

    /** Called when a world loads to preload in the default books */
    public static void preloadBooks() {
        ModBooks books = null;
        //Grab the config file
        File default_file = new File(Enchiridion.root, "library_books.json");
        if (!default_file.exists()) {
            //If the config doesn't exist we should make it.
            books = ModBooks.getModBooks(new ModBooks());
            try {
                Writer writer = new OutputStreamWriter(new FileOutputStream(default_file), "UTF-8");
                writer.write(GsonServerHelper.getGson().toJson(books));
                writer.close(); //Write the default json to file
            } catch (Exception e) {}
        } else {
            //Now that we know another config exists we should load it in
            try {
                books = GsonServerHelper.getGson().fromJson(FileUtils.readFileToString(default_file), ModBooks.class);
            } catch (Exception e) {}
        }

        //Now that we have our list of books we should Add them in to a LibraryStorage
        LibraryStorage storage = new LibraryStorage();
        for (ModBookData book : books.books) { //Loop through the list of modadded books
            //If the mod is loaded add it's book to the default list
            if (Loader.isModLoaded(book.mod)) {
                ItemStack stack = StackHelper.getStackFromString(book.stack);
                if (stack != null && stack.getItem() != null) {
                    storage.add(stack, book.type);
                }
            }
        }

        LibraryDataServer.DEFAULT = storage;
    }

    public LibraryStorage getStorageFor(EntityPlayerMP player) {
        //If the player has never ever request their data before send them a new storage
        if (!data.containsKey(player.getPersistentID())) {
            return createNew(player);
        } else return data.get(player.getPersistentID());
    }

    public LibraryStorage createNew(EntityPlayerMP player) {
        LibraryStorage copy = new LibraryStorage(DEFAULT);
        this.data.put(player.getPersistentID(), copy);
        this.markDirty();
        return new LibraryStorage(DEFAULT);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tracker = nbt.getTagList("LibraryTracker", 10);
        for (int i = 0; i < tracker.tagCount(); i++) {
            NBTTagCompound tag = tracker.getCompoundTagAt(i);
            UUID uuid = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            LibraryStorage storage = new LibraryStorage();
            storage.readFromNBT(nbt);
            data.put(uuid, storage);
        }

        //Now that we have read the books in we should sanitize the storage
        //Aka remove all books that aren't in the default anymore
        for (Map.Entry<UUID, LibraryStorage> entry : data.entrySet()) {
            entry.getValue().sanitize(DEFAULT);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList tracker = new NBTTagList();
        for (Map.Entry<UUID, LibraryStorage> entry : data.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setLong("UUIDMost", entry.getKey().getMostSignificantBits());
                tag.setLong("UUIDLeast", entry.getKey().getLeastSignificantBits());
                entry.getValue().writeToNBT(tag);
                tracker.appendTag(tag);
            }
        }

        nbt.setTag("LibraryTracker", tracker);
    }
}
