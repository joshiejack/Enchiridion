package joshie.enchiridion.items;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IBook;
import joshie.enchiridion.data.book.BookRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GREEN;
import static net.minecraft.util.text.TextFormatting.RESET;

public class ItemEnchiridion extends Item {

    public ItemEnchiridion(Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 16;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        if (EffectiveSide.get() == LogicalSide.SERVER) {
            return new StringTextComponent(Enchiridion.format("new", DARK_GREEN, RESET));
        }

        IBook book = BookRegistry.INSTANCE.getBook(stack);
        return book == null ? new StringTextComponent(Enchiridion.format("new", DARK_GREEN, RESET)) : book.getDisplayName();
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
        ItemStack stack = player.getHeldItem(hand);
        //player.openGui(Enchiridion.instance, GuiIDs.BOOK, world, 0, hand.ordinal(), 0); //TODO
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    /*@Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) { //TODO

            for (String book : BookRegistry.INSTANCE.getUniqueNames()) {
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("identifier", book);
                list.add(stack);
    }*/
}