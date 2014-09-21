package joshie.enchiridion.wiki;

import java.util.ArrayList;

import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.gui.GuiBackground;
import joshie.enchiridion.wiki.gui.GuiCanvas;
import joshie.enchiridion.wiki.gui.GuiExtension;
import joshie.enchiridion.wiki.gui.GuiLighting;
import joshie.enchiridion.wiki.gui.GuiMain;
import joshie.enchiridion.wiki.gui.GuiMenu;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.input.Mouse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WikiHelper {
    public static GuiMain gui = new GuiMain();
    private static boolean EDIT_MODE;
    
    public static boolean lighting;
    public static int theLeft;
    public static int theTop;
    public static int theScrolled;
    public static int mouseX;
    public static int mouseY;
    public static int height;

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Element.class, new WikiAbstractAdapter()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    }
    
    public static void drawRect(int x, int y, int x2, int y2, int color) {
        gui.drawRect(theLeft + x, theTop + y, theLeft + x2, theTop + y2, color);
    }
    
    public static void horizontalGradient(int x, int y, int x2, int y2, int from, int to) {
        gui.horizontalGradient(theLeft + x, theTop + y, theLeft + x2, theTop + y2, from, to);
    }
    
    public static void verticalGradient(int x, int y, int x2, int y2, int from, int to) {
        gui.verticalGradient(theLeft + x, theTop + y, theLeft + x2, theTop + y2, from, to);
    }
    
    public static void drawScaledCentredText(float scale, String text, int x, int y, int color) {
        gui.drawScaledCentredText(gui.mc, scale, text, x, y, color);
    }
    
    public static void drawScaledText(float scale, String text, int x, int y, int color) {
        gui.drawScaledText(gui.mc, scale, text, x, y, color);
    }
    
    public static void fixLighting() {
        lighting = !lighting;
        
        if(lighting) {
            RenderHelper.enableGUIStandardItemLighting();
        } else {
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    public static WikiPage getPage() {
        return gui.page;
    }
    
    public static void updateMouse() {
        mouseX = ((Mouse.getEventX() * gui.width / gui.mc.displayWidth) * gui.resolution.getScaleFactor()) - gui.getLeft();
        mouseY = ((gui.mc.displayHeight - Mouse.getEventY())) - gui.getTop(1.0F, 0);
    }
    
    public static void updateGUI(){
        theLeft = (int) (gui.mc.displayWidth / 2D) - 512;
        theTop = 150;
        theScrolled = theTop + getPage().getContent().getScroll();
        height = gui.mc.displayHeight;
    }

    public static boolean isEditMode() {
        return gui.page.isEditMode();
    }

    public static void setPage(String mod, String tab, String cat, String page) {
        gui.setPage(mod, tab, cat, page);
    }

    public static ArrayList<GuiExtension> content = new ArrayList();
    static {
        content.add(new GuiBackground());
        content.add(new GuiLighting());
        content.add(new GuiCanvas());
        content.add(new GuiLighting());
        content.add(new GuiMenu());
    }
    public static WikiMod getMod() {
        return gui.mod;
    }
}
