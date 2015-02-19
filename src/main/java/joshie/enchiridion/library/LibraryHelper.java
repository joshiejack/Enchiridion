package joshie.enchiridion.library;

import joshie.enchiridion.ELogger;
import net.minecraft.world.WorldServer;

import org.apache.logging.log4j.Level;

public class LibraryHelper {
    public static ModBooks modBooks; //The Client instance of ModBooks
    public static LibraryStorage storage = new LibraryStorage(ModBooks.getModBooks(new ModBooks())); //The Clients instance of LibraryStorage, Gets overwritten by the server
    public static LibrarySaveData data = null; //The Servers Save Data

    //Setup the data file
    public static void init(WorldServer world) {
        data = (LibrarySaveData) world.loadItemData(LibrarySaveData.class, LibrarySaveData.DATA_NAME);
        if (data == null) {
            ELogger.log(Level.INFO, "Enchiridion 2 couldn't find the world data for the library so is creating it fresh");
            data = new LibrarySaveData(LibrarySaveData.DATA_NAME);
            world.setItemData(LibrarySaveData.DATA_NAME, data);
        }
        
        //Load in the books
        data.reloadBooks();
    }
}
