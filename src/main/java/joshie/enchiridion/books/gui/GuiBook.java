package joshie.enchiridion.books.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IBookEditorOverlay;
import joshie.enchiridion.api.IBookHelper;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.Book;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.lib.PenguinFontRenderer;
import joshie.lib.editables.TextEditor;
import joshie.lib.helpers.ClientStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBook extends GuiScreen implements IBookHelper {
	private static final ResourceLocation legacyCoverL = new ResourceLocation("enchiridion", "textures/books/guide_cover_left.png");
    private static final ResourceLocation legacyCoverR = new ResourceLocation("enchiridion", "textures/books/guide_cover_right.png");
	private static final ResourceLocation legacyLeft = new ResourceLocation("enchiridion", "textures/books/guide_page_left.png");
    private static final ResourceLocation legacyRight = new ResourceLocation("enchiridion", "textures/books/guide_page_right.png");
	public static final GuiBook INSTANCE = new GuiBook();
	public HashMap<String, Integer> pageCache = new HashMap();
	public List<String> tooltip = new ArrayList();
	private Set<IBookEditorOverlay> overlays = new HashSet();
	private boolean isEditMode = false; // Whether we are in edit mode or not
	private Book book; // The current book being displayed
	private IPage page; // The current page being displayed
	private IFeatureProvider selected; //Currently selected feature
	public int mouseX = 0;
	public int mouseY = 0;
	public int x;
	public int y;
	protected int xSize = 430;
	protected int ySize = 217;
	private float red, green, blue;

	private GuiBook() {}
	public GuiBook setBook(Book book, boolean playerSneaked) {
        this.book = book; //Set the book
        int color = book.color;
        if (book.colorHex != null && !book.colorHex.equals("")) {
        	try {
        		color = (int) Long.parseLong(book.colorHex, 16);
        	} catch (Exception e) {}
        }
        
        red = (color >> 16 & 255) / 255.0F;
        green = (color >> 8 & 255) / 255.0F;
        blue = (color & 255) / 255.0F;
        
        //If the config allows editing, and the book isn't locked, enable edit mode
        if (EConfig.enableEditing && !book.isLocked && playerSneaked) {
            isEditMode = true;
        } else isEditMode = false;

        Integer number = pageCache.get(book.uniqueName);
        if (number == null) number = book.defaultPage;
        JumpHelper.jumpToPageByNumber(number);
        return this;
    }
	
	public void registerOverlay(IBookEditorOverlay overlay) {
		overlays.add(overlay);
	}

	@Override
	public void drawScreen(int x2, int y2, float partialTicks) {	
		x = (width - 430) / 2;
		y = (height - ySize) / 2;
		tooltip.clear();
		
		if (book.showBackground) {
			//Display the left side
			if (book.legacyTexture) {
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
			} else EnchiridionAPI.draw.drawImage(book.getResource(), book.backgroundStartX, book.backgroundStartY, book.backgroundEndX, book.backgroundEndY);
		}
				
		// Draw all the features, In reverse
		for (IFeatureProvider feature : Lists.reverse(page.getFeatures())) {
			feature.draw(mouseX, mouseY);
			feature.addTooltip(tooltip, mouseX, mouseY);
			GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		}
		
		//Draw all the overlays
		if(isEditMode) {
			for (IBookEditorOverlay overlay: overlays) {
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
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if (!book.forgetPageOnClose && page != null) pageCache.put(book.uniqueName, page.getPageNumber());
		if (isEditMode) {
			if (selected != null) selected.deselect();
			
			try {
				File directory = new File(Enchiridion.root, "books");
		        if (!directory.exists() && !directory.mkdirs()) {
		            throw new IllegalStateException("Couldn't create dir: " + directory);
		        }
		        
		        String saveName = book.saveName == null ? book.uniqueName: book.saveName;
		        book.mc189book = true; //Force it to a mc189book with new formatting
		        
		        File toSave = new File(directory, saveName + ".json");
		        Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
	            writer.write(GsonHelper.getModifiedGson().toJson(book));
	            writer.close();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}

	@Override
	protected void keyTyped(char character, int key) throws IOException {
		if (!Keyboard.areRepeatEventsEnabled()) {
			Keyboard.enableRepeatEvents(true);
		}
		
		super.keyTyped(character, key);
		if (selected != null) {
			if (selected.keyTyped(character, key)) {
				page.removeFeature(selected);
				selected = null;
			}
		}
		
		if(isEditMode) {
			TextEditor.INSTANCE.keyTyped(character, key);
			for (IBookEditorOverlay overlay: overlays) {
				overlay.keyTyped(character, key);
			}
		}
	}
	
	public void select(IFeatureProvider provider) {
		
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
		super.mouseClicked(x, y, mouseButton);				
		//Perform clicks for the overlays
		if(isEditMode) {
			for (IBookEditorOverlay overlay: overlays) {
				if(overlay.mouseClicked(mouseX, mouseY)) {
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
				return;
			}
		}
		
		//If nothing was clicked on, remove the current selection
		if (selected != null) selected.deselect();
		selected = null;
	}

	@Override
	public void mouseReleased(int x, int y, int state) {
		super.mouseReleased(x, y, state);
		
		if (selected != null) {
			selected.mouseReleased(mouseX, mouseY);
		}
		
		//Perform releases for the overlays
		if(isEditMode) {
			for (IBookEditorOverlay overlay: overlays) {
				overlay.mouseReleased(mouseX, mouseY);
			}
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		int x = Mouse.getEventX() * width / mc.displayWidth;
		int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

		mouseX = x - (width - xSize) / 2;
		mouseY = y - (height - ySize) / 2;
		
		
		
		if (selected != null) {
			selected.follow(mouseX, mouseY);
		}
		
		int wheel = Mouse.getDWheel();
		if (wheel != 0) {
			boolean down = wheel < 0;
			if (isEditMode) {
				for (IBookEditorOverlay overlay: overlays) {
					overlay.scroll(down, mouseX, mouseY);
				}
			}
			
			if (selected != null) {
				selected.scroll(down);
			}
		}

		super.handleMouseInput();
	}
	
	@Override
	public void setPage(IPage page) {
		GuiSimpleEditor.INSTANCE.setEditor(null); //Reset the editor
		this.page = page;
	}

	// Helper methods
	@Override
	public boolean isEditMode() {
		return isEditMode;
	}
	
	@Override
	public IPage getPage() {
		return page;
	}
	
	@Override
	public String getBookID() {
		return book.uniqueName;
	}
	
	@Override
	public IFeatureProvider getSelectedFeature() {
		return selected;
	}
	
	@Override
	public String getBookSaveName() {
		return book.saveName;
	}
	
	@Override
	public List<IPage> getBookPages() {
		return book.book;
	}
	
	@Override
	public void setSelected(IFeatureProvider provider) {
		this.selected = provider;
	}
	
	@Override
	public void drawSplitScaledString(String text, int xPos, int yPos, int wrap, int color, float scale) {
		GlStateManager.pushMatrix();
    	GlStateManager.scale(scale, scale, scale);
    	PenguinFontRenderer.INSTANCE.drawSplitString(text, (int) ((x + xPos) / scale), (int) ((y + yPos) / scale), wrap, color);
    	GlStateManager.popMatrix();
	}
	
	@Override
	public void drawRectangle(int left, int top, int right, int bottom, int colorI) {
		drawRect(x + left, y + top, x + right, y + bottom, colorI);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void drawBorderedRectangle(int left, int top, int right, int bottom, int colorI, int colorB) {
		drawRect(x + left, y + top, x + right, y + bottom, colorI);
		drawRect(x + left, y + top, x + right, y + top + 1, colorB);
		drawRect(x + left, y + bottom - 1, x + right, y + bottom, colorB);
		drawRect(x + left, y + top, x + left + 1, y + bottom, colorB);
		drawRect(x + right - 1, y + top, x + right, y + bottom, colorB);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void drawStack(ItemStack stack, int left, int top, float size) {
        if (stack == null || stack.getItem() == null) return; //Don't draw stacks that don't exist
        int x2 = (int) Math.floor(((x + left) / size));
        int y2 = (int) Math.floor( ((y + top) / size));
        ClientStackHelper.drawStack(stack, x2, y2, size);
    }
	
	@Override
	public void drawResource(ResourceLocation resource, int left, int top, int width, int height, float scaleX, float scaleY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(resource);
        GlStateManager.scale(scaleX, scaleY, 1.0F);
        drawTexturedModalRect((int) ((x + left) / scaleX), (int) ((y + top) / scaleY), 0, 0, width, height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}
	
	@Override
	public void drawImage(ResourceLocation resource, int left, int top, int right, int bottom) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos((double)(x + left), (double)(y + bottom), (double)zLevel).tex(0, 1).color(1F, 1F, 1F, 1F).endVertex();
        worldrenderer.pos((double)(x + right), (double)(y + bottom), (double)zLevel).tex(1, 1).color(1F, 1F, 1F, 1F).endVertex();
        worldrenderer.pos((double)(x + right), (double)(y + top), (double)zLevel).tex(1, 0).color(1F, 1F, 1F, 1F).endVertex();
        worldrenderer.pos((double)(x + left), (double)(y + top), (double)zLevel).tex(0, 0).color(1F, 1F, 1F, 1F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
