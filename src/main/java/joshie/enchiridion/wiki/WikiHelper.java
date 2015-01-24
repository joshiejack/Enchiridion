package joshie.enchiridion.wiki;

import static joshie.lib.helpers.ClientHelper.bindTexture;
import static joshie.lib.helpers.OpenGLHelper.disable;
import static joshie.lib.helpers.OpenGLHelper.enable;
import static joshie.lib.helpers.OpenGLHelper.end;
import static joshie.lib.helpers.OpenGLHelper.scale;
import static joshie.lib.helpers.OpenGLHelper.start;
import static org.lwjgl.opengl.GL11.GL_BLEND;

import java.util.ArrayList;

import joshie.enchiridion.api.IWikiMode;
import joshie.enchiridion.library.GuiLibrary;
import joshie.enchiridion.library.GuiShelves;
import joshie.enchiridion.wiki.data.WikiData;
import joshie.enchiridion.wiki.elements.Element;
import joshie.enchiridion.wiki.elements.ElementItem;
import joshie.enchiridion.wiki.gui.GuiBackground;
import joshie.enchiridion.wiki.gui.GuiCanvas;
import joshie.enchiridion.wiki.gui.GuiColorEdit;
import joshie.enchiridion.wiki.gui.GuiExtension;
import joshie.enchiridion.wiki.gui.GuiHistory;
import joshie.enchiridion.wiki.gui.GuiItemSelect;
import joshie.enchiridion.wiki.gui.GuiLayers;
import joshie.enchiridion.wiki.gui.GuiLighting;
import joshie.enchiridion.wiki.gui.GuiMain;
import joshie.enchiridion.wiki.gui.GuiMenu;
import joshie.enchiridion.wiki.gui.GuiMode;
import joshie.enchiridion.wiki.gui.GuiScrollbarMenu;
import joshie.enchiridion.wiki.gui.GuiScrollbarPage;
import joshie.enchiridion.wiki.gui.GuiSearch;
import joshie.enchiridion.wiki.gui.GuiSidebar;
import joshie.enchiridion.wiki.gui.GuiTabs;
import joshie.enchiridion.wiki.gui.GuiTextEdit;
import joshie.enchiridion.wiki.mode.SaveMode;
import joshie.enchiridion.wiki.mode.edit.ConfirmAddition;
import joshie.enchiridion.wiki.mode.edit.ConfirmDeletion;
import joshie.enchiridion.wiki.mode.edit.ConfirmLocking;
import joshie.enchiridion.wiki.mode.edit.PageEditAddition;
import joshie.enchiridion.wiki.mode.edit.PageEditLink;
import joshie.lib.helpers.StackHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WikiHelper {
    public static GuiMain gui = new GuiMain();
    public static Gson gson = null;
    private static boolean EDIT_MODE;

    public static boolean lighting;
    public static int theLeft;
    public static int theTop;
    public static int theScrolled;
    public static int mouseX;
    public static int mouseY;
    public static int height;

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation();
            builder.registerTypeAdapter(Element.class, new WikiAbstractAdapter());
            gson = builder.create();
        }

        return gson;
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
    
    public static void drawScaledSplitText(float scale, String text, int x, int y, int color, int length) {
        gui.drawScaledSplitText(gui.mc, scale, text, x, y, color, length);
    }

    public static void drawTexture(int x, int y, int xStart, int yStart, int xEnd, int yEnd) {
        gui.drawTexturedModalRect(x, y, xStart, yStart, xEnd, yEnd);
    }

    public static void clearEditGUIs() {
        GuiLayers.clear();
        GuiItemSelect.clear();
        GuiMenu.clear();
        GuiTextEdit.clear();
        GuiColorEdit.clear();
    }

    public static void fixLighting() {
        lighting = !lighting;

        if (lighting) {
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

    public static void updateGUI() {
        theLeft = (int) (gui.mc.displayWidth / 2D) - 512;
        theTop = 150;
        theScrolled = theTop + getPage().getData().getScroll();
        height = gui.mc.displayHeight;
    }

    public static boolean isEditMode() {
        return gui.page.isEditMode();
    }

    public static void setPage(String mod, String tab, String cat, String page) {
        gui.setPage(mod, tab, cat, page);
    }

    public static ArrayList<GuiExtension> wiki = new ArrayList();
    public static ArrayList<GuiExtension> library = new ArrayList();
    private static ArrayList<GuiExtension> selected;
    
    public static ArrayList<GuiExtension> getGui() {
        return selected;
    }
    
    /** Switches from the wiki gui to the library gui and vice versa **/
    public static void switchGui(IWikiMode mode, ArrayList<GuiExtension> list) {
        if(!selected.equals(mode)) {
            //If the previous mode was Save, then save it
            if(gui.mode.equals(SaveMode.getInstance())) {
                SaveMode.getInstance().markDirty();
            }
            
            //Otherwise switch the gui over to the new mode
            gui.setMode(mode);
            
            //Set the selected to this list
            selected = list;
        }
    }
    
    public static void switchGui(ArrayList<GuiExtension> list) {
        switchGui(gui.mode, list);
    }
    
    public static void init() {
        wiki = new ArrayList();
        wiki.add(new GuiBackground());
        wiki.add(new GuiSidebar());
        wiki.add(new GuiLighting());
        wiki.add(new GuiCanvas());
        wiki.add(new GuiTabs());
        wiki.add(new GuiItemSelect());
        wiki.add(new GuiLighting());
        wiki.add(new GuiColorEdit());
        wiki.add(new GuiMenu());
        wiki.add(new GuiScrollbarMenu());
        wiki.add(new GuiScrollbarPage());
        wiki.add(new GuiLayers());
        wiki.add(new GuiTextEdit());
        wiki.add(new GuiSearch());
        wiki.add(new GuiMode());
        wiki.add(new GuiHistory());
        wiki.add(new ConfirmDeletion());
        wiki.add(new ConfirmLocking());
        wiki.add(new ConfirmAddition());
        wiki.add(new PageEditAddition());
        wiki.add(new PageEditLink());

        library = new ArrayList();
        library.add(new GuiBackground());
        library.add(new GuiLighting());
        library.add(new GuiLibrary());
        library.add(new GuiShelves());
        library.add(new GuiLighting());
        library.add(new GuiMode());
        
        if(selected == null) {
            selected = wiki;
        }
    }
    
    public static GuiExtension getInstance(Class extension) {
        for(GuiExtension e: getGui()) {
            if(e.getClass().equals(extension)) {
                return e;
            }
        }
        
        return null;
    }
    
    public static void setVisibility(Class extension, boolean isVisible) {
    	for(GuiExtension e: getGui()) {
    		if(e.getClass().equals(extension)) {
    			e.setVisibility(isVisible);
    			break;
    		}
    	}
    }
    
    public static boolean isLibrary() {
        return getGui().equals(library);
    }

    public static WikiMod getMod() {
        return gui.mod;
    }

    public static int getIntFromMouse(int x1, int x2, int y1, int y2, int int1, int int2) {
        if (mouseX >= x1 && mouseX <= x2) {
            if (mouseY >= y1 && mouseY <= y2) {
                return int2;
            }
        }

        return int1;
    }

    public static void drawScaledTexture(ResourceLocation texture, int x, int y, int xStart, int yStart, int xEnd, int yEnd, float scale) {
        start();
        enable(GL_BLEND);
        bindTexture(texture);
        scale(scale);
        drawTexture(getScaledX(x, scale), getScaledY(y, scale), xStart, yStart, xEnd, yEnd);
        disable(GL_BLEND);
        end();
    }

    public static void drawScaledStack(ItemStack stack, int x, int y, float scale) {
        start();
        scale(scale);
        renderStack(stack, getScaledX(x, scale), getScaledY(y, scale));
        end();
    }

    public static void renderStack(ItemStack stack, int x, int y) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ElementItem.itemRenderer.renderItemAndEffectIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, x, y);
        ElementItem.itemRenderer.renderItemOverlayIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), stack, x, y);
        GL11.glDisable(GL11.GL_LIGHTING);        
    }

    public static int getScaledX(int x) {
        return getScaledX(x, 1.0F);
    }

    public static int getScaledX(int x, float scale) {
        return (int) ((WikiHelper.theLeft + x) / scale);
    }

    public static int getScaledY(int y) {
        return getScaledY(y, 1.0F);
    }

    public static int getScaledY(int y, float scale) {
        return ((int) ((WikiHelper.theTop + y) / scale));
    }

    public static void setTab(WikiTab tab) {
        if (!isTabSelected(tab)) {
            gui.tab = tab;
            gui.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }

    public static boolean isTabSelected(WikiTab tab) {
        return tab == gui.tab;
    }

    public static void setPage(WikiPage page) {
        gui.setPage(page);
    }

    public static void setMod(WikiMod mod) {
        gui.mod = mod;
    }

    public static void delete() {
        WikiData.instance().removePage(gui.page);
    }
}
