package com.wen.tools.domain.utils;


import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private Properties props = null;


    public Properties getProperties(String name) {
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

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
