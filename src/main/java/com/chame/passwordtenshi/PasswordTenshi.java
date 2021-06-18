package com.chame.passwordtenshi;

import com.chame.passwordtenshi.player.*;
import com.chame.passwordtenshi.database.*;
import com.chame.passwordtenshi.commands.*;
import com.chame.passwordtenshi.utils.*;

import org.apache.logging.log4j.Logger;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordTenshi implements DedicatedServerModInitializer {
    private final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager.getRootLogger();
    private final ConfigFile config = new ConfigFile();
    //Amount of threads to use.
    private static final ExecutorService scheduler = Executors.newFixedThreadPool(2);

    @Override
    public void onInitializeServer() {

        PlayerSession.initialize(this);
        PlayerStorage.initialize();
        initializeDatabase();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            Login.register(dispatcher);
            Register.register(dispatcher);
        });
        
        this.logger.addFilter(new LogFilter());
        logger.info("PPTenshi is here to protect your server <3");
    }

    public static ExecutorService getMainExecutor(){
        return scheduler;
    }

    public Logger getMainLogger(){
        return this.logger;
    }

    private void initializeDatabase(){
        File directory = new File("config/pptenshi/");
        Path path = Paths.get(directory.getAbsolutePath(), config.getProperty("databaseName"));
        
        Database database = new Database(path.toString(), config.getProperty("databaseUser"), config.getProperty("databasePassword"));

        if (!database.check()){
            logger.info("PPTenshi has encountered an error when writing and/or reading the H2 local database");
            logger.info("Check your file system permissions and the credentials in the config.yml file.");
            logger.info("The default credentials are 'dontchangeme' and should not be changed after the database is first created.");
            logger.info("Authentication will not be possible until you fix the problem.");
        } else {
            logger.info("PPTenshi has successfully established a connection with your H2 local database and will now use it to store credentials.");
        }
        PlayerSession.setDatabase(database);
    }
}
