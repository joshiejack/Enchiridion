package joshie.enchiridion.network.core;

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IPacketArray {
    /**
     * Client should do nothing or send a packet to request data
     **/
    default void receivedHashcode(ServerPlayerEntity player) {
    }

    /**
     * Server should now send the length
     **/
    default void receivedLengthRequest(ServerPlayerEntity player) {
    }

    /**
     * Client should now send a received size packet
     **/
    default void receivedStringLength(ServerPlayerEntity player) {
    }

    /**
     * Server should now send the data for the string
     **/
    default void receivedDataRequest(ServerPlayerEntity player) {
    }

    /**
     * Client should try to build a list of the data after received all the data
     **/
    default void receivedData(ServerPlayerEntity player) {
    }
}