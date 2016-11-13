package joshie.enchiridion.api;

import joshie.enchiridion.api.book.IBookHelper;
import joshie.enchiridion.api.edit.IEditHelper;
import joshie.enchiridion.api.gui.IDrawHelper;
import joshie.enchiridion.api.library.ILibraryRegistry;

public class EnchiridionAPI {
    /** Instance of the enchiridion api **/
    public static IEnchiridionAPI instance;

    /** Reference to the draw helper,
     *  when drawing features **/
    public static IDrawHelper draw;

    /** Reference to the current book
     *  and page information **/
    public static IBookHelper book;

    /** Reference to editing modes **/
    public static IEditHelper editor;

    /** Instance of the library registry **/
    public static ILibraryRegistry library;
}