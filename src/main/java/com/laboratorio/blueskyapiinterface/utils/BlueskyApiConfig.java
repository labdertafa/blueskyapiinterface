package com.laboratorio.blueskyapiinterface.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 16/08/2024
 * @updated 17/08/2024
 */
public class BlueskyApiConfig {
    private static final Logger log = LogManager.getLogger(BlueskyApiConfig.class);
    private static BlueskyApiConfig instance;
    private final Properties properties;

    private BlueskyApiConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bluesky_api.properties")) {
            if (input == null) {
                log.error("No ha sido posible recuperar el fichero de configuración del API Bluesky. Finalizando aplicación!");
                System.exit(-1);
                return;
            }
            properties.load(input);
            input.close();
        } catch (IOException e) {
            log.error("Ha ocurrido un error leyendo el fichero de configuración del API de Bluesky. Finaliza la aplicación!");
            log.error(String.format("Error: %s", e.getMessage()));
            if (e.getCause() != null) {
                log.error(String.format("Causa: %s", e.getCause().getMessage()));
            }
            System.exit(-1);
        }
    }

    public static BlueskyApiConfig getInstance() {
        if (instance == null) {
            synchronized (BlueskyApiConfig.class) {
                if (instance == null) {
                    instance = new BlueskyApiConfig();
                }
            }
        }
        
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}