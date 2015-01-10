package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import static joshie.lib.helpers.OpenGLHelper.end;
import static joshie.lib.helpers.OpenGLHelper.start;

import java.util.Collection;

import joshie.enchiridion.lib.ETranslate;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiMod;
import joshie.enchiridion.wiki.WikiRegistry;
import joshie.enchiridion.wiki.data.WikiData;

import org.lwjgl.opengl.GL11;

public class GuiMode extends GuiExtension {
    public static String search = "";
    public static int visible;

    @Override
    public void draw() {
        int x = 0;
        if (getIntFromMouse(920, 1044, -45, -6, 0, 1) == 1) {
            x = 130;
        }
        
        drawScaledTexture(texture, 920, -45, x, 104, 124, 39, 1F);
        if(!WikiHelper.isLibrary()) {
            drawScaledCentredText(2F, "[b]" + ETranslate.translate("library") + "[/b]", 978, -32, 0xFFFFFF);
        } else {
            drawScaledCentredText(2F, "[b]" + ETranslate.translate("wiki") + "[/b]", 978, -32, 0xFFFFFF);
        }
    }

    @Override
    protected int[] getContentBGColors(int pageY) {
        int page_bg_1 = 0xFF10202F;
        int page_bg_2 = 0xFF10202F;
        if (mouseX >= 630 && mouseX <= 900) {
            if (mouseY > pageY + -35 + 39 && mouseY < -35 + pageY + 81) {
                page_bg_1 = 0xFF366374;
                page_bg_2 = 0xFF47849A;
                this.visible = 50;
            }
        }

        return new int[] { page_bg_1, page_bg_2 };
    }

    @Override
    public void clicked(int button) {
        if (getIntFromMouse(920, 1040, -45, -5, 0, 1) == 1) {
            WikiHelper.switchGui();
        }
    }
}
