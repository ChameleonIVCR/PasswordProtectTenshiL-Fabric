package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.*;

import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;

public class OnPlayerConnect {

    public static void listen(ServerPlayerEntity player) {
        
        PlayerSession playerSession = new PlayerSession(player);

        if(player.isDead()){
            if (player.getHealth() > 0.0F) {
                player.setHealth(0.0F);
            }
            ServerWorld playerWorld = player.getServerWorld();
            //TODO: Respawn player if dead.
            //Corrupts entity
            player.getServer().getPlayerManager().respawnPlayer(player, true);
            //player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(playerWorld.getDimension(), playerWorld.getRegistryKey(), -1, GameMode.SPECTATOR, player.interactionManager.getGameMode(), false, false, false));
        }

        if (player.getIp().equals(playerSession.getStoredIp()) && playerSession.isAutoLogin()){
            playerSession.setSurvival();
            playerSession.setAuthorized(true);
            player.sendMessage(new LiteralText("§9You have been logged in automatically. Use /autologin to deactive this function.\n§aWelcome back."), false);
            
        } else {
            playerSession.setSpectator();
            playerSession.authReminder();
            player.sendMessage(new LiteralText("§9Welcome to this shithole, to play, you must auth in.\n§eLog in using /login and register using /register"), false);
        }

        PlayerStorage.addPlayerSession(playerSession);
        //Dont really need this:
        //player.networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, new LiteralText("§aIdentify yourself")));
    }
}
