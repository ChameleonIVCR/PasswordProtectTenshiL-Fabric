package com.chame.passwordtenshi.utils;

import com.chame.passwordtenshi.PasswordTenshi;

import java.io.*;
import java.util.*;
import java.util.HashMap;

/**
 * The ConfigFile class allows to read from .properties files, validate them
 * against the defaults, and regenerate them in case the properties are invalid.
 * The regenerated files are simply unpacked from the .jar assets.
 * TODO: Add write to file support.
 */

public class ConfigFile {
    private final HashMap<String, Properties> propertiesStorage;

    // Initializes the Hashmap and loads the properties into it.
    public ConfigFile() {
        propertiesStorage = new HashMap<>();
        propertiesStorage.put("pptfabric", loadProperties("pptfabric"));
    }

    // Returns the content of a Properties element. As the properties files is
    // only loaded once and is checked for errors at that time, it shouldn't
    // be possible to consult a non-existing Properties element.
    public String getProperty(String property) {
        Properties properties = propertiesStorage.get("pptfabric");
        return properties.getProperty(property);
    }

    private static Properties loadProperties(String name) {
        final String pathProperties = "assets/pptfabric/" + name + ".properties";
        File propertiesFile = new File("config/pptenshi/" + name + ".properties");
        Properties properties = new Properties();
        Properties defProperties = new Properties();

        try {
            File directory = new File("config/pptenshi/");
            directory.mkdirs();
            defProperties.load(PasswordTenshi.class.getResourceAsStream("/" + pathProperties));

            if (propertiesFile.exists()) {
                FileInputStream fileis = new FileInputStream(propertiesFile);
                properties.load(fileis);
                fileis.close();

                // If the loaded .properties file doesn't match the default format,
                // delete it and extract it from the .jar again.
                if (!properties.stringPropertyNames().equals(defProperties.stringPropertyNames())) {

                    if (!propertiesFile.delete()) {
                        throw new IOException();
                    }
                    FileOutputStream fileos = new FileOutputStream(propertiesFile);
                    defProperties.store(fileos, name + ".properties");
                    fileos.close();
                    return defProperties;
                } else {
                    return properties;
                }
            } else {
                FileOutputStream fileos = new FileOutputStream(propertiesFile);
                defProperties.store(fileos, name + ".properties");
                fileos.close();
                return defProperties;
            }
            // Returns null if there was any I/O error, again, i should handle
            // this later.
            // TODO: Handle this.
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        } catch (IOException e) {
            System.out.println("Can't write to storage, check the file system permissions");
            return null;
        }
    }
}