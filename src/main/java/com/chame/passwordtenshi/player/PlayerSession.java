package com.chame.passwordtenshi.player;

import com.chame.passwordtenshi.PasswordTenshi;
import com.chame.passwordtenshi.database.Database;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.UUID;

import org.apache.logging.log4j.Logger;

import java.util.concurrent.FutureTask;

public class PlayerSession {

    private boolean authorized;
    private final ServerPlayerEntity player;
    private final UUID uuid;
    private static Database database;
    private static PasswordTenshi pt;
    private static Logger logger;

    public PlayerSession(ServerPlayerEntity player){
        this.player = player;
        this.uuid = player.getUuid();
        this.authorized = false;
    }

    public static void setDatabase(Database db){
        database = db;
    }

    public static void initialize(PasswordTenshi ptt){
        pt = ptt;
        logger = pt.getMainLogger();
    }

    public UUID getUUID(){
        return uuid;
    }

    public boolean isAuthorized(){
        if (uuid == null) { return false; }
        return authorized;
    }

    public void setGameMode(String gamemode){
        //TODO: Research this later.
        this.player.changeGameMode(GameMode.byName(gamemode));
    }

    public void setAuthorized(boolean authorized) {
        logger.info(String.format("Set %s authorization to %s", uuid, authorized));
        this.authorized = authorized;
    }

    public String getPasswordHash(){
        return database.getPass(uuid.toString());
    }

    public ServerPlayerEntity getPlayer(){
        return this.player;
    }

    public void setPasswordHash(String hash){
        removePasswordHash();
        database.addPass(uuid.toString(), hash);
    }

    public void removePasswordHash() {
        database.deletePass(uuid.toString());
    }

    public void authReminder() {
        FutureTask<Boolean> task = new FutureTask<>(new PlayerRegisterReminder((PlayerSession) this));
        PasswordTenshi.getMainExecutor().execute(task);
    }
}