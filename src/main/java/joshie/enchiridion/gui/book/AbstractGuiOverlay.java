package joshie.enchiridion.gui.book;

import joshie.enchiridion.api.gui.IBookEditorOverlay;
import joshie.enchiridion.util.ELocation;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class AbstractGuiOverlay implements IBookEditorOverlay {
    protected static final ResourceLocation TOOLBAR = new ELocation("toolbar");
    protected static final ResourceLocation SIDEBAR = new ELocation("sidebar");

    @Override
    public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
    }

    @Override
    public void charTyped(char character, int key) {
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {
    }

    @Override
    public void updateSearch(String search) {
    }

    @Override
    public void tick() {
    }

    @Override
    public IGuiEventListener getFocused() {
        return null;
    }
}