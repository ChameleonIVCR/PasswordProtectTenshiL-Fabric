package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.PlayerSession;
import com.chame.passwordtenshi.player.PlayerStorage;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;


public class OnGameMessage {
    public static boolean canSendMessage(ServerPlayNetworkHandler networkHandler, ChatMessageC2SPacket packet) {
        ServerPlayerEntity player = networkHandler.player;
        String message = packet.getChatMessage();
        PlayerSession playerSession = PlayerStorage.getPlayerSession(player.getUuid());

        boolean isAuthorized = playerSession != null && playerSession.isAuthorized();

        if (!isAuthorized && (message.startsWith("/login") || message.startsWith("/register"))) {
            return true;
        }

        return isAuthorized;
    }
}
