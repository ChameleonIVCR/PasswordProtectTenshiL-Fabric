package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.LoginMod;
import com.chame.passwordtenshi.PlayerLogin;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class OnPlayerMove {
    public static boolean canMove(ServerPlayNetworkHandler networkHandler) {
        ServerPlayerEntity player = networkHandler.player;
        Boolean isAuthorized = PlayerStorage.getPlayerSession(player.getUUID()).isAuthorized();
        if (!isAuthorized) {
            player.teleport(player.getX(), player.getY(), player.getZ()); // teleport to sync client position
        }
        return isAuthorized;
    }
}
