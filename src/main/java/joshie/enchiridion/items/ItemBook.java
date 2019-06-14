package joshie.enchiridion.items;

import joshie.enchiridion.EClientHandler;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.gui.book.GuiBook;
import joshie.enchiridion.gui.book.GuiBookCreate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GREEN;
import static net.minecraft.util.text.TextFormatting.RESET;

public class ItemBook extends Item {

    public ItemBook(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 16;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        IBook book = BookRegistry.INSTANCE.getBook(stack);
        return book == null || stack.getItem() == EItems.BOOK ? new TranslationTextComponent(Enchiridion.format("new", DARK_GREEN, RESET)) : new StringTextComponent(book.getDisplayName());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        IBook book = BookRegistry.INSTANCE.getBook(stack);
        if (book != null) {
            book.addInformation(tooltip);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (!held.isEmpty()) {
            IBook book = BookRegistry.INSTANCE.getBook(held);
            if (book != null) {
                if (world.isRemote) {
                    GuiBook.INSTANCE.setBook(book, player.isSneaking());
                    EClientHandler.openGuiBook();
                }
            } else {
                if (world.isRemote) {
                    EClientHandler.openGuiBookCreate();
                }
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, held);
    }
}