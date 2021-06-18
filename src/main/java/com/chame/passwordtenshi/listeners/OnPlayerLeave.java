package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.PlayerStorage;

import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerLeave {
    
    public static void listen(ServerPlayerEntity player) {
        PlayerStorage.removePlayerSession(player.getUuid());
    }
}
