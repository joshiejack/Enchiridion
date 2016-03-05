package joshie.enchiridion.library;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import joshie.enchiridion.gui.library.GuiLibrary;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;

@JEIPlugin
public class JEISupport implements IAdvancedGuiHandler<GuiLibrary>, IModPlugin {
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

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {}

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {}

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(this);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {}

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
}