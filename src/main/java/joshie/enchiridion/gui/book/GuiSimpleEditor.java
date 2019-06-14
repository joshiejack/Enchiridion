package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.util.TextEditor;

import java.util.List;

public class GuiSimpleEditor extends AbstractGuiOverlay implements ITextEditable {
    public static final GuiSimpleEditor INSTANCE = new GuiSimpleEditor();
    private String text = "";
    private IBookEditorOverlay editor = null;

    private GuiSimpleEditor() {
    }

    public void setEditor(IBookEditorOverlay editor) {
        this.editor = editor;
        this.text = "";
        if (editor != null) {
            editor.updateSearch(this.text);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        if (editor != null) {
            /* Draw the Background */
            EnchiridionAPI.draw.drawImage(SIDEBAR, EConfig.SETTINGS.editorXPos - 3, EConfig.SETTINGS.toolbarYPos.get() - 7, EConfig.SETTINGS.editorXPos + 87, EConfig.SETTINGS.timelineYPos.get() + 13);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos, EConfig.SETTINGS.toolbarYPos.get() + 7, EConfig.SETTINGS.editorXPos + 85, EConfig.SETTINGS.timelineYPos.get() + 11, 0xFF312921, 0xFF191511);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos + 2, EConfig.SETTINGS.toolbarYPos.get() + 9, EConfig.SETTINGS.editorXPos + 83, EConfig.SETTINGS.timelineYPos.get() + 9, 0xFFE4D6AE, 0x5579725A);
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos, EConfig.SETTINGS.toolbarYPos.get() - 3, EConfig.SETTINGS.editorXPos + 84, EConfig.SETTINGS.toolbarYPos.get() + 7, 0xFF312921, 0xFF191511);
            EnchiridionAPI.draw.drawSplitScaledString(TextEditor.INSTANCE.getText(this), EConfig.SETTINGS.editorXPos + 5, EConfig.SETTINGS.toolbarYPos.get(), 250, 0xFFFFFFFF, 0.5F);
            editor.draw(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char character, int key) {
        if (editor != null) {
            editor.updateSearch(getTextField());
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        if (editor != null) {
            System.out.println("editor not null");
            if (mouseX >= EConfig.SETTINGS.editorXPos && mouseX <= EConfig.SETTINGS.editorXPos + 84 && mouseY >= EConfig.SETTINGS.toolbarYPos.get() - 3 && mouseY <= EConfig.SETTINGS.toolbarYPos.get() + 7) {
                TextEditor.INSTANCE.setEditable(this);
                return true;
            } else {
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

    @Override
    public String getTextField() {
        return text;
    }

    @Override
    public void setTextField(String text) {
        this.text = text;
    }
}