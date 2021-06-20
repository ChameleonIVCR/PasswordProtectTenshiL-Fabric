package com.chame.passwordtenshi.player;

import net.minecraft.text.LiteralText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

public class PlayerRegisterReminder implements Callable<Boolean> {
    private final PlayerSession playerSession;

    public PlayerRegisterReminder(PlayerSession player){
        this.playerSession = player;
    }

    @Override
    public Boolean call(){
        final int[] times = {0};
        final String passwordHash = playerSession.getPasswordHash();
        
        while (playerSession.getPlayer().networkHandler.getConnection().isOpen()){ 
            
            if(!playerSession.isAuthorized()){
                if (passwordHash == null){
                    playerSession.getPlayer().sendMessage(new LiteralText("Please register by using /register, then inputing your password."), false);
                } else {
                    playerSession.getPlayer().sendMessage(new LiteralText("Please login by using /login."), false);
                }
            } else{
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e){
                //pass
            }
            times[0]++;
            if (times[0] >= 12){
                playerSession.getPlayer().networkHandler.disconnect(new LiteralText("You've been disconnected by uhh, login timeout."));
                break;
            }
        }
        return true;
    }
}