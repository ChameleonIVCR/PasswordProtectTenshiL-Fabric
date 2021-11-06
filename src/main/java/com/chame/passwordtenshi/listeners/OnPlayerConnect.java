package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class OnPlayerConnect {

    public static void listen(ServerPlayerEntity player) {
        PlayerSession playerSession = new PlayerSession(player);

        if(player.isDead()){
            if (player.getHealth() > 0.0F) {
                player.setHealth(0.0F);
            }
            MinecraftServer server = player.getServer();
            if (server == null) return;
            server.getPlayerManager().respawnPlayer(player, true);
        }

        if (playerSession.isAutoLogin() && player.getIp().equals(playerSession.getStoredIp())){
            playerSession.setSurvival();
            playerSession.setAuthorized(true);
            player.sendMessage(new LiteralText(
                    "§9You have been logged in automatically. Use /autologin to deactive this function.\n§aWelcome back."),
                    false
            );
        } else {
            playerSession.setSpectator();
            playerSession.authReminder();
            player.sendMessage(new LiteralText(
                    "§9Welcome to this shithole, to play, you must auth in.\n§eLog in using /login and register using /register"),
                    false
            );
        }

        PlayerStorage.addPlayerSession(playerSession);
    }
}
