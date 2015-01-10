package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;

import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.Configuration;
import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.gui.GuiScalable;
import joshie.enchiridion.lib.EnchiridionInfo;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.WikiTab;
import joshie.enchiridion.wiki.mode.ButtonBase;
import joshie.enchiridion.wiki.mode.DisplayMode;
import joshie.enchiridion.wiki.mode.SaveMode;
import joshie.enchiridion.wiki.mode.WikiMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiMain extends GuiScalable {
    public static final ResourceLocation texture = new ResourceLocation(EnchiridionInfo.MODPATH, "textures/gui/core.png");
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
    public static WikiMode mode = null;

    public void setPage(String mod, String tab, String cat, String page) {
        this.page = WikiRegistry.instance().getPage(mod, tab, cat, page);
    }

    public void setMode(WikiMode mode) {
        this.mode = mode;
        buttons = mode.addButtons(new ArrayList());
        this.mode.onSwitch();
    }

    @Override
    public void initGui() {
        super.initGui();
        WikiHelper.init();
        if (page == null) {
            page = WikiRegistry.instance().getPage("Enchiridion", "Default", "Basics", "Menu");
            tab = WikiRegistry.instance().getTab("Enchiridion", "Default");
            mod = WikiRegistry.instance().getMod("Enchiridion");
        }

        if (mode == null) {
            if (Configuration.EDIT_ENABLED) {
                setMode(SaveMode.getInstance());
            } else setMode(DisplayMode.getInstance());
        }
        
        page.getData().refreshY();

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void drawText(Minecraft mc, String text, int x, int y) {
        drawScaledText(mc, 1.0F, text, x, y, 0xFFFFFF);
    }

    public void drawScaledText(Minecraft mc, float scale, String text, int x, int y, int color) {
        glPushMatrix();
        glScalef(scale, scale, 1.0F);
        EClientProxy.font.drawString(text, getLeft(scale, x), getTop(scale, y), color);
        glPopMatrix();
    }

    private void drawCentredText(Minecraft mc, String text, int x, int y) {
        drawScaledCentredText(mc, 1.0F, text, x, y, 0xFFFFFF);
    }

    public void drawScaledCentredText(Minecraft mc, float scale, String text, int x, int y, int color) {
        glPushMatrix();
        glScalef(scale, scale, 1.0F);
        EClientProxy.font.drawString(text, getLeft(scale, x) - EClientProxy.font.getStringWidth(text) / 2, getTop(scale, y), color);
        glPopMatrix();
    }

    protected void drawScaledText(Minecraft mc, float scale, String text, int x, int y) {
        drawScaledText(mc, scale, text, x, y, 0xFFFFFF);
    }

    @Override
    public void drawScreen(int x, int y, float tick) {
        glPushMatrix();
        float scaled = 1.0F / resolution.getScaleFactor();
        if(resolution.getScaleFactor() < 3) {
            glScalef(scaled, scaled, 0.15F);
        } else {
            glScalef(scaled, scaled, 0.1325F);
        }
        
        theLeft = getLeft();

        WikiHelper.updateGUI();
        for (GuiExtension element : WikiHelper.getGui()) {
            element.draw();
        }

        if (page.getSelected() != null) {
            page.getSelected().whileSelected();
        }

        for (int k = 0; k < buttons.size(); ++k) {
            ((GuiButton) buttons.get(k)).drawButton(this.mc, x, y);
        }

        glPopMatrix();
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
                element.clicked(button);
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        if (!releasedButton(x, y, button)) {
            for (GuiExtension element : WikiHelper.getGui()) {
                element.release(button);
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        page.keyTyped(character, key);
        for (GuiExtension element : WikiHelper.getGui()) {
            element.keyTyped(character, key);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        WikiHelper.updateMouse();
        int wheel = Mouse.getDWheel();
        for (GuiExtension element : WikiHelper.getGui()) {
            if(wheel != 0) {
                element.scroll(wheel < 0);
            }
            
            element.follow();
        }
    }

    public void setZLevel(int i) {
        zLevel = 100 + i;
    }
}
