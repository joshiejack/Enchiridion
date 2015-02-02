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
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketSyncLibraryBooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import org.apache.commons.io.FileUtils;

public class LibrarySaveData extends WorldSavedData {
    /** The name this data is saved as **/
    static final String DATA_NAME = "Library-Storage";

    /** This is the hashmap, storage a playerd UUID to their LibraryStorage data **/
    private HashMap<UUID, LibraryStorage> data = new HashMap();
    /** This is the instance of ModBooks, which gets loaded in server side,
     * it is a list of all valid books to go in to the library. */
    private ModBooks modBooks;

    /** The constructor for this data **/
    public LibrarySaveData(String string) {
        super(string);
    }

    /** Adds a 'new' book to the library storage if it is allowed' **/
    public void addUnlockedBook(EntityPlayer player, ItemStack stack, ItemStack overwrites) {        
        LibraryStorage storage = getOrCreateStorage(player.getPersistentID());
        if (overwrites == null) {
            storage.add(stack);
        } else storage.overwrite(stack, overwrites);
        this.markDirty();
    }

    /** This will reload the list of books that are accepted in the library server side **/
    public void reloadBooks() {
        modBooks = null; //Reset modbooks to a null value, clearing it

        /** First step is to grab the json file, then check if it exists, if not, we will create
         * this json file. Otherwise we will set the value of modBooks to the one that is loaded */
        File default_file = new File(Enchiridion.root, "library_books.json");
        if (!default_file.exists()) {
            //If the config doesn't exist we should make it. Using the default list of books.
            modBooks = ModBooks.getModBooks(new ModBooks());
            try {
                Writer writer = new OutputStreamWriter(new FileOutputStream(default_file), "UTF-8");
                writer.write(GsonServerHelper.getGson().toJson(modBooks));
                writer.close(); //Write the default json to file
            } catch (Exception e) {}
        } else {
            //Now that we know another config exists we should load it in
            try {
                modBooks = GsonServerHelper.getGson().fromJson(FileUtils.readFileToString(default_file), ModBooks.class);
            } catch (Exception e) {}
        }

        /** Inits the modbooks, creating their item stacks **/
        modBooks.init();
    }

    /** Sends the ModBooks to the client as json, and sends their LibraryStorage value **/
    public void updateClient(EntityPlayerMP player) {
        LibraryStorage storage = getOrCreateStorage(player.getPersistentID()).updateStoredBooks(modBooks); //Get the storage
        data.put(player.getPersistentID(), storage); //Save the updated data
        //TODO: SEND A PACKET WITH THE NEW LIBRARYSTORAGE TO PLAYERS, AND THE MODBOOKS FILE
        EPacketHandler.sendToClient(new PacketSyncLibraryBooks(storage, modBooks), player);
        //No matter what when we are updating the client, it means we have updated their storage
        //So we should mark this as dirty
        this.markDirty();
    }

    /** Returns either a new piece of librarystorage or the one that is attached to this UUID **/
    public LibraryStorage getOrCreateStorage(UUID uuid) {
        if (data.containsKey(uuid)) {
            return data.get(uuid);
        } else return new LibraryStorage();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {        
        //Read in the stored list of UUID > Data Mappings
        NBTTagList tracker = nbt.getTagList("LibraryTracker", 10);
        for (int i = 0; i < tracker.tagCount(); i++) {
            NBTTagCompound tag = tracker.getCompoundTagAt(i);
            UUID uuid = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            LibraryStorage storage = new LibraryStorage();
            storage.readFromNBT(tag);
            data.put(uuid, storage);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {        
        //Write the list of UUID > Data Mappings
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
