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

        //Will return null if the player has disconnected, the ternary is possibly not needed, but i'll leave it there for testing
        //TODO: Remove ternary after testing.
        while (
            //OnFirstTick.getMinecraftServer().getPlayerManager().getPlayer(this.playerSession.getUUID()) == null ? false : true 
            this.playerSession.getPlayer().networkHandler.getConnection().isOpen()
        ){ 
            
            if(!this.playerSession.isAuthorized()){
                if (this.playerSession.getPasswordHash() != null){
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
                //This should kick the player, or at least, try to.
            }
            times[0]++;
            if (times[0] >= 12){
                //TODO: May not work due to be running on a thread.
                this.playerSession.getPlayer().networkHandler.disconnect(new LiteralText("You've been disconnected by uhh, login timeout."));
                break;
            }
        }
        return true;
    }
}