package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import net.minecraft.util.ResourceLocation;

public abstract class GuiSimpleEditorAbstract extends AbstractGuiOverlay {
    public void drawImage(ResourceLocation location, int x, int y, int x2, int y2) {
        EnchiridionAPI.draw.drawImage(location, EConfig.SETTINGS.editorXPos + x, EConfig.SETTINGS.toolbarYPos.get() + y, EConfig.SETTINGS.editorXPos + x2, EConfig.SETTINGS.toolbarYPos.get() + y2);
    }

    public void drawBorderedRectangle(int x, int y, int x2, int y2, int colorI, int colorB) {
        EnchiridionAPI.draw.drawBorderedRectangle(EConfig.SETTINGS.editorXPos + x, EConfig.SETTINGS.toolbarYPos.get() + y, EConfig.SETTINGS.editorXPos + x2, EConfig.SETTINGS.toolbarYPos.get() + y2, colorI, colorB);
    }

    public void drawRectangle(int x, int y, int x2, int y2, int colorI) {
        EnchiridionAPI.draw.drawRectangle(EConfig.SETTINGS.editorXPos + x, EConfig.SETTINGS.toolbarYPos.get() + y, EConfig.SETTINGS.editorXPos + x2, EConfig.SETTINGS.toolbarYPos.get() + y2, colorI);
    }

    public void drawSplitScaledString(String text, int x, int y, int color, float scale) {
        EnchiridionAPI.draw.drawSplitScaledString(text, EConfig.SETTINGS.editorXPos + x, EConfig.SETTINGS.toolbarYPos.get() + y, 155, color, scale);
    }
}