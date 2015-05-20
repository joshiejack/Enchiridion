package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.getHeight;
import static joshie.enchiridion.wiki.WikiHelper.getPage;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.Element;

public class GuiCanvas extends GuiExtension {
    private static final int DEFAULT_COLOR = 0xEE000000;

    @Override
    public void draw() {
        start();
        drawRect(295, 43, 1002, getHeight() + 10, 0x22000000);
        drawRect(295, 43, 1002, 5000 + 10, (WikiHelper.isLibrary() ? DEFAULT_COLOR : getPage().getData().getBackground()));
        drawRect(630, -45, 910, -10, 0xFF000000);
        enable(GL_SCISSOR_TEST);
        glEnable(GL_SCISSOR_TEST);
        getPage().display();
        glScissor(WikiHelper.getScaledX(295), 0, 704, getHeight() - 103);
        disable(GL_SCISSOR_TEST);
        end();
    }

    @Override
    public void clicked(int button) {
        if (!WikiHelper.hasPopupOpen()) {
            getPage().clickButton(mouseX - Element.BASE_X, mouseY - Element.BASE_Y, button);
        }
    }

    @Override
    public void release(int button) {
        getPage().releaseButton(mouseX - Element.BASE_X, mouseY - Element.BASE_Y, button);
    }

    @Override
    public void follow() {
        getPage().follow(mouseX - Element.BASE_X, mouseY - Element.BASE_Y);
    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (mouseX >= 290 && mouseX <= 1024) {
            getPage().scroll(scrolledDown ? -100 : 100);
        }
    }
}