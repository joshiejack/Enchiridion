package joshie.enchiridion.library;

import java.util.UUID;

import joshie.lib.helpers.EntityHelper;
import joshie.lib.helpers.UUIDHelper;
import joshie.lib.util.InventoryStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class LibraryInventory extends InventoryStorage {
    public static final int MAX = 1024;
    private EntityPlayer player; //No Direct calling, it's a cache value
    private UUID uuid; //SHOULD NOT BE CALLED, EXCEPT BY GET AND CREATE PLAYER

    public LibraryInventory() {
        super(MAX); //Create the inventory
    }

    public LibraryInventory(EntityPlayer player) {
        super(MAX); //Create the inventory

        if (player != null) { //Creation before the server has started
            this.player = player;
            this.uuid = UUIDHelper.getPlayerUUID(player);
        }
    }

    public EntityPlayer getAndCreatePlayer() {
        if (player == null) {
            player = EntityHelper.getPlayerFromUUID(uuid);
        }

        return player;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    @Override
    public void markDirty() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            LibraryHelper.markDirty();
        }
    }

    @Override
    public String getName() {
        return "enchiridion.library";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuid = UUID.fromString(nbt.getString("UUID")); //Read UUID
        super.readFromNBT(nbt); //Read NBT
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("UUID", uuid.toString()); //Write UUID
        super.writeToNBT(nbt); //Write Items
    }
}
