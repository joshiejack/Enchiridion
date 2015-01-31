package joshie.enchiridion.library.mods;

import joshie.enchiridion.network.EPacketHandler;
import joshie.enchiridion.network.PacketAlfheim;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class BotaniaCommon {
    public static final BotaniaCommon INSTANCE = new BotaniaCommon();
    public static ItemStack alfheim;
    public static ItemStack lexicon;

    public void init() {
        EPacketHandler.registerPacket(PacketAlfheim.class, Side.SERVER);
        lexicon = new ItemStack(GameRegistry.findItem("Botania", "lexicon"), 1, 0);
        alfheim = lexicon.copy();
        alfheim.setTagCompound(new NBTTagCompound());
        alfheim.stackTagCompound.setBoolean("knowledge.minecraft", true);
        alfheim.stackTagCompound.setBoolean("knowledge.alfheim", true);
    }
}
