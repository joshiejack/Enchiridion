package joshie.enchiridion.gui.book;

import com.google.common.base.CaseFormat;
import joshie.enchiridion.data.book.Book;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.buttons.ButtonChangeIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiBookCreate extends GuiBase {
    public static final GuiBookCreate INSTANCE = new GuiBookCreate();
    TextFieldWidget textField;
    private String text;

    private GuiBookCreate() {
    }

    public GuiBookCreate setStack(@Nonnull ItemStack stack) {
        this.text = "";
        return this;
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return this.textField;
    }

    @Override
    public void init() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);

        this.textField = new TextFieldWidget(this.font, this.width / 2 - 101, height / 2 - 57, 202, 20, "enchiridion.bookCreate.title");
        this.textField.setMaxStringLength(32767);
        this.textField.changeFocus(true);
        this.textField.setCanLoseFocus(false);
        this.textField.setText(text != null && !text.isEmpty() ? text : "");
    }

    @Override
    public void removed() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
        ButtonChangeIcon.refreshResources();
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ENTER || p_keyPressed_1_ == GLFW.GLFW_KEY_KP_ENTER) {
            if (!text.isEmpty()) {
                String sanitized = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text).replaceAll("[^A-Za-z0-9]",".").replace(".", "_");
                Book book = new Book(sanitized, text); //Create the book
                BookRegistry.INSTANCE.register(book); //Register the book
                GuiBook.INSTANCE.setBook(book, true);
                GuiBook.INSTANCE.removed(); //Save the data to json
                this.onClose();
                return true;
            }
            return false;
        } else {
            return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().player.closeScreen();
    }

    @Override
    public boolean charTyped(char key, int keycode) {
        if (this.textField.charTyped(key, keycode)) {
            text = textField.getText().trim();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        this.textField.mouseClicked(mx, my, button);
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        super.render(mouseX, mouseY, partial);

        this.textField.render(mouseX, mouseY, partial);
    }
}