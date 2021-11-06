package com.chame.passwordtenshi;

import com.chame.passwordtenshi.player.*;
import com.chame.passwordtenshi.commands.*;
import com.chame.passwordtenshi.utils.*;
import com.chame.passwordtenshi.database.Database;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import org.h2.tools.Server;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PasswordTenshi implements DedicatedServerModInitializer {
    private final ConfigFile config = new ConfigFile();
    private static final List<PlayerPendingLogin> playersPendingLogin = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Override
    public void onInitializeServer() {
        PlayerStorage.initialize();
        initializeDatabase();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            Login.register(dispatcher);
            Register.register(dispatcher);
            Unregister.register(dispatcher);
            UnregisterPlayer.register(dispatcher);
            AutoLogin.register(dispatcher);
        });
        scheduler.scheduleAtFixedRate(new PlayerRegisterReminder(playersPendingLogin), 0, 12, TimeUnit.SECONDS);
    }

    public static ScheduledExecutorService getMainExecutor(){
        return scheduler;
    }

    public static void addPlayerPendingLogin(ServerPlayerEntity player){
        playersPendingLogin.add(new PlayerPendingLogin(player));
    }

    private void initializeDatabase(){
        File directory = new File("config/pptenshi/");
        Path path = Paths.get(directory.getAbsolutePath(), config.getProperty("databaseName"));
        
        Database database = new Database(
                path.toString(),
                config.getProperty("databaseUser"),
                config.getProperty("databasePassword")
        );

        database.check();
        PlayerSession.setDatabase(database);
    }

    public static class PlayerPendingLogin{
        public ServerPlayerEntity player;
        public int reminderCounter = 0;

        public PlayerPendingLogin(ServerPlayerEntity p){
            player = p;
        }
    }
}
