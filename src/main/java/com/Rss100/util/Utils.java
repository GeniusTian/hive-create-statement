package com.Rss100.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wststart on 2020/3/25
 */
public class Utils {
    public static Properties getProperties(String configName) {
        Properties prop = new Properties();
        InputStream in = Utils.class.getClassLoader().getResourceAsStream(configName);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
