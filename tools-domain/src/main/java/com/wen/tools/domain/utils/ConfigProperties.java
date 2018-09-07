package com.wen.tools.domain.utils;

import com.wen.tools.domain.config.IConstantsDomain;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private static Properties props = null;

    static {
        props = getProperties(IConstantsDomain.ENV_PROPERTIES_FILE);
    }

    private static Properties getProperties(String name) {
        Properties pro = new Properties();
        try {
            ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(name);
            pro.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pro;
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
