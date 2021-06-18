package com.chame.passwordtenshi.player;

import com.chame.passwordtenshi.listeners.OnFirstTick;
import com.chame.passwordtenshi.PasswordTenshi;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.TimeUnit;

public class PlayerRegisterReminder implements Runnable {
    private final PlayerSession player;
    private static Database database;
    private static Logger logger;

    public PlayerRegisterReminder(PlayerSession player){
        this.player = player;
    }

    @Override
    public void call(){
        final int[] times = {0};

        //Will return null if the player has disconnected, the ternary is possibly not needed, but i'll leave it there for testing
        //TODO: Remove ternary after testing.
        while (OnFirstTick.getMinecraftServer().getPlayerManager().getPlayer(this.player.getUUID()) == null ? false : true 
        //&& this.player.networkHandler.getConnection().isOpen()
        ){ 
            
            if(!this.player.isAuthorized()){
                if (this.player.getPasswordHash() != null){
                    player.getPlayer().sendMessage(new LiteralText("Please register by using /register, then inputing your password."), false);
                } else {
                    player.getPlayer().sendMessage(new LiteralText("Please login by using /login."), false);
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
                this.player.networkHandler.disconnect(new LiteralText("You've been disconnected by uhh, login timeout."));
                break;
            }
        }
    }
}