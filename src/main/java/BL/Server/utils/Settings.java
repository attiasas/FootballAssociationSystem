package BL.Server.utils;

import java.io.IOException;
import java.util.Properties;

public final class Settings {

    public static final String PROPERTY_FILE = "config.properties";
    private static Properties props = null;

    /**
     *  initialize properties file
     */
    private static void initializeProperties() {
        if(props == null) props = loadProperties();
    }

    /**
     * Returns the property value for the given key or null if it does not exist
     * Key/value must be defined in "config.properties"
     *
     * @param key key-name
     * @return Property value for the given key
     */
    public static String getPropertyValue(String key){
        initializeProperties();
        return props.getProperty(key);
    }

    /**
     * Utility method that builds the DEV-connection string using the property values: db.server , db.port and db.database
     * @return
     *  a connection string formatted like this: "jdbc:mysql://localhost:3306/sportify"
     */
    public static String getDEV_DBConnection(){
        initializeProperties();
        return String.format("jdbc:mysql://%s:%s/%s",props.getProperty("db.server"),props.getProperty("db.port"),props.getProperty("db.database"));
    }

    /**
     * Utility method that builds the TEST-connection string using the property values: db.server , db.port and dbtest.database
     * @return
     *  a connection string formatted like this: "jdbc:mysql://localhost:3306/sportify_test"
     */
    public static String getTEST_DBConnection(){
        initializeProperties();
        return String.format("jdbc:mysql://%s:%s/%s", props.getProperty("db.server"), props.getProperty("dbtest.port"), props.getProperty("dbtest.database"));
    }

    /**
     * Load a new properties file from all configuration in the `PROPERTY_FILE`
     * @serialData   config.properties
     * @return a filled properties file
     * @throws RuntimeException if file cannot be loaded
     */
    private static Properties loadProperties() {
        Properties allProps = new Properties();
        try {
            allProps.load(Settings.class.getClassLoader().getResourceAsStream(PROPERTY_FILE));
            for (Object key : allProps.keySet()) {
                allProps.setProperty((String) key, allProps.getProperty((String) key).trim());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not load properies in file: " + PROPERTY_FILE);
        }
        return allProps;
    }
}