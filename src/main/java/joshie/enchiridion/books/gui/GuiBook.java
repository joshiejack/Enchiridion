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
import joshie.enchiridion.api.IBook;
import joshie.enchiridion.api.IBookEditorOverlay;
import joshie.enchiridion.api.IBookHelper;
import joshie.enchiridion.api.IDrawHelper;
import joshie.enchiridion.api.IFeatureProvider;
import joshie.enchiridion.api.IItemStack;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.lib.PenguinFontRenderer;
import joshie.lib.editables.TextEditor;
import joshie.lib.helpers.ClientStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBook extends GuiScreen implements IDrawHelper, IBookHelper {
	private static final ResourceLocation legacyCoverL = new ResourceLocation("enchiridion", "textures/books/guide_cover_left.png");
    private static final ResourceLocation legacyCoverR = new ResourceLocation("enchiridion", "textures/books/guide_cover_right.png");
	private static final ResourceLocation legacyLeft = new ResourceLocation("enchiridion", "textures/books/guide_page_left.png");
    private static final ResourceLocation legacyRight = new ResourceLocation("enchiridion", "textures/books/guide_page_right.png");
	public static final GuiBook INSTANCE = new GuiBook();
	public HashMap<String, Integer> pageCache = new HashMap();
	public List<String> tooltip = new ArrayList();
	private Set<IBookEditorOverlay> overlays = new HashSet();
	private boolean isEditMode = false; // Whether we are in edit mode or not
	private IBook book; // The current book being displayed
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
	
	public void registerOverlay(IBookEditorOverlay overlay) {
		overlays.add(overlay);
	}

	@Override
	public void drawScreen(int x2, int y2, float partialTicks) {	
		x = (width - 430) / 2;
		y = (height - ySize) / 2;
		tooltip.clear();
		
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
			} else EnchiridionAPI.draw.drawImage(book.getResource(), book.getBackgroundStartX(), book.getBackgroundStartY(), book.getBackgroundEndX(), book.getBackgroundEndY());
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
		TextEditor.INSTANCE.clearEditable();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if (!book.doesBookForgetClose() && page != null) pageCache.put(book.getUniqueName(), page.getPageNumber());
		if (isEditMode) {
			if (selected != null) selected.deselect();
			
			try {
				File directory = new File(Enchiridion.root, "books");
		        if (!directory.exists() && !directory.mkdirs()) {
		            throw new IllegalStateException("Couldn't create dir: " + directory);
		        }
		        
		        String saveName = book.getSaveName() == null ? book.getUniqueName(): book.getSaveName();
		        book.setMadeIn189(); //Force it to a mc189book with new formatting
		        
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
	
	//For use with recipe handlers
	private int renderX;
	private int renderY;
	private double renderWidth;
	private double renderHeight;
	private float renderSize;
	
	@Override
	public void setRenderData(int xPos, int yPos, double width, double height, float size) {
		renderX = xPos;
		renderY = yPos;
		renderWidth = width;
		renderHeight = height;
		renderSize = size;
	}
	
	private int getLeft(double x) {
        return (int) (renderX + ((x / 150D) * renderWidth));
    }
    
    private int getTop(double y) {       
        return (int) (renderY + ((y / 100D) * renderHeight));
    }
	
	@Override
	public boolean isMouseOver(IItemStack stack) {
		if (stack == null || stack.getItemStack() == null) return false;
        int left = getLeft(stack.getX());
        int top = getTop(stack.getY());
        int scaled = (int) (16 * stack.getScale() * renderSize);
        int right = left + scaled;
        int bottom = top + scaled; 
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
	}
	
	@Override
    public void drawIItemStack(IItemStack stack) {
		stack.onDisplayTick(); //Update the display ticker
        drawStack(stack.getItemStack(), getLeft(stack.getX()), getTop(stack.getY()), renderSize * stack.getScale());
    }
	
	@Override
	 public void drawTexturedRectangle(double left, double top, int u, int v, int w, int h, float scale) {
		float size = renderSize * scale;
		int x2 = (int) Math.floor(((x + getLeft(left)) / size));
        int y2 = (int) Math.floor( ((y + getTop(top)) / size));
        
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableAlpha();
        GlStateManager.scale(size, size, 1.0F);
        drawTexturedModalRect(x2, y2, u, v, w, h);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	 }	
	
	 @Override
	 public void drawTexturedReversedRectangle(double left, double top, int u, int v, int w, int h, float scale) {
		 float size = renderSize * scale;
		 int x2 = (int) Math.floor(((x + getLeft(left)) / size)) - w;
	     int y2 = (int) Math.floor( ((y + getTop(top)) / size)) - h;
	        
	     GlStateManager.pushMatrix();
	     GlStateManager.enableBlend();
	     GlStateManager.color(1F, 1F, 1F, 1F);
	     GlStateManager.enableAlpha();
	     GlStateManager.scale(size, size, 1.0F);
	     drawTexturedModalRect(x2, y2, u, v, w, h);
	     GlStateManager.disableBlend();
	     GlStateManager.popMatrix();
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
	
	@Override //From vanilla, switching to my font renderer though
	protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font)
    {
        if (!textLines.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines)
            {
                int j = PenguinFontRenderer.INSTANCE.getStringWidth(s);

                if (j > i)
                {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1)
            {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.width)
            {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > this.height)
            {
                i2 = this.height - k - 6;
            }

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            int l = 0xCC312921;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 0xFF191511;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1)
            {
                String s1 = (String)textLines.get(k1);
                PenguinFontRenderer.INSTANCE.drawStringWithShadow(s1, (float)l1, (float)i2, -1);

                if (k1 == 0)
                {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}
