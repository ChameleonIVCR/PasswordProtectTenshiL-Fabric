package com.chame.passwordtenshi;

import com.chame.passwordtenshi.player.*;
import com.chame.passwordtenshi.commands.*;
import com.chame.passwordtenshi.utils.*;
import com.chame.passwordtenshi.database.Database;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


//TODO: Respawn player if dead.
//TODO: Fix the reminder not calling sometimes after player leaves and connects again.

public class PasswordTenshi implements DedicatedServerModInitializer {
    private final ConfigFile config = new ConfigFile();
    // Amount of threads to use.
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
    }

    public static ScheduledExecutorService getMainExecutor(){
        return scheduler;
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
}
