package joshie.enchiridion.lib;

import io.netty.buffer.Unpooled;
import joshie.enchiridion.ECommonHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class GuiIDs {
    public static final String BOOK = EInfo.MODID + ":" + "book";
    public static final String LIBRARY = EInfo.MODID + ":" + "library";
    public static final String BOOK_FORCE = EInfo.MODID + ":" + "book_force";
    public static final String WRITABLE = EInfo.MODID + ":" + "writable";
    public static final String WRITTEN = EInfo.MODID + ":" + "written";

    public static INamedContainerProvider getLibraryProvider(Hand hand) {
        return new INamedContainerProvider() {
            @Override
            @Nonnull
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent(EInfo.MODID + ".container.library");
            }

            @Override
            public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
                PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
                buffer.writeInt(hand.ordinal());
                return ECommonHandler.LIBRARY_CONTAINER.create(id, playerInventory, buffer);
            }
        };
    }
}