package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.scale;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.EInfo;
import joshie.enchiridion.api.IWikiMode;
import joshie.enchiridion.api.IWikiMode.WikiMode;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.gui.buttons.ButtonBase;
import joshie.enchiridion.wiki.gui.popups.PageEditLink;
import joshie.enchiridion.wiki.gui.popups.PageEditResource;
import joshie.enchiridion.wiki.mode.DisplayMode;
import joshie.enchiridion.wiki.mode.SaveMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiMain extends GuiScalable {
    public static final ResourceLocation texture = new ResourceLocation(EInfo.MODPATH, "textures/gui/core.png");
    public static int button_id;

    /** The current page of the wiki we are on **/
    public static List<ButtonBase> buttons = null;
    public static ButtonBase selected = null;
    /** The current mod we are viewing **/
    public static WikiMod mod = null;
    /** The current tab we are viewing **/
    public static WikiTab tab = null;
    /** The current page that we are viewing **/
    public static WikiPage page = null;
    public static IWikiMode mode = DisplayMode.getInstance();

    public void loadPage(String mod, String tab, String cat, String page) {
        GuiHistory.newPage(WikiRegistry.instance().getPage(mod, tab, cat, page));
    }

    public static void setMode(IWikiMode mode) {
        GuiMain.mode = mode;
        buttons = mode.addButtons(new ArrayList());
        GuiMain.mode.onSwitch();
    }

    public static void setPage(WikiPage page) {
        if (mode.getType() == WikiMode.DISPLAY) {
            if (page.getData().canEdit()) {
                if (EConfig.EDIT_ENABLED) {
                    setMode(SaveMode.getInstance());
                } else setMode(DisplayMode.getInstance());
            } else setMode(DisplayMode.getInstance());
        } else if (!page.getData().canEdit()) { //If the new page cannot be edited, automatically save everything
            setMode(SaveMode.getInstance()); //Now that the page has been saved
            setPage(page); //Go through this again as a display mode page
        }

        GuiMain.page = page;

        //Clear the resource editing and page editing
        ((PageEditResource) (WikiHelper.getInstance(PageEditResource.class))).setEditing(null);
        ((PageEditLink) (WikiHelper.getInstance(PageEditLink.class))).setEditing(null);
    }

    @Override
    public void initGui() {
        super.initGui();
        WikiHelper.init();

        setMode(DisplayMode.getInstance());
        if (page == null) {
            page = WikiRegistry.instance().getPage("Enchiridion 2", "Enchiridion 2", "Enchiridion 2", "About");
            tab = WikiRegistry.instance().getTab("Enchiridion 2", "Enchiridion 2");
            mod = WikiRegistry.instance().getMod("Enchiridion 2");
            loadPage("Enchiridion 2", "Enchiridion 2", "Enchiridion 2", "About");
        }

        page.getData().refreshY();
        setPage(page);

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void drawScaledText(Minecraft mc, float scale, String text, int x, int y, int color) {
        start();
        scale(scale);
        EClientProxy.font.drawString(text, getLeft(scale, x), getTop(scale, y), color);
        end();
    }

    public void drawScaledSplitText(Minecraft mc, float scale, String text, int x, int y, int color, int length) {
        start();
        scale(scale);
        EClientProxy.font.drawSplitString(text, getLeft(scale, x), getTop(scale, y), length, color);
        end();
    }

    private void drawCentredText(Minecraft mc, String text, int x, int y) {
        drawScaledCentredText(mc, 1.0F, text, x, y, 0xFFFFFF);
    }

    public void drawScaledCentredText(Minecraft mc, float scale, String text, int x, int y, int color) {
        start();
        scale(scale);
        EClientProxy.font.drawString(text, getLeft(scale, x) - EClientProxy.font.getStringWidth(text) / 2, getTop(scale, y), color);
        end();
    }

    @Override
    public void drawScreen(int x, int y, float tick) {
        start();
        float scaled = 1.0F / resolution.getScaleFactor();
        if (resolution.getScaleFactor() < 3) {
            scaleZ(scaled, 0.15F);
        } else {
            scaleZ(scaled, 0.1325F);
        }

        theLeft = getLeft();

        WikiHelper.updateGUI();
        for (GuiExtension element : WikiHelper.getGui()) {
            if (element.isVisible()) {
                element.draw();
            }
        }

        if (page.getSelected() != null) {
            page.getSelected().whileSelected();
        }

        for (int k = 0; k < buttons.size(); ++k) {
            ((GuiButton) buttons.get(k)).drawButton(this.mc, x, y);
        }

        end();
    }

    @Override
    public boolean clickedButton(int x, int y, int button) {
        if (button == 0) {
            for (ButtonBase b : buttons) {
                if (b.mousePressed(mc, x, y)) {
                    b.onClicked();
                    b.func_146113_a(mc.getSoundHandler());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean releasedButton(int x, int y, int button) {
        if (selected != null && button == 0) {
            selected.mouseReleased(x, y);
            selected = null;
            return true;
        }

        return false;
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (!clickedButton(x, y, button)) {
            for (GuiExtension element : WikiHelper.getGui()) {
                if (element.isVisible()) {
                    element.clicked(button);
                }
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        if (!releasedButton(x, y, button)) {
            for (GuiExtension element : WikiHelper.getGui()) {
                if (element.isVisible()) {
                    element.release(button);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        page.keyTyped(character, key);
        for (GuiExtension element : WikiHelper.getGui()) {
            if (element.isVisible()) {
                element.keyTyped(character, key);
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        WikiHelper.updateMouse();
        int wheel = Mouse.getDWheel();
        for (GuiExtension element : WikiHelper.getGui()) {
            if (element.isVisible()) {
                if (wheel != 0) {
                    element.scroll(wheel < 0);
                }

                element.follow();
            }
        }
    }

    public void setZLevel(int i) {
        zLevel = 100 + i;
    }
}
