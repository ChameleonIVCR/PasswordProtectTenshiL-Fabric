package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.PlayerStorage;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerMove {
    public static boolean canMove(ServerPlayNetworkHandler networkHandler) {
        ServerPlayerEntity player = networkHandler.player;
        Boolean isAuthorized = PlayerStorage.getPlayerSession(player.getUuid()).isAuthorized();
        if (!isAuthorized) {
            player.teleport(player.getX(), player.getY(), player.getZ()); // teleport to sync client position
        }
        return isAuthorized;
    }
}
