package joshie.enchiridion.wiki.elements;

import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiTextEdit;
import joshie.enchiridion.wiki.mode.ButtonBase;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextEdit;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextEffects;
import joshie.enchiridion.wiki.mode.edit.ButtonWikiTextSize;
import joshie.lib.util.Text;

import org.lwjgl.opengl.GL11;

import com.google.gson.annotations.Expose;

public class ElementText extends Element implements ITextEditable {
    @Expose
    private String text = "";
    private boolean init;

    @Override
    public ElementText setToDefault() {
        this.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        this.width = 200;
        this.height = 100;
        return this;
    }

    @Override
    public void display(boolean isEditMode) {
        if (!init) {
            init = ((text = text.replace("Â§", "\u00a7").replace("§", "\u00a7")) != null);
        }

        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        EClientProxy.font.drawSplitString(GuiTextEdit.getText(this, text), (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int) ((width * 2) / size) + 4, 0xFFFFFFFF);
        GL11.glPopMatrix();
    }

    @Override
    public void addEditButtons(List list) {
        /* int yCoord = 50;
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord, Text.END, "disable"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.BOLD, "bold"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.ITALIC, "italics"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.UNDERLINE, "underline"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.STRIKETHROUGH, "strikethrough"));
        list.add(new ButtonWikiTextEffects(wiki, wiki.button_id++, 75, yCoord += 50, Text.OBFUSCATED, "obfuscated"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, 0.1F, "larger"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, -0.1F, "smaller")); */
    }

    @Override
    public void onSelected() {
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = true;
            }
        }

        GuiTextEdit.select(this);
    }

    @Override
    public void onDeselected() {
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = false;
            }
        }

        markDirty();
    }

    @Override
    public void setText(String text) {
        if (isSelected) {
            this.text = text;
            markDirty();
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public boolean canEdit(Object... objects) {
        return isSelected;
    }
}
