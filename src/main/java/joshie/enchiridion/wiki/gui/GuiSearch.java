package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixColors;
import static joshie.enchiridion.helpers.OpenGLHelper.resetZ;
import static joshie.enchiridion.helpers.OpenGLHelper.scaleZ;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static joshie.enchiridion.wiki.WikiHelper.setMod;
import static joshie.enchiridion.wiki.WikiHelper.setPage;
import static joshie.enchiridion.wiki.WikiHelper.setTab;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;

import java.util.ArrayList;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.ETranslate;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.data.WikiData;

public class GuiSearch extends GuiExtension implements ITextEditable {
    public static String search = "";
    public static int visible;

    @Override
    public void draw() {
        start();
        resetZ();

        int x = 0;
        if (getIntFromMouse(920, 1044, -45, -6, 0, 1) == 1) {
            x = 130;
        }

        if (visible > 0) {
            visible--;
            start();
            ArrayList<WikiPage> pages = WikiData.instance().getPages();
            drawRect(630, -10, 910, -3 + (pages.size() > 0 ? (pages.size() * 40) : 40), 0xFFC2C29C);
            drawRect(633, -7, 907, -6 + (pages.size() > 0 ? (pages.size() * 40) : 40), 0xFF1B2C43);

            if (pages.size() > 0) {
                for (int i = 0; i < pages.size(); i++) {
                    int[] colors = getContentBGColors(-5 + (i * 40));
                    scaleZ(100F);
                    drawSearchBox(pages.get(i).getTitle(), -48 + (i * 40), colors[0], colors[1], 638, 0);
                }
            } else {
                int[] colors = getContentBGColors(-5);
                scaleZ(100F);
                drawSearchBox(ETranslate.translate("noresults"), -48, colors[0], colors[1], 638, 0);
            }

            end();
        }

        fixColors();
        drawScaledTexture(texture, 630, -45, 0, 57, 254, 39, 1F);
        drawScaledTexture(texture, 756, -45, 100, 57, 154, 39, 1F);
        drawScaledText(2F, GuiTextEdit.getText(this, search), 641, -32, 0xFFFFFF);
        end();
    }

    @Override
    protected int[] getContentBGColors(int pageY) {
        int page_bg_1 = 0xFF10202F;
        int page_bg_2 = 0xFF10202F;
        if (mouseX >= 630 && mouseX <= 910) {
            if (mouseY > pageY + -35 + 39 && mouseY < -35 + pageY + 79) {
                page_bg_1 = 0xFF366374;
                page_bg_2 = 0xFF47849A;
                this.visible = 50;
            }
        }

        return new int[] { page_bg_1, page_bg_2 };
    }

    @Override
    public void clicked(int button) {
        if (getIntFromMouse(630, 910, -45, -10, 0, 1) == 1) {
            GuiTextEdit.select(this);
        }

        if (visible > 0) {
            ArrayList<WikiPage> pages = WikiData.instance().getPages();
            if (pages.size() > 0) {
                for (int i = 0; i < pages.size(); i++) {
                    if (getIntFromMouse(630, 910, (i * 40), 40 + (i * 40), 0, 1) == 1) {
                        setPage(pages.get(i));
                        setTab(pages.get(i).getCategory().getTab());
                        setMod(pages.get(i).getCategory().getTab().getMod());
                    }
                }
            }
        }
    }

    @Override
    public void setText(String text) {
        if (EClientProxy.font.getStringWidth(text) <= 130) {
            this.visible = 200;
            this.search = text.replace("\n", "");
            WikiData.instance().updateSearch(this.search);
        }
    }

    @Override
    public String getText() {
        return this.search;
    }

    @Override
    public boolean canEdit(Object... objects) {
        return true;
    }
}
