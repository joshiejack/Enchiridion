package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.helpers.OpenGLHelper.disable;
import static joshie.enchiridion.helpers.OpenGLHelper.enable;
import static joshie.enchiridion.helpers.OpenGLHelper.end;
import static joshie.enchiridion.helpers.OpenGLHelper.fixShitForThePedia;
import static joshie.enchiridion.helpers.OpenGLHelper.start;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.getHeight;
import static joshie.enchiridion.wiki.WikiHelper.getPage;
import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_NEVER;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilMask;
import static org.lwjgl.opengl.GL11.glStencilOp;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.Element;

import org.lwjgl.opengl.GL11;

public class GuiCanvas extends GuiExtension {
    private static final int DEFAULT_COLOR = 0xEE000000;

    @Override
    public void draw() {
        if(!EConfig.SHIT_COMPUTER) {
            fixShitForThePedia();
        }
        
        start();
        enable(GL_BLEND);
        glClear(GL_DEPTH_BUFFER_BIT);
        enable(GL_STENCIL_TEST);
        glStencilFunc(GL_NEVER, 1, 0xFF);
        glStencilOp(GL_REPLACE, GL_KEEP, GL_KEEP);
        glStencilMask(0xFF);
        glClear(GL_STENCIL_BUFFER_BIT);
        drawRect(295, 43, 1002, getHeight() + 10, 0x22000000);
        glStencilMask(0x00);
        glStencilFunc(GL_EQUAL, 0, 0xFF);
        glStencilFunc(GL_EQUAL, 1, 0xFF);
        disable(GL11.GL_LIGHTING);
        drawRect(295, 43, 1002, 5000 + 10, (WikiHelper.isLibrary() ? DEFAULT_COLOR : getPage().getData().getBackground()));
        drawRect(630, -45, 910, -10, 0xFF000000);
        getPage().display();
        disable(GL_STENCIL_TEST);
        disable(GL_BLEND);
        enable(GL11.GL_LIGHTING);
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