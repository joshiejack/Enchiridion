package joshie.enchiridion.items;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.lib.EGuis;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLibrary extends Item {

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
        return new TranslationTextComponent(this.getTranslationKey(stack)).setStyle(new Style().setColor(TextFormatting.GOLD));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) return new ActionResult<>(ActionResultType.FAIL, stack);

        if (player.isSneaking()) {
            if (player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, EGuis.getLibraryProvider(), buf -> buf.writeInt(hand.ordinal()));
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            }
        } else {
            int currentBook = LibraryHelper.getLibraryContents(player).getCurrentBook();
            ItemStack book = LibraryHelper.getLibraryContents(player).getStackInSlot(currentBook);
            if (!book.isEmpty()) {
                IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(book);
                if (handler != null && player instanceof ServerPlayerEntity) {
                    handler.handle(book, (ServerPlayerEntity) player, hand, currentBook, player.isSneaking());
                }
            } else {
                if (player instanceof ServerPlayerEntity) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, EGuis.getLibraryProvider(), buf -> buf.writeInt(hand.ordinal()));
                    return new ActionResult<>(ActionResultType.SUCCESS, stack);
                }
            }
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        /*int currentBook = LibraryHelper.getClientLibraryContents().getCurrentBook(); //TODO Crashes on client startup
        ItemStack internal = LibraryHelper.getClientLibraryContents().getStackInSlot(currentBook);
        if (!internal.isEmpty()) {
            tooltip.addAll(internal.getTooltip(Minecraft.getInstance().player, flag));
        }*/
    }
}