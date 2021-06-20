package com.chame.passwordtenshi.player;

import com.chame.passwordtenshi.PasswordTenshi;
import com.chame.passwordtenshi.database.Database;
import com.chame.passwordtenshi.utils.RandomRGBColor;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.world.GameMode;

import java.util.UUID;

import java.util.concurrent.FutureTask;

public class PlayerSession {

    private Boolean authorized = false;
    private Boolean hasChangedAutoLogin = false;
    private String password;
    private String storedIp;
    private Boolean autologin;
    private AreaEffectCloudEntity playerCloud;
    private final ServerPlayerEntity player;
    private final UUID uuid;
    private static Database database;

    public PlayerSession(ServerPlayerEntity player) {
        this.player = player;
        this.uuid = player.getUuid();
        fetchDatabase();
    }

    public UUID getUUID() {
        return uuid;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public boolean isAuthorized() {
        if (uuid == null)
            return false;
        return authorized;
    }

    public void setSpectator() {
        player.changeGameMode(GameMode.SPECTATOR);
        playerCloud = new AreaEffectCloudEntity(player.getServerWorld(), player.getX(), player.getY(), player.getZ());

        //Time is in ticks. It's a ratio of 20 TPS to 1 second. If the server is under heavy load, a.k.a TPS reduction,
        //this timer would be longer than intended. That's not a problem as we want a long timeout anyway.
        playerCloud.setDuration(36000);
        playerCloud.setDurationOnUse(36000);

        //Random Color
        playerCloud.setColor(RandomRGBColor.getColor());

        player.getServerWorld().spawnEntity(playerCloud);
        player.setCameraEntity(playerCloud);
    }

    public void setSurvival() {
        if (playerCloud != null) {
            playerCloud.setDuration(0);
            playerCloud.setDurationOnUse(0);
            playerCloud = null;
        }

        player.changeGameMode(GameMode.SURVIVAL);
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getStoredIp() {
        return storedIp;
    }

    public Boolean isAutoLogin() {
        return autologin;
    }

    public Boolean hasChangedAutoLogin() {
        return hasChangedAutoLogin;
    }

    public void setAutoLogin(Boolean value) {
        database.changeAutoLogin(uuid.toString(), value);
        this.autologin = !this.autologin;
        hasChangedAutoLogin = true;
    }

    public String getPasswordHash() {
        return password;
    }

    public void addUser(String hash) {
        database.addUser(uuid.toString(), hash, this.player.getIp());
        password = hash;
        autologin = false;
        storedIp = this.player.getIp();
    }

    public void removeUser() {
        database.removeUser(uuid.toString());
        password = null;
        autologin = null;
        storedIp = null;
    }

    public void authReminder() {
        FutureTask<Boolean> task = new FutureTask<>(new PlayerRegisterReminder((PlayerSession) this));
        PasswordTenshi.getMainExecutor().execute(task);
    }

    public static void setDatabase(Database db) {
        database = db;
    }

    private void fetchDatabase() {
        final String[] fetchResult = database.fetchUser(this.uuid.toString());
        if (fetchResult == null)
            return;

        this.password = fetchResult[0];
        this.autologin = Boolean.parseBoolean(fetchResult[1]);
        this.storedIp = fetchResult[2];
    }
}