package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

public class ButtonToggleScrollable extends ButtonAbstract {
    private ResourceLocation selected_dflt;
    private ResourceLocation selected_hover;
    private String translate_selected;

    public ButtonToggleScrollable() {
        super("scroll");
        dflt = new ResourceLocation(EInfo.TEXPATH + name + "_unselected_dftl.png");
        hover = new ResourceLocation(EInfo.TEXPATH + name + "_unselected_hover.png");
        selected_dflt = new ResourceLocation(EInfo.TEXPATH + name + "_selected_dftl.png");
        selected_hover = new ResourceLocation(EInfo.TEXPATH + name + "_selected_hover.png");
        translate_selected = "button." + name + ".selected";
    }

    @Override
    public boolean isLeftAligned() {
        return false;
    }

    @Override
    public ResourceLocation getResource() {
        return GuiBook.INSTANCE.getPage().isScrollingEnabled() ? selected_dflt : dflt;
    }

    @Override
    public ResourceLocation getHoverResource() {
        return GuiBook.INSTANCE.getPage().isScrollingEnabled() ? selected_hover : hover;
    }

    @Override
    public String getTooltip() {
        return GuiBook.INSTANCE.getPage().isScrollingEnabled() ? Enchiridion.translate(translate_selected) : Enchiridion.translate(translate);
    }

    @Override
    public void performAction() {
        GuiBook.INSTANCE.getPage().toggleScroll();
    }
}