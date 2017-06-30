package joshie.enchiridion.jei;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.gui.library.GuiLibrary;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public class LibraryPlugin implements IModPlugin, IAdvancedGuiHandler<GuiLibrary> {
    @Override
    public void register(@Nonnull IModRegistry registry) {
        if (EConfig.addOreDictionaryRecipeForLibrary) {
            registry.handleRecipes(ShapedOreRecipe.class, recipe -> new LibraryRecipeWrapper(), VanillaRecipeCategoryUid.CRAFTING);
            registry.addRecipes(Collections.singletonList(new LibraryRecipeWrapper()), VanillaRecipeCategoryUid.CRAFTING);
        }
        registry.addAdvancedGuiHandlers(this);
    }

    @Override
    @Nonnull
    public Class<GuiLibrary> getGuiContainerClass() {
        return GuiLibrary.class;
    }

    @Override
    public List<Rectangle> getGuiExtraAreas(@Nonnull GuiLibrary library) {
        List<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(new Rectangle(library.x, library.y + 40, 367, 116));
        return rectangles;
    }

    @Override
    @Nullable
    public Object getIngredientUnderMouse(@Nonnull GuiLibrary guiContainer, int mouseX, int mouseY) {
        return null;
    }
}