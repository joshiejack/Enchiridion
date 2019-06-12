package joshie.enchiridion.data.book;

import joshie.enchiridion.EClientHandler;
import joshie.enchiridion.api.book.IBook;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BookMeshDefinition implements ItemMeshDefinition {
    public static final BookMeshDefinition INSTANCE = new BookMeshDefinition();

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        if (stack.getDamage() == 1) return EClientHandler.libraryItem;
        else {
            BookRegistry bookRegistry = BookRegistry.INSTANCE;
            IBook book = bookRegistry.getBook(stack);
            if (book == null) return bookRegistry.DFLT;
            else return bookRegistry.locations.get(book.getUniqueName());
        }
    }
}