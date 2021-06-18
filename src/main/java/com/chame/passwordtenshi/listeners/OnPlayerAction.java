package com.chame.passwordtenshi.listeners;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerAction {
    public static boolean canInteract(ServerPlayNetworkHandler networkHandler) {
/*         ServerPlayerEntity player = networkHandler.player;
        PlayerLogin playerLogin = LoginMod.getPlayer(player);
        boolean isLoggedIn = playerLogin.isLoggedIn();
        return isLoggedIn; */
        //We may not need this, really.
        return true;
    }
}
