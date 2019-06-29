package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.List;

public class GuiSimpleEditor extends AbstractGuiOverlay {
    public static final GuiSimpleEditor INSTANCE = new GuiSimpleEditor();
    private TextFieldWidget textField;
    private String text = "";
    private IBookEditorOverlay editor = null;

    private GuiSimpleEditor() {
    }

    public void setEditor(IBookEditorOverlay editor) {
        this.editor = editor;
        this.text = "";
    }

    @Override
    public void init() {
        Minecraft mc = Minecraft.getInstance();
        Screen currentScreen = mc.currentScreen;
        if (currentScreen != null) {
            this.textField = new TextFieldWidget(mc.fontRenderer, mc.mainWindow.getScaledWidth() / 4 + (EConfig.SETTINGS.editorXPos + 27), mc.mainWindow.getScaledHeight() / 4 + (EConfig.SETTINGS.toolbarYPos.get() + 17), 80, 7, "enchiridion.simpleEditor.search");
            this.textField.setMaxStringLength(32);
            this.textField.setEnableBackgroundDrawing(false); //TODO Set to false when done
            this.textField.setText(text != null && !text.isEmpty() ? text : "");
        }
    }

    @Override
    public void tick() {
        if (editor != null) {
            textField.tick();
            this.editor.updateSearch(this.getText());
        }
    }

    @Override
    public IGuiEventListener getFocused() {
        if (editor != null) {
            return this.textField;
        }
        return super.getFocused();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (editor != null) {
            /* Draw the Background */
            EnchiridionAPI.draw.drawImage(SIDEBAR, EConfig.SETTINGS.editorXPos - 3, EConfig.SETTINGS.toolbarYPos.get() - 7, EConfig.SETTINGS.editorXPos + 87, EConfig.SETTINGS.timelineYPos.get() + 13);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos, EConfig.SETTINGS.toolbarYPos.get() + 7, EConfig.SETTINGS.editorXPos + 85, EConfig.SETTINGS.timelineYPos.get() + 11, 0xFF312921, 0xFF191511);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos + 2, EConfig.SETTINGS.toolbarYPos.get() + 9, EConfig.SETTINGS.editorXPos + 83, EConfig.SETTINGS.timelineYPos.get() + 9, 0xFFE4D6AE, 0x5579725A);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos, EConfig.SETTINGS.toolbarYPos.get() - 3, EConfig.SETTINGS.editorXPos + 84, EConfig.SETTINGS.toolbarYPos.get() + 7, 0xFF312921, 0xFF191511);
            editor.draw(mouseX, mouseY);
            if (textField.isFocused()) {
                textField.renderButton(mouseX, mouseY, 0);
            }
        }
    }

    @Override
    public void charTyped(char character, int key) {
        if (editor != null) {
            if (this.textField.charTyped(character, key)) {
                text = textField.getText().trim();
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (editor != null) {
            if (mouseX >= EConfig.SETTINGS.editorXPos && mouseX <= EConfig.SETTINGS.editorXPos + 84 && mouseY >= EConfig.SETTINGS.toolbarYPos.get() - 3 && mouseY <= EConfig.SETTINGS.toolbarYPos.get() + 7) {
                this.textField.mouseClicked(mouseX, mouseY, 0);
                this.textField.setFocused2(true);
                return true;
            } else {
                this.textField.setFocused2(false);
                return editor.mouseClicked(mouseX, mouseY);

            }

        }
        return false;
    }

    @Override
    public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
        if (editor != null) {
            editor.addToolTip(tooltip, mouseX, mouseY);
        }
    }

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {
        if (editor != null) {
            editor.scroll(down, mouseX, mouseY);
        }
    }

    public String getText() {
        return textField.getText();
    }
}