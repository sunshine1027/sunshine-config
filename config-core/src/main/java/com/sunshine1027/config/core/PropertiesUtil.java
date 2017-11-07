package com.sunshine1027.config.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static com.sunshine1027.config.core.BaseConstants.CONFIG_PROPERTIES;

/**
 * @author sunshine1027 [sunshine10271993@gmail.com]
 */
public class PropertiesUtil {
    public static Properties loadProperties(String propertyFileName) {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(propertyFileName);
            if (url != null) {
                in = new FileInputStream(url.getPath());
                if (in != null) {
                    prop.load(in);
                }
            }
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return prop;
    }

    public static String getProperties(String key) {
        return loadProperties(CONFIG_PROPERTIES).getProperty(key);
    }
}
