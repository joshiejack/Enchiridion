package joshie.enchiridion.api;

public class EnchiridionAPI {
    /** Instance of the enchiridion api **/
    public static IEnchiridionAPI instance;
    
    /** Reference to the draw helper, 
     *  when drawing features **/
    public static IDrawHelper draw;
    
    /** Reference to the current book 
     *  and page information **/
    public static IBookHelper book;
}
