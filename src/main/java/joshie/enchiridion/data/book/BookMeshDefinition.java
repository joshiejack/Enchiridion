package joshie.enchiridion.data.book;

import joshie.enchiridion.EClientProxy;
import joshie.enchiridion.api.book.IBook;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BookMeshDefinition implements ItemMeshDefinition {
    public static final BookMeshDefinition INSTANCE = new BookMeshDefinition();

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        if (stack.getItemDamage() == 1) return EClientProxy.libraryItem;
        else {
            BookRegistry bookRegistry = BookRegistry.INSTANCE;
            IBook book = bookRegistry.getBook(stack);
            if (book == null) return bookRegistry.DFLT;
            else return bookRegistry.locations.get(book.getUniqueName());
        }
    }
}