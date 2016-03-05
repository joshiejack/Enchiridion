package joshie.enchiridion.helpers;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class UUIDHelper {
    public static UUID getPlayerUUID(EntityPlayer player) {
        return EntityPlayer.getUUID(player.getGameProfile());
    }
    
    public static UUID getEntityUUID(Entity entity) {
        return entity.getPersistentID();
    }

    /** Gets the player from the uuid **/
    public static EntityPlayer getPlayerFromUUID(UUID uuid) {        
        //Loops through every single player
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
            if (getPlayerUUID(player).equals(uuid)) {
                return (EntityPlayer) player;
            }
        }
    
        return null;
    }
}