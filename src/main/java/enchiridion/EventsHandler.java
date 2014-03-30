package enchiridion;

import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IPlayerTracker;
import enchiridion.CustomBooks.BookInfo;

public class EventsHandler implements ICraftingHandler, IPlayerTracker {
	//Places any books in the binder
	@ForgeSubscribe
	public void onItemPickUp(EntityItemPickupEvent event) {
		ItemStack stack = event.item.getEntityItem();
		if(ContainerBinder.isBook(stack)) {
			EntityPlayer player = event.entityPlayer;
			for(ItemStack invent: player.inventory.mainInventory) {
				if(invent != null) {
					if(invent.itemID == Enchiridion.items.itemID && invent.getItemDamage() == ItemEnchiridion.BINDER && invent.stackSize == 1) {
						ItemEnchiridion binder = (ItemEnchiridion)invent.getItem();
						int placed = binder.addToStorage(player.worldObj, invent, stack);
						if(placed > 0) {
							ItemStack clone = stack.copy();
							clone.stackSize-= placed;
							if(clone.stackSize > 0) {
								event.item.setEntityItemStack(clone);
							} else event.item.setDead();
							
							event.setCanceled(true);
							return;
						}
					}
				}
			}
		}
	}
	
	public NBTTagCompound getPlayerData(EntityPlayer player) {
		NBTTagCompound data = player.getEntityData();
		if(!data.hasKey(player.PERSISTED_NBT_TAG)) {
			data.setTag(player.PERSISTED_NBT_TAG, new NBTTagCompound());
		}

		return data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}
	
	public void spawnBook(EntityPlayer player, String identifier) {
		if(!getPlayerData(player).hasKey("EnchiridionBook" + identifier)) {
			getPlayerData(player).setBoolean("EnchiridionBook" + identifier, true);
			ItemStack book = CustomBooks.create(identifier);
			if(!player.worldObj.isRemote) {
				player.dropPlayerItem(book);
			}
		}
	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if(CustomBooks.bookInfo.size() > 0) {
			for (Entry<String, BookInfo> books : CustomBooks.bookInfo.entrySet()) {
				BookInfo info = books.getValue();
				if(info.onWorldStart) {
					spawnBook(player, books.getKey());
				}
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {}
	
	public void onCrafting(EntityPlayer player, ItemStack item) {
		if(CustomBooks.bookInfo.size() > 0) {
			for (Entry<String, BookInfo> books : CustomBooks.bookInfo.entrySet()) {
				BookInfo info = books.getValue();
				if(info.onCrafting != null) {
					ItemStack check = info.onCrafting;
					if(item.itemID == check.itemID && item.getItemDamage() == check.getItemDamage()) {
						spawnBook(player, books.getKey());
					}
				}
			}
		}
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
		onCrafting(player, item);
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
		onCrafting(player, item);
	}
}
