package joshie.enchiridion.gui.book.features;

import com.google.common.collect.Lists;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.api.gui.ISimpleEditorFieldProvider;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiSimpleEditor;
import joshie.enchiridion.gui.book.GuiSimpleEditorGeneric;
import joshie.enchiridion.helpers.JumpHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class FeaturePreviewWindow extends FeatureAbstract implements ISimpleEditorFieldProvider {
    public int pageNumber;
    public transient IPage page;
    public transient IBook book;
    public transient IPage thisPage;
    public transient boolean isDragging;
    public transient int startY;

	public FeaturePreviewWindow() {}
	public FeaturePreviewWindow(int page) {
		this.pageNumber = page;
	}

    @Override
    public void onFieldsSet() {
        page = JumpHelper.getPageByNumber(book, pageNumber - 1);
    }

	@Override
	public FeaturePreviewWindow copy() {
	    return new FeaturePreviewWindow(pageNumber);
	}
	
	@Override
	public String getName() {
		return "Scroll: " + page;
	}

	@Override
	public void update(IFeatureProvider position) {
		super.update(position);
        thisPage = position.getPage();
        book = position.getPage().getBook();
        page = JumpHelper.getPageByNumber(book, pageNumber);
	}

    @Override
    public boolean getAndSetEditMode() {
        GuiSimpleEditor.INSTANCE.setEditor(GuiSimpleEditorGeneric.INSTANCE.setFeature(this));
        return false;
    }

    private boolean isOverScrollY(int yCheck, int x, int y) {
        return x >= position.getRight() - 10 && x <= position.getRight() && y >= position.getTop() + yCheck && y <= position.getTop() + yCheck + 10;
    }

    @Override
    public void performClick(int mouseX, int mouseY) {
        if (page != null && page != thisPage) {
            for (IFeatureProvider feature : Lists.reverse(page.getFeatures())) {
                if (feature instanceof FeaturePreviewWindow) continue; //No Cascading
                mouseY = GuiBook.INSTANCE.mouseY + page.getScroll();
                feature.mouseClicked(mouseX, mouseY, 0);
            }


            int scrollMax = page.getScrollbarMax(position.getBottom() - 5);
            int pos = (int)((page.getScroll() * (position.getHeight() - 10)) / scrollMax);
            if (isOverScrollY(pos, mouseX, GuiBook.INSTANCE.mouseY)) {
                isDragging = true;
                startY = GuiBook.INSTANCE.mouseY;
            }
        }
    }

    @Override
    public void performRelease(int mouseX, int mouseY) {
        if (isDragging) {
            isDragging = false;
        }
    }

    @Override
    public void draw(int xMouse, int yMouse) {
		if (GuiBook.INSTANCE.isEditMode()) {
    		EnchiridionAPI.draw.drawBorderedRectangle(position.getLeft(), position.getTop(), position.getRight(), position.getBottom(), 0x00000000, 0xFF48453C);
		}

        if (page != null && page != thisPage) {
            int scrollMax = page.getScrollbarMax(position.getBottom() - 5);
            if (isDragging) {
                if (startY != GuiBook.INSTANCE.mouseY) {
                    int scrollPosition = (int) (((GuiBook.INSTANCE.mouseY - position.getTop()) * (scrollMax)) / position.getHeight());
                    page.updateMaximumScroll(position.getBottom() -5); //Update the max
                    page.setScrollPosition(scrollPosition);
                }

                startY = GuiBook.INSTANCE.mouseY;
            }

            GlStateManager.pushMatrix();
            int scale = GuiBook.INSTANCE.getRes().getScaleFactor();
            GL11.glEnable(GL_SCISSOR_TEST);
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glScissor((GuiBook.INSTANCE.x + position.getLeft()) * scale, (int)(GuiBook.INSTANCE.y + 217 - position.getTop() - position.getHeight()) * scale, (int)position.getWidth() * scale, (int)position.getHeight() * scale);

            for (IFeatureProvider feature : Lists.reverse(page.getFeatures())) {
                if (feature instanceof FeaturePreviewWindow) continue; //No Cascading
                int y = GuiBook.INSTANCE.y;
                if (page.getScroll() > 0) {
                    GuiBook.INSTANCE.y -= page.getScroll();
                }

                boolean isMouseHovering = position.isOverFeature(xMouse, yMouse);
                int mouseX = isMouseHovering ? GuiBook.INSTANCE.mouseX: Short.MAX_VALUE;
                int mouseY = isMouseHovering ? GuiBook.INSTANCE.mouseY + page.getScroll() : Short.MAX_VALUE;
                int originalY = GuiBook.INSTANCE.mouseY;
                if (isMouseHovering) {
                    GuiBook.INSTANCE.mouseY = GuiBook.INSTANCE.mouseY + page.getScroll();
                }

                feature.draw(mouseX, mouseY);
                feature.addTooltip(GuiBook.INSTANCE.tooltip, mouseX, mouseY);
                if (isMouseHovering) {
                    GuiBook.INSTANCE.mouseY = originalY;
                }

                GuiBook.INSTANCE.y = y;
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            }


            glDisable(GL_SCISSOR_TEST);
            GlStateManager.popMatrix();

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            if (position.getHeight() < scrollMax) {
                EnchiridionAPI.draw.drawBorderedRectangle(position.getRight() - 10, position.getTop(), position.getRight(), position.getBottom(), 0xFFB0A483, 0xFF362C24);
                int pos = (int)((page.getScroll() * (position.getHeight() - 10)) / scrollMax);
                EnchiridionAPI.draw.drawBorderedRectangle(position.getRight() - 10, position.getTop() + pos, position.getRight(), position.getTop() + pos + 10, 0xFF2F271F, 0xFF191511);
            }
        }
    }

    @Override
    public void scroll(boolean down, int amount) {
        if (page != null && page != thisPage) {
            page.updateMaximumScroll(position.getBottom() -5); //Called constantly
            page.scroll(down, amount);
        }
    }
}
