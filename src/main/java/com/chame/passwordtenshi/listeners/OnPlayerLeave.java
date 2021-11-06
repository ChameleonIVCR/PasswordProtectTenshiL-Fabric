package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerLeave {
    
    public static void listen(ServerPlayerEntity player) {
        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());
        if (playerSession != null){
            playerSession.setSurvival();
        }
        PlayerStorage.removePlayerSession(player.getUuid());
    }
}
