package joshie.enchiridion.api;

import net.minecraft.item.ItemStack;

public interface IEnchiridionAPI {   
    /** Registering an itemstack here, will cause them to open the bookdata
     *  with the identifier passed in.
     * @param stack
     * @param identifier */
    public void registerBookData(ItemStack stack, String identifier);
    
    /** Registering your mod, will have the mod search your assets folder
     *  for a books folder with json. If you wish to use your own item,
     *  instead of the built in ones (custom texture). Then you should use, the above
     *  method instead for each book. This one is provided for convenience.
     * @param modid */
    public void registerModBooks(String assetspath);
    
    /** Register a method for handling the opening of books **/
    public void registerBookHandler(IBookHandler handler);
}
