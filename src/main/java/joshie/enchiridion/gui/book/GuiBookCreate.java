package joshie.enchiridion.gui.book;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import joshie.enchiridion.data.book.Book;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.buttons.ButtonChangeIcon;
import joshie.lib.PenguinFontRenderer;
import joshie.lib.editables.ITextEditable;
import joshie.lib.editables.TextEditor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiBookCreate extends GuiScreen implements ITextEditable {
	public static final GuiBookCreate INSTANCE = new GuiBookCreate();
	
    private String text;
    private ItemStack stack;

    private GuiBookCreate() {}
    public GuiBookCreate setStack(ItemStack stack) {
        this.stack = stack;
        this.text = "";
        return this;
    }
    
	@Override
	public String getTextField() {
		return text;
	}

	@Override
	public void setTextField(String text) {
		this.text = text;
		
		if (this.text.contains("\n")) { //If we pressed enter, clear out the return
			text = text.replace("\n", "");
			String sanitized = text.replaceAll("[^A-Za-z0-9]", "_");
			Book book = new Book(sanitized, text); //Create the book
			stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("identifier", sanitized);
            BookRegistry.INSTANCE.register(book); //Register the book
            GuiBook.INSTANCE.setBook(book, true); //Set the gui to it ahead of time
            GuiBook.INSTANCE.onGuiClosed(); //Save the data to json
		}
	}

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        TextEditor.INSTANCE.setEditable(this);
        ButtonChangeIcon.refreshResources();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        TextEditor.INSTANCE.clearEditable();
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);
        TextEditor.INSTANCE.keyTyped(character, key);
        if (key == 28 || key == 156) {
        	mc.thePlayer.closeScreen();
        }
    }
    
    private int x, y;

    public void drawScreen(int i, int j, float f) {
        x = width / 2;
        y = height / 2;
        super.drawScreen(i, j, f);

        drawARect(-102, -55, -100, -37, 0xFFFFFFFF);
        drawARect(100, -55, 102, -37, 0xFFFFFFFF);
        drawARect(-102, -57, 102, -55, 0xFFFFFFFF);
        drawARect(-100, -55, 100, -37, 0xFF000000);
        drawARect(-100, -37, 100, -39, 0xFFFFFFFF);
        
        drawSplitString(TextEditor.INSTANCE.getText(this), -95, -50, 700, 0xFFFFFFFF);
    }

    public void drawARect(int x1, int y1, int x2, int y2, int color) {
        drawRect(x + x1, y + y1, x + x2, y + y2, color);
    }

    public void drawSplitString(String text, int left, int top, int wrap, int color) {
        PenguinFontRenderer.INSTANCE.drawSplitString(text, x + left, y + top, wrap, color);
    }
}
