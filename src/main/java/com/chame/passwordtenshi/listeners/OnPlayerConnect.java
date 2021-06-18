package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.player.*;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class OnPlayerConnect {

    public static void listen(ServerPlayerEntity player) {
        
        PlayerSession playerSession = new PlayerSession(player);
        
        //Either this, or cancel actions with a inject. Let's try this.
        playerSession.setSpectator();
        PlayerStorage.addPlayerSession(playerSession);

        //Reminder
        playerSession.authReminder();

        //player.setInvulnerable(true);
        player.sendMessage(new LiteralText("§9Welcome to this shithole, to play, you must auth in.\n§eLog in using /login and register using /register"), false);
        
        //Dont really need this:
        //player.networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, new LiteralText("§aIdentify yourself")));
    }
}
