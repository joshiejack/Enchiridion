package joshie.enchiridion.gui.book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IBookHelper;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.helpers.DefaultHelper;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.lib.editables.TextEditor;
import joshie.lib.helpers.ClientHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiBook extends GuiBase implements IBookHelper {
    private static final ResourceLocation legacyCoverL = new ResourceLocation("enchiridion", "textures/books/guide_cover_left.png");
    private static final ResourceLocation legacyCoverR = new ResourceLocation("enchiridion", "textures/books/guide_cover_right.png");
    private static final ResourceLocation legacyLeft = new ResourceLocation("enchiridion", "textures/books/guide_page_left.png");
    private static final ResourceLocation legacyRight = new ResourceLocation("enchiridion", "textures/books/guide_page_right.png");
    public static final GuiBook INSTANCE = new GuiBook(); //Instane of this book

    //Page Number Cache
    public HashMap<String, Integer> pageCache = new HashMap();
    private Set<IBookEditorOverlay> overlays = new HashSet();
    private boolean isEditMode = false; // Whether we are in edit mode or not
    private IBook book; // The current book being displayed
    private IPage page; // The current page being displayed
    private IFeatureProvider selected; //Currently selected feature
    private Set<IFeatureProvider> group = new HashSet(); //Groups?
    private Set<IFeatureProvider> clipboard = new HashSet(); //Clipboard
    private float red, green, blue; //Colour to render the book
    private boolean isGroupMoveMode = false;

    protected GuiBook() {}

    public void registerOverlay(IBookEditorOverlay overlay) {
        overlays.add(overlay);
    }

    @Override
    public void drawScreen(int x2, int y2, float partialTicks) {
        super.drawScreen(x2, y2, partialTicks);

        if (book.isBackgroundVisible()) {
            //Display the left side
            if (book.isBackgroundLegacy()) {
                GlStateManager.color(red, green, blue);
                mc.getTextureManager().bindTexture(legacyCoverL);
                drawTexturedModalRect(x - 9, y, 35, 0, 212 + 9, ySize);
                GlStateManager.color(1F, 1F, 1F);
                mc.getTextureManager().bindTexture(legacyLeft);
                drawTexturedModalRect(x, y, 44, 0, 212, ySize);

                //Display the right side
                GlStateManager.color(red, green, blue);
                mc.getTextureManager().bindTexture(legacyCoverR);
                drawTexturedModalRect(x + 212, y, 0, 0, 218 + 9, ySize);
                GlStateManager.color(1F, 1F, 1F);
                mc.getTextureManager().bindTexture(legacyRight);
                drawTexturedModalRect(x + 212, y, 0, 0, 218, ySize);
            } else EnchiridionAPI.draw.drawImage(book.getBackgroundResource(), book.getBackgroundStartX(), book.getBackgroundStartY(), book.getBackgroundEndX(), book.getBackgroundEndY());
        }

        // Draw all the features, In reverse
        for (IFeatureProvider feature : Lists.reverse(page.getFeatures())) {
            feature.draw(mouseX, mouseY);
            feature.addTooltip(tooltip, mouseX, mouseY);
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        }

        //Draw all the overlays
        if (isEditMode) {
            for (IBookEditorOverlay overlay : overlays) {
                overlay.draw(mouseX, mouseY);
                overlay.addToolTip(tooltip, mouseX, mouseY);
            }
        }

        drawHoveringText(tooltip, x2, y2, mc.fontRendererObj);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        GuiSimpleEditor.INSTANCE.setEditor(null); //Reset the editor
        TextEditor.INSTANCE.clearEditable();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (!book.doesBookForgetClose() && page != null) pageCache.put(book.getUniqueName(), page.getPageNumber());
        if (isEditMode) {
            if (selected != null) selected.deselect();

            try {
                book.setMadeIn189(); //Force it to a mc189book with new formatting
                File toSave = FileHelper.getSaveJSONForBook(book);
                Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
                writer.write(GsonHelper.getModifiedGson().toJson(book));
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        if (!Keyboard.areRepeatEventsEnabled()) {
            Keyboard.enableRepeatEvents(true);
        }

        super.keyTyped(character, key);

        if (isEditMode) {
            for (IFeatureProvider g : group) {
                if (g.keyTyped(character, key)) {
                    page.removeFeature(g);
                    group = new HashSet();
                }
            }
            
            //Copy to clipboard
            if (ClientHelper.isCtrlPressed() && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                clipboard.clear();
                for (IFeatureProvider provider: group) {
                    IFeatureProvider copy = provider.copy();
                    copy.getFeature().update(copy);
                    clipboard.add(copy);
                }
            } else if (ClientHelper.isCtrlPressed() && Keyboard.isKeyDown(Keyboard.KEY_V)) { //Paste features
                for (IFeatureProvider provider: clipboard) {
                    page.addFeature(provider.getFeature().copy(), provider.getX(), provider.getY(), provider.getWidth(), provider.getHeight(), provider.isLocked(), !provider.isVisible());
                }
            }

            TextEditor.INSTANCE.keyTyped(character, key);
            for (IBookEditorOverlay overlay : overlays) {
                overlay.keyTyped(character, key);
            }
        }
    }

    private transient boolean wasControlPressedBefore = false;

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        //Perform clicks for the overlays
        if (isEditMode) {
            for (IBookEditorOverlay overlay : overlays) {
                if (overlay.mouseClicked(mouseX, mouseY)) {
                    return;
                }
            }
        }

        //Perform clicks for the features
        for (IFeatureProvider feature : page.getFeatures()) {
            if (feature.mouseClicked(mouseX, mouseY) && isEditMode) {
                if (selected != null) selected.deselect();
                selected = feature;
                selected.select(mouseX, mouseY);
                boolean isCtrlPressedNow = ClientHelper.isCtrlPressed();
                //If we didn't have control before and we do now
                if ((!wasControlPressedBefore && isCtrlPressedNow) || isCtrlPressedNow) {
                    wasControlPressedBefore = true; //It has now been pressed
                    isGroupMoveMode = false;
                } else if (wasControlPressedBefore && !isCtrlPressedNow) { //Now if it was pressed before but isn't now
                    wasControlPressedBefore = false; //Reset the info
                    isGroupMoveMode = true;
                } else if (!group.contains(selected)) { //If the control wasn't pressed before trying to move this item, then clear the group
                    group = new HashSet();
                    isGroupMoveMode = false;
                }

                group.add(selected); //Add it to the group
                for (IFeatureProvider provider : group) { //Refresh the x position
                    provider.select(mouseX, mouseY);
                }

                return;
            }
        }

        //If nothing was clicked on, remove the current selection
        if (selected != null) selected.deselect();
        selected = null;
        for (IFeatureProvider g : group) {
            g.deselect();
        }

        group = new HashSet();
    }

    @Override
    public void mouseReleased(int x, int y, int state) {
        super.mouseReleased(x, y, state);

        isGroupMoveMode = false;
        for (IFeatureProvider provider : group) {
            provider.mouseReleased(mouseX, mouseY);
        }

        //Perform releases for the overlays
        if (isEditMode) {
            for (IBookEditorOverlay overlay : overlays) {
                overlay.mouseReleased(mouseX, mouseY);
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        for (IFeatureProvider provider : group) {
            provider.follow(mouseX, mouseY, isGroupMoveMode);
        }

        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            boolean down = wheel < 0;
            if (isEditMode) {
                for (IBookEditorOverlay overlay : overlays) {
                    overlay.scroll(down, mouseX, mouseY);
                }
            }
        }
    }

    // Helper methods
    @Override //Getters
    public boolean isEditMode() {
        return isEditMode;
    }

    @Override
    public IBook getBook() {
        return book;
    }

    @Override
    public IPage getPage() {
        return page;
    }

    @Override
    public IFeatureProvider getSelected() {
        return selected;
    }

    //Setters
    @Override
    public IBookHelper setBook(IBook book, boolean playerSneaked) {
        this.book = book; //Set the book
        try {
            int color = book.getColorAsInt();
            red = (color >> 16 & 255) / 255.0F;
            green = (color >> 8 & 255) / 255.0F;
            blue = (color & 255) / 255.0F;
        } catch (Exception e) {}

        //If the config allows editing, and the book isn't locked, enable edit mode
        if (EConfig.enableEditing && !book.isLocked() && playerSneaked) {
            isEditMode = true;
        } else isEditMode = false;

        Integer number = pageCache.get(book.getUniqueName());
        if (number == null) number = book.getDefaultPage();
        JumpHelper.jumpToPageByNumber(number);
        if (page == null) { //If we've got a dumb book, without a good page, then let's create a new blank page
            page = DefaultHelper.addDefaults(new Page(0));
            book.addPage(page);
        }

        return this;
    }

    @Override
    public void setPage(IPage page) {
        GuiSimpleEditor.INSTANCE.setEditor(null); //Reset the editor
        TextEditor.INSTANCE.clearEditable();
        this.page = page;
    }

    @Override
    public void setSelected(IFeatureProvider provider) {
        this.selected = provider;
    }
}
