package joshie.enchiridion.gui.book;

import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.util.ELocation;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class AbstractGuiOverlay implements IBookEditorOverlay {
    protected static final ResourceLocation toolbar = new ELocation("toolbar");
    protected static final ResourceLocation sidebar = new ELocation("sidebar");

    @Override
    public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {}

    @Override
    public void keyTyped(char character, int key) {}

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {}

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {}

    @Override
    public void updateSearch(String search) {}
}
