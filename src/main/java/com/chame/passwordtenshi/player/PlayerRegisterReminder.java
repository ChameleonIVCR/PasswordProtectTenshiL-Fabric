package com.chame.passwordtenshi.player;

import net.minecraft.text.LiteralText;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerRegisterReminder implements Runnable {
    private final ServerPlayerEntity player;
    private final String passwordHash;

    public PlayerRegisterReminder(PlayerSession player){
        this.player = player.getPlayer();
        passwordHash = player.getPasswordHash();
    }

    @Override
    public void run() {
        final Boolean isAuthorized = PlayerStorage.getPlayerSession(player.getUuid()).isAuthorized();
        
        System.out.println(isAuthorized);
        System.out.println(!player.networkHandler.getConnection().isOpen());
        System.out.println(isAuthorized == null);
        
        if (!player.networkHandler.getConnection().isOpen() || isAuthorized || isAuthorized == null) {
            //Stop further repetitions. TODO: find a cleaner way.
            throw new RuntimeException("Reminder done.");
        }

        if(passwordHash == null) {
            player.sendMessage(new LiteralText("§3Please register by using §c/register.\n§3E.g. §c/register <password>"), false);
        } else {
            player.sendMessage(new LiteralText("§3To continue, please login by using §c/login."), false);
        }
    }
}