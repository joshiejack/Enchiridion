package joshie.enchiridion.wiki.gui.popups;

import static joshie.enchiridion.ETranslate.translate;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledCentredText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.verticalGradient;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.ETranslate;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.ElementImage;
import joshie.enchiridion.wiki.gui.GuiTextEdit;

public class PageEditResource extends PageEdit {
    public ITextEditable resource = new AbstractTextEdit("enchiridion:textures/wiki/enchiridion_logo.png:2.5");
    public ElementImage editing;

    public PageEditResource() {
        super("image");
    }

    //Clear the editing when we switch page
    public void clear() {
        editing = null;
    }

    @Override
    public void draw() {
        start();
        resetZ();

        drawRect(350, 150, 950, 310, 0xEE1B2C43);
        drawRect(350, 310, 950, 313, 0xFFC2C29C);
        drawRect(350, 147, 950, 150, 0xFFC2C29C);
        drawRect(347, 147, 350, 313, 0xFFC2C29C);
        drawRect(950, 147, 953, 313, 0xFFC2C29C);
        verticalGradient(350, 150, 950, 165, 0xFF09111E, 0xFF1B2C43);
        verticalGradient(350, 165, 950, 180, 0xFF1B2C43, 0xFF09111E);
        drawRect(350, 180, 950, 183, 0xFFC2C29C);

        drawScaledCentredText(1.75F, "[b]" + getTitle() + "[/b]", 630, 160, 0xFFFFFF);

        drawScaledTexture(texture, 470, 190, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 660, 190, 100, 147, 134, 39, 1F);
        drawScaledTexture(texture, 790, 190, 100, 147, 154, 39, 1F);

        drawScaledCentredText(2F, "" + translate("image") + ":", 410, 200, 0xFFFFFF);

        drawScaledText(2F, GuiTextEdit.getText(resource), 480, 203, 0xFFFFFF);

        fixColors();
        //Highlighting the buttons
        int xYes = 0, xNo = 0;
        if (getIntFromMouse(500, 624, 250, 289, 0, 1) == 1) {
            xYes = 130;
        }

        if (getIntFromMouse(660, 784, 250, 289, 0, 1) == 1) {
            xNo = 130;
        }

        drawScaledTexture(texture, 500, 250, xYes, 104, 124, 39, 1F);
        drawScaledTexture(texture, 660, 250, xNo, 104, 124, 39, 1F);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate(getConfirmationText()) + "[/b]", 560, 262, 0xFFFFFF);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("cancel") + "[/b]", 720, 262, 0xFFFFFF);
        end();
    }

    @Override
    public String getConfirmationText() {
        return "edit";
    }

    @Override
    public void clicked(int button) {
        if (getIntFromMouse(470, 944, 190, 229, 0, 1) == 1) {
            GuiTextEdit.select(resource);
        }

        if (getIntFromMouse(500, 624, 250, 289, 0, 1) == 1) {
            add();
            cancel();
        }

        if (getIntFromMouse(650, 774, 250, 289, 0, 1) == 1) {
            cancel();
        }
    }

    @Override
    public boolean add() {
        if (editing != null) {
            editing.setPath(resource.getText(), true);
            return true;
        } else return false;
    }

    @Override
    public void cancel() {
        super.cancel();
        ((PageEditResource) (WikiHelper.getInstance(PageEditResource.class))).setEditing(null);
    }

    public boolean isEditingResource() {
        return editing != null;
    }

    public void setEditing(ElementImage image) {
        editing = image;
        if (image != null) {
            resource.setText(image.path);
        }
    }
}
