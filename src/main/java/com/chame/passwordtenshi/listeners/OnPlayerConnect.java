package com.chame.passwordtenshi.listeners;

import com.chame.passwordtenshi.LoginMod;
import com.chame.passwordtenshi.PlayerLogin;
import com.chame.passwordtenshi.player.*;

import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;


public class OnPlayerConnect {

    public static void listen(ServerPlayerEntity player) {
        
        PlayerSession playerSession = new PlayerSession(player);
        
        //Either this, or cancel actions with a inject. Let's try this.
        playerSession.setGamemode("SPECTATOR");
        PlayerStorage.addPlayerSession(playerSession);

        //Reminder
        FutureTask<Boolean> task = new FutureTask<>(new PlayerRegisterReminder(playerSession));
        PasswordTenshi.getMainExecutor().execute(task);

        //player.setInvulnerable(true);
        player.sendMessage(new LiteralText("§9Welcome to this shithole, to play, you must log in.\n§eLog in using /login and register using /register"), false);
        player.networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, new LiteralText("§aIdentify yourself")));
    }
}
