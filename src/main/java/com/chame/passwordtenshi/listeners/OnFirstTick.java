package com.chame.passwordtenshi.listeners;

import net.minecraft.server.MinecraftServer;

/**
 * Listens on the first tick after server start, this way, the MinecraftServer object is passed
 * onto a static variable to be used in the rest of the functions with require of checks.
 */

public class OnFirstTick {
    private static MinecraftServer minecraftServer;

    public static void setMinecraftServer(MinecraftServer mcServer) {
        minecraftServer = mcServer;
    }

    public static MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }
}