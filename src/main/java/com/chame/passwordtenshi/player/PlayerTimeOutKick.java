package com.chame.passwordtenshi.player;

import net.minecraft.text.LiteralText;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTimeOutKick implements Runnable {
    private final ServerPlayerEntity player;

    public PlayerTimeOutKick(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public void run() {
        final Boolean isAuthorized = PlayerStorage.getPlayerSession(player.getUuid()).isAuthorized();
        
        if (!player.networkHandler.getConnection().isOpen() || isAuthorized || isAuthorized == null) {
            return;
        }
        player.networkHandler.disconnect(new LiteralText("You have been disconnected by login timeout."));
    }
}