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
import joshie.enchiridion.wiki.gui.GuiExtension;
import joshie.enchiridion.wiki.gui.GuiTextEdit;

public abstract class PageEdit extends GuiExtension implements IPopupIDoItellYou {
    public ITextEditable mod = new AbstractTextEdit("Enchiridion 2");
    public ITextEditable tab = new AbstractTextEdit("Enchiridion 2");
    public ITextEditable cat = new AbstractTextEdit("Enchiridion 2");
    public ITextEditable page = new AbstractTextEdit("");

    public static class AbstractTextEdit implements ITextEditable {
        private String text = "";

        public AbstractTextEdit(String theDefault) {
            this.text = theDefault;
        }

        @Override
        public void setText(String text) {
            this.text = text.replace("\n", "");
        }

        @Override
        public String getText() {
            return this.text;
        }

        @Override
        public boolean canEdit(Object... objects) {
            return true;
        }
    }

    protected final String descriptor;

    public PageEdit(String descriptor) {
        setVisibility(false);
        this.descriptor = descriptor;
    }

    @Override
    public void draw() {
        start();
        resetZ();

        drawRect(450, 150, 850, 430, 0xEE1B2C43);
        drawRect(450, 430, 850, 433, 0xFFC2C29C);
        drawRect(450, 147, 850, 150, 0xFFC2C29C);
        drawRect(447, 147, 450, 433, 0xFFC2C29C);
        drawRect(850, 147, 853, 433, 0xFFC2C29C);
        verticalGradient(450, 150, 850, 165, 0xFF09111E, 0xFF1B2C43);
        verticalGradient(450, 165, 850, 180, 0xFF1B2C43, 0xFF09111E);
        drawRect(450, 180, 850, 183, 0xFFC2C29C);

        drawScaledCentredText(1.75F, "[b]" + getTitle() + "[/b]", 630, 160, 0xFFFFFF);

        drawScaledTexture(texture, 570, 190, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 690, 190, 100, 147, 154, 39, 1F);
        drawScaledTexture(texture, 570, 235, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 690, 235, 100, 147, 154, 39, 1F);
        drawScaledTexture(texture, 570, 280, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 690, 280, 100, 147, 154, 39, 1F);
        drawScaledTexture(texture, 570, 325, 0, 147, 254, 39, 1F);
        drawScaledTexture(texture, 690, 325, 100, 147, 154, 39, 1F);
        drawScaledCentredText(2F, "" + translate("mod") + ":", 510, 200, 0xFFFFFF);
        drawScaledCentredText(2F, "" + translate("tab") + ":", 510, 245, 0xFFFFFF);
        drawScaledCentredText(2F, "" + translate("category") + ":", 510, 290, 0xFFFFFF);
        drawScaledCentredText(2F, "" + translate("page") + ":", 510, 335, 0xFFFFFF);

        drawScaledText(2F, GuiTextEdit.getText(mod), 580, 203, 0xFFFFFF);
        drawScaledText(2F, GuiTextEdit.getText(tab), 580, 248, 0xFFFFFF);
        drawScaledText(2F, GuiTextEdit.getText(cat), 580, 293, 0xFFFFFF);
        drawScaledText(2F, GuiTextEdit.getText(page), 580, 338, 0xFFFFFF);

        fixColors();
        //Highlighting the buttons
        int xYes = 0, xNo = 0;
        if (getIntFromMouse(500, 624, 380, 419, 0, 1) == 1) {
            xYes = 130;
        }

        if (getIntFromMouse(660, 784, 380, 419, 0, 1) == 1) {
            xNo = 130;
        }

        drawScaledTexture(texture, 500, 380, xYes, 104, 124, 39, 1F);
        drawScaledTexture(texture, 660, 380, xNo, 104, 124, 39, 1F);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate(getConfirmationText()) + "[/b]", 560, 392, 0xFFFFFF);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("cancel") + "[/b]", 720, 392, 0xFFFFFF);
        end();
    }

    public String getConfirmationText() {
        return "add";
    }

    @Override
    public void clicked(int button) {
        if (getIntFromMouse(570, 844, 190, 229, 0, 1) == 1) {
            GuiTextEdit.selectAlphaNumeric(mod);
        }

        if (getIntFromMouse(570, 844, 235, 274, 0, 1) == 1) {
            GuiTextEdit.selectAlphaNumeric(tab);
        }

        if (getIntFromMouse(570, 844, 280, 319, 0, 1) == 1) {
            GuiTextEdit.selectAlphaNumeric(cat);
        }

        if (getIntFromMouse(570, 844, 325, 364, 0, 1) == 1) {
            GuiTextEdit.selectAlphaNumeric(page);
        }

        if (getIntFromMouse(500, 624, 380, 419, 0, 1) == 1) {
            if (add()) {
                cancel();
            }
        }

        if (getIntFromMouse(650, 774, 380, 419, 0, 1) == 1) {
            cancel();
        }
    }

    public String getTitle() {
        return translate("pageedit." + descriptor + ".title");
    }

    public String getDescription() {
        return translate("pageedit." + descriptor + ".description");
    }

    public void cancel() {
        WikiHelper.setVisibility(getClass(), false);
    }

    public abstract boolean add();
}
