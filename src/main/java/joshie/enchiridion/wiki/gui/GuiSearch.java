package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import static joshie.lib.helpers.OpenGLHelper.end;
import static joshie.lib.helpers.OpenGLHelper.start;

import java.util.ArrayList;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.lib.ETranslate;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.data.WikiData;

import org.lwjgl.opengl.GL11;

public class GuiSearch extends GuiExtension implements ITextEditable {
    public static String search = "";
    public static int visible;

    @Override
    public void draw() {
        int x = 0;
        if (getIntFromMouse(920, 1044, -45, -6, 0, 1) == 1) {
            x = 130;
        }
        
        drawScaledTexture(texture, 920, -45, x, 104, 124, 39, 1F);
        drawScaledCentredText(2F, "[b]" + ETranslate.translate("search") + "[/b]", 978, -32, 0xFFFFFF);

        if (visible > 0) {
            visible--;
            start();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            ArrayList<WikiPage> pages = WikiData.instance().getPages();
            drawRect(630, -10, 910, -3 + (pages.size() > 0 ? (pages.size() * 40) : 40), 0xFFC2C29C);
            drawRect(633, -7, 907, -6 + (pages.size() > 0 ? (pages.size() * 40) : 40), 0xFF1B2C43);

            if (pages.size() > 0) {
                for (int i = 0; i < pages.size(); i++) {
                    int[] colors = getContentBGColors(-5 + (i * 40));
                    GL11.glScalef(1.0F, 1.0F, 100F);
                    drawSearchBox(pages.get(i).getTitle(), -48 + (i * 40), colors[0], colors[1], 638, 0);
                }
            } else {
                int[] colors = getContentBGColors(-5);
                GL11.glScalef(1.0F, 1.0F, 100F);
                drawSearchBox(ETranslate.translate("noresults"), -48, colors[0], colors[1], 638, 0);
            }

            end();
        }
        
        drawScaledTexture(texture, 630, -45, 0, 57, 254, 39, 1F);
        drawScaledTexture(texture, 756, -45, 100, 57, 154, 39, 1F);
        drawScaledText(2F, GuiTextEdit.getText(this, search), 641, -33, 0xFFFFFF);
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

        if (getIntFromMouse(920, 1040, -45, -10, 0, 1) == 1) {
            WikiData.instance().updateSearch(this.search);
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
            this.search = text;
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
