package joshie.enchiridion.wiki.gui;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.getHeight;
import static joshie.enchiridion.wiki.WikiHelper.horizontalGradient;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class GuiSidebar extends GuiExtension {
    @Override
    public void draw() {
        glDisable(GL_LIGHTING);
        drawRect(275, 43, 278, getHeight(), 0xFFC2C29C);
        
        /* Menu Selection */
        //#2A535E
        horizontalGradient(5, 43, 270, getHeight(), 0xEE0E1924, 0xEE0E1924);
        drawRect(270, 43, 275, getHeight(), 0xEE2A535E);

        glEnable(GL_LIGHTING);
    }

    @Override
    public void clicked(int button) {
        return;
    }

    @Override
    public void release(int button) {
        return;
    }
}
