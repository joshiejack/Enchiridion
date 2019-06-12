package joshie.enchiridion.items;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLibrary extends Item { //WAS METADATA 1 BEFORE
    //TODO if (EConfig.SETTINGS.libraryAsItem)

    public ItemLibrary(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return new StringTextComponent(TextFormatting.GOLD + Enchiridion.format("library"));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            //NetworkHooks.openGui(player, GuiIDs.LIBRARY, buf -> buf.writeInt(hand.ordinal())); //TODO
        } else {
            int currentBook = LibraryHelper.getLibraryContents(player).getCurrentBook();
            ItemStack book = LibraryHelper.getLibraryContents(player).getStackInSlot(currentBook);
            if (!book.isEmpty()) {
                IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(book);
                if (handler != null && player instanceof ServerPlayerEntity) {
                    handler.handle(book, (ServerPlayerEntity) player, hand, currentBook, player.isSneaking());
                }
            } else {
                //NetworkHooks.openGui(player, GuiIDs.LIBRARY, buf -> buf.writeInt(hand.ordinal())); //TODO
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            }
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        int currentBook = LibraryHelper.getClientLibraryContents().getCurrentBook();
        ItemStack internal = LibraryHelper.getClientLibraryContents().getStackInSlot(currentBook);
        if (!internal.isEmpty()) {
            tooltip.addAll(internal.getTooltip(Minecraft.getInstance().player, flag));
        }
    }
}
