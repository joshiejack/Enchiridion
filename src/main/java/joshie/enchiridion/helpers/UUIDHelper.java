package joshie.enchiridion.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class UUIDHelper {
    public static UUID getPlayerUUID(PlayerEntity player) {
        return PlayerEntity.getUUID(player.getGameProfile());
    }

    /**
     * Gets the player from the uuid
     **/
    public static ServerPlayerEntity getPlayerFromUUID(UUID uuid) {
        //Loops through every single player
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (getPlayerUUID(player).equals(uuid)) {
                return player;
            }
        }
        return null;
    }
}