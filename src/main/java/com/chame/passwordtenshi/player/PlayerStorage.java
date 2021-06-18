package com.chame.passwordtenshi.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStorage {
    private static ConcurrentHashMap<UUID, PlayerSession> playerstorage;

    public static void initialize(){
        playerstorage = new ConcurrentHashMap<>();
    }

    public static void addPlayerSession(PlayerSession playersession){
        playerstorage.put(playersession.getUUID(), playersession);
    }

    public static PlayerSession getPlayerSession(UUID uuid){
        return playerstorage.get(uuid);
    }

    public static void removePlayerSession(UUID uuid){
        playerstorage.remove(uuid);
    }
}