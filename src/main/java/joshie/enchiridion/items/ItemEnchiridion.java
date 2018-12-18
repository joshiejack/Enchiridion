package joshie.enchiridion.items;

import amerifrance.guideapi.api.IGuideItem;
import amerifrance.guideapi.api.impl.Book;
import joshie.enchiridion.ECommonProxy;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.api.book.IBookHandler;
import joshie.enchiridion.data.book.BookRegistry;
import joshie.enchiridion.lib.GuiIDs;
import joshie.enchiridion.library.LibraryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GREEN;
import static net.minecraft.util.text.TextFormatting.RESET;

@Optional.Interface(modid = "guideapi", iface = "amerifrance.guideapi.api.IGuideItem")
@EventBusSubscriber
public class ItemEnchiridion extends Item implements IGuideItem {
    public ItemEnchiridion() {
        setHasSubtypes(true);
    }

    @Optional.Method(modid = "guideapi")
    @Override
    public Book getBook(@Nonnull ItemStack stack) {
        return null;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || oldStack.getItemDamage() != newStack.getItemDamage();
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == 1 ? 1 : 16;
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return TextFormatting.GOLD + Enchiridion.translate("library");
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return Enchiridion.format("new", DARK_GREEN, RESET);
        }

        IBook book = BookRegistry.INSTANCE.getBook(stack);
        return book == null ? Enchiridion.format("new", DARK_GREEN, RESET) : book.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (stack.getItemDamage() == 1) {
            int currentBook = LibraryHelper.getClientLibraryContents().getCurrentBook();
            ItemStack internal = LibraryHelper.getClientLibraryContents().getStackInSlot(currentBook);
            if (!internal.isEmpty()) {
                tooltip.addAll(internal.getTooltip(Minecraft.getMinecraft().player, flag));
            }
        } else {
            IBook book = BookRegistry.INSTANCE.getBook(stack);
            if (book != null) {
                book.addInformation(tooltip);
            }
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItemDamage() == 1) {
            if (player.isSneaking()) player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, world, 0, hand.ordinal(), 0);
            else {
                int currentBook = LibraryHelper.getLibraryContents(player).getCurrentBook();
                ItemStack book = LibraryHelper.getLibraryContents(player).getStackInSlot(currentBook);
                if (!book.isEmpty()) {
                    IBookHandler handler = EnchiridionAPI.library.getBookHandlerForStack(book);
                    if (handler != null) {
                        handler.handle(book, player, hand, currentBook, player.isSneaking());
                    }
                } else player.openGui(Enchiridion.instance, GuiIDs.LIBRARY, world, 0, hand.ordinal(), 0);
            }

        } else player.openGui(Enchiridion.instance, GuiIDs.BOOK, world, 0, hand.ordinal(), 0);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab)) {
            if (EConfig.libraryAsItem) list.add(new ItemStack(this, 1, 1));

            list.add(new ItemStack(this));

            for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("identifier", book);
                list.add(stack);
            }
        }
    }

    @Override
    @Nonnull
    public Item setTranslationKey(@Nonnull String unlocalizedName) {
        super.setTranslationKey(unlocalizedName);
        setRegistryName(unlocalizedName);
        return this;
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ECommonProxy.book);
    }
}