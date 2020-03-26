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

    public static void putTableInfo() {

    }

    public static String getPartition(String... partitions) {
        if (partitions == null) {
            return "";
        } else {
            StringBuilder strPartition = new StringBuilder();
            for (int i = 0; i < partitions.length; i++) {
                strPartition.append(partitions[i]);
                if (i < partitions.length - 1) {
                    strPartition.append(", ");
                }
            }
            return strPartition.toString();
        }
    }
}
