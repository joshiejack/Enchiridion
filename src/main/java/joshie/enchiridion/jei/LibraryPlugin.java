package joshie.enchiridion.jei;

import joshie.enchiridion.gui.library.GuiLibrary;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public class LibraryPlugin extends BlankModPlugin implements IAdvancedGuiHandler<GuiLibrary> {
    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipeHandlers(new LibraryRecipeHandler());
        registry.addRecipes(Collections.singletonList(new LibraryRecipeWrapper()));
        registry.addAdvancedGuiHandlers(this);
    }

    @Override
    public Class<GuiLibrary> getGuiContainerClass() {
        return GuiLibrary.class;
    }

    @Override
    public List<Rectangle> getGuiExtraAreas(GuiLibrary library) {
        List<Rectangle> rectangles = new ArrayList();
        rectangles.add(new Rectangle(library.x, library.y + 40, 367, 116));
        return rectangles;
    }
}
