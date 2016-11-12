package joshie.enchiridion.api;

import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IButtonAction;
import joshie.enchiridion.api.book.ITemplate;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.api.gui.IToolbarButton;
import joshie.enchiridion.api.recipe.IRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;

public interface IEnchiridionAPI {      
    /** Registering your mod, will have the mod search your assets folder
     *  for a books folder with json. You need to do this if you wish your book data
     *  to ever get registered.
     *  
     *  @param modid  This should either be just your modid i.e. "Mariculture" , IF your assets path is the same
     *              ^ the assets path will get converted to all lower case, so don't worry about capitalisation for the above
     *              or in the format "modid:assets_path". for example, a mod called called Smash with the modid Fish and assets Dog would be.
     *              
     *              Fish:dog
     *              
     *              With the first half being the mod id, and the second half being
     *              what your assets folder is called, where you store the book json
     *              Although nowadays it's more than likely your modid is the same as
     *              your assets path so just that will do!.*/
    public void registerModWithBooks(String modid);

    /** Register a recipe handler, Client Side only **/
    public void registerRecipeHandler(IRecipeHandler handler);
    
    /** Register an editor overlay, Client Side only **/
    public void registerEditorOverlay(IBookEditorOverlay overlay);
    
    /** Register a toolbar button, Client Side only **/
    public void registerToolbarButton(IToolbarButton button);
    
    /** Register a button action, Client Side only **/
    public void registerButtonAction(IButtonAction action);

    /** Register a custom template **/
    public void registerTemplate(ITemplate template);

    /** Call this to get a book from it's id **/
    public IBook getBook(String bookid);

    /** Call this to open a books gui **/
    public void openBook(EntityPlayer player, String bookid, int page);
}
