package com.chame.passwordtenshi.utils;

import com.chame.passwordtenshi.PasswordTenshi;

import java.io.*;
import java.util.*;

/**
 * The ConfigFile class allows to read from .properties files, validate them
 * against the defaults, and regenerate them in case the properties are invalid.
 * The regenerated files are simply unpacked from the .jar assets.
 */

public class ConfigFile {
    private final Properties properties;

    // Initializes the Hashmap and loads the properties into it.
    public ConfigFile() {
        properties = load();
    }

    // Returns the content of a Properties element. As the properties files is
    // only loaded once and is checked for errors at that time, it shouldn't
    // be possible to consult a non-existing Properties element.
    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    public void setProperty(String property, String value) {
        properties.setProperty(property, value);
    }

    public void save(){
        final String filename = "PasswordProtectTenshi.properties";
        File propertiesFile = new File("config/pptenshi/" + filename);
        propertiesFile.getParentFile().mkdirs();

        try {
            FileOutputStream fileos = new FileOutputStream(propertiesFile);
            this.properties.store(fileos, filename);
            fileos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties load() {
        final String filename = "PasswordProtectTenshi.properties";
        File propertiesFile = new File("config/pptenshi/" + filename);
        Properties locProperties = new Properties();
        Properties defProperties = new Properties();

        try {
            propertiesFile.getParentFile().mkdirs();
            defProperties.load(PasswordTenshi.class.getResourceAsStream("/assets/pptfabric/"+filename));

            if (propertiesFile.exists()) {
                FileInputStream fileis = new FileInputStream(propertiesFile);
                locProperties.load(fileis);
                fileis.close();

                if (locProperties.stringPropertyNames().equals(defProperties.stringPropertyNames())) {
                    return locProperties;
                }
            }

            FileOutputStream fileos = new FileOutputStream(propertiesFile);
            defProperties.store(fileos, filename);
            fileos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return defProperties;
    }
}