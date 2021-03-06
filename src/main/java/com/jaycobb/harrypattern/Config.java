package com.jaycobb.harrypattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private final static Properties prop = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            prop.load(input);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getProperty(final String key) {
        return prop.getProperty(key);
    }

    public static String getProperty(final String key, final String defaultValue) {
        final String property = prop.getProperty(key);
        if (property != null) {
            return property;
        }
        return defaultValue;
    }

}
