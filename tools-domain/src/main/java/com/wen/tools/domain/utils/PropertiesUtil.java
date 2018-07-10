package com.wen.tools.domain.utils;

import com.wen.tools.domain.config.IConstants;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties props = null;

    static {
        props = getProperties ( IConstants.CONFIG.CONFIG_PROPERTIES );
    }

    public static Properties getProperties(String name) {
        Properties pro = new Properties ();
        try {
            ClassLoader classLoader = PropertiesUtil.class.getClassLoader ();
            InputStream inputStream = classLoader.getResourceAsStream ( name );
            pro.load ( inputStream );
            inputStream.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return pro;
    }
    public static String getProperty(String key) {
        return props.getProperty(key);
    }

}
