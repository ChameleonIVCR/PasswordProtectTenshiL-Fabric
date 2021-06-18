package com.chame.passwordtenshi.listeners;

import net.minecraft.server.network.ServerPlayNetworkHandler;

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
