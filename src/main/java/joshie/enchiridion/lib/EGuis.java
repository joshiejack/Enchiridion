package joshie.enchiridion.lib;

import io.netty.buffer.Unpooled;
import joshie.enchiridion.gui.library.ContainerLibrary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = EInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EGuis {
    public static final String BOOK = EInfo.MODID + ":" + "book";
    public static final String LIBRARY = EInfo.MODID + ":" + "library";
    public static final String BOOK_FORCE = EInfo.MODID + ":" + "book_force";
    public static final String WRITABLE = EInfo.MODID + ":" + "writable";
    public static final String WRITTEN = EInfo.MODID + ":" + "written";

    @ObjectHolder(EGuis.LIBRARY)
    public static ContainerType<ContainerLibrary> LIBRARY_CONTAINER;

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
                return LIBRARY_CONTAINER.create(id, playerInventory, buffer);
            }
        };
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(IForgeContainerType.create(ContainerLibrary::new).setRegistryName(LIBRARY));
    }
}