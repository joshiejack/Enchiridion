package joshie.enchiridion.wiki.elements;

import static joshie.enchiridion.helpers.OpenGLHelper.*;

import java.util.List;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.IGuiDisablesMenu;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.gui.GuiTextEdit;
import joshie.enchiridion.wiki.gui.buttons.ButtonBase;
import joshie.enchiridion.wiki.gui.buttons.ButtonWikiTextEdit;
import joshie.enchiridion.wiki.gui.buttons.ButtonWikiTextMode;
import joshie.enchiridion.wiki.gui.buttons.ButtonWikiTextSize;

import com.google.gson.annotations.Expose;

public class ElementText extends Element implements ITextEditable, IGuiDisablesMenu {
    //Whether text elements should display bbcode or display the formatted text
    public static boolean showBBCode = true;

    @Expose
    private String text = "";
    @Expose
    private int color = 0xFFFFFFFF;
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

        start();
        scaleAll(size);
        
        if (showBBCode && isEditMode) {
            EClientProxy.font.drawUnformattedSplitString(GuiTextEdit.getText(this, text), (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int) ((width * 2) / size) + 4, color);
        } else {
            EClientProxy.font.drawSplitString(GuiTextEdit.getText(this, text), (int) ((WikiHelper.theLeft + BASE_X + left) / size), ((int) ((WikiHelper.theTop + BASE_Y + top) / size)), (int) ((width * 2) / size) + 4, color);
        }

        end();
    }

    @Override
    public void addEditButtons(List list) {
        int yCoord = 10;
        list.add(new ButtonWikiTextMode(wiki, wiki.button_id++, 75, yCoord += 50, "mode"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, 0.01F, "larger.slightly"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, 0.1F, "larger"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, 0.5F, "larger.greatly"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, -0.01F, "smaller.slightly"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, -0.1F, "smaller"));
        list.add(new ButtonWikiTextSize(wiki, wiki.button_id++, 75, yCoord += 50, -0.5F, "smaller.greatly"));
    }

    @Override
    public void onSelected(int x, int y) {
        WikiHelper.clearEditGUIs();
        
        for (ButtonBase button : wiki.buttons) {
            if (button instanceof ButtonWikiTextEdit) {
                button.visible = true;
            }
        }

        GuiTextEdit.select(this, getText().length());
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
