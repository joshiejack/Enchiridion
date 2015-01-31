package joshie.enchiridion.designer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.designer.BookRegistry.BookData;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketSyncNewBook;
import joshie.enchiridion.wiki.WikiHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

public class GuiDesignNew extends GuiScreen {
    private String text;
    private int mouseX = 0;
    private int mouseY = 0;
    private int x;
    private int y;
    private int tick;
    private boolean white;
    private int position;

    private ItemStack stack;

    public GuiDesignNew(ItemStack stack) {
        this.stack = stack;
        this.text = "";
        this.position = 0;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);

        if (key == 203) {
            cursorLeft(1);
        } else if (key == 205) {
            cursorRight(1);
        } else if (character == 22) {
            add(GuiScreen.getClipboardString());
        } else if (key == 14) {
            delete(-1);
        } else if (key == 211) {
            delete(0);
        } else if (key == 28 || key == 156) {
            add("\n");
        } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
            add(Character.toString(character));
        }
    }

    public void drawScreen(int i, int j, float f) {
        x = width / 2;
        y = height / 2;
        super.drawScreen(i, j, f);

        drawARect(-102, -55, -100, -37, 0xFFFFFFFF);
        drawARect(100, -55, 102, -37, 0xFFFFFFFF);
        drawARect(-102, -57, 102, -55, 0xFFFFFFFF);
        drawARect(-100, -55, 100, -37, 0xFF000000);
        drawARect(-100, -37, 100, -39, 0xFFFFFFFF);
        drawSplitString(getText(), -95, -50, 700, 0xFFFFFFFF);
    }

    public void drawARect(int x1, int y1, int x2, int y2, int color) {
        drawRect(x + x1, y + y1, x + x2, y + y2, color);
    }

    public void drawSplitString(String text, int left, int top, int wrap, int color) {
        EClientProxy.font.drawSplitString(text, x + left, y + top, wrap, color);
    }

    public String getText() {
        tick++;
        if (tick % 60 == 0) {
            if (white) {
                white = false;
            } else {
                white = true;
            }
        }

        if (white) {
            return new StringBuilder(text).insert(Math.min(position, text.length()), "[*cursor*]").toString();
        } else {
            return new StringBuilder(text).insert(Math.min(position, text.length()), "[*/cursor*]").toString();
        }
    }

    private void cursorLeft(int count) {
        int left = position - count;
        if (left < 0) {
            position = 0;
        } else position = left;
    }

    private void cursorRight(int count) {
        String text = this.text;
        int right = position + count;
        if (right > text.length()) {
            position = text.length();
        } else position = right;
    }

    private void add(String string) {
        if (text.length() > 30 && !string.equals("\n")) return;
        if (string.equals("\n")) {
            try {
                String sanitized = text.replaceAll("[^A-Za-z0-9]", "_");
                BookData data = new BookData(sanitized);
                File example = new File(Enchiridion.root, "books/" + sanitized + ".json");
                Writer writer = new OutputStreamWriter(new FileOutputStream(example), "UTF-8");
                writer.write(WikiHelper.getGson().toJson(data));
                writer.close();

                stack.setTagCompound(new NBTTagCompound());
                stack.stackTagCompound.setString("identifier", sanitized);
                BookRegistry.register(data);
                EPacketHandler.sendToServer(new PacketSyncNewBook(sanitized));
                ClientHelper.getPlayer().closeScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String text = this.text;
        StringBuilder builder = new StringBuilder(text);
        text = builder.insert(position, string).toString();
        this.text = text;
        cursorRight(string.length());
    }

    private void delete(int count) {
        String text = this.text;
        if ((count < 0 && position > 0) || (count >= 0 && position < text.length())) {
            StringBuilder builder = new StringBuilder(text);
            text = builder.deleteCharAt(position + count).toString();
            this.text = text;
            if (count < 0) cursorLeft(-count);
            else if (count >= 0) cursorRight(count);
        }
    }
}
