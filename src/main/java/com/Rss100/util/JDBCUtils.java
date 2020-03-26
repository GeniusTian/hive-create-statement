package com.Rss100.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by wststart on 2020/3/25
 */
public class JDBCUtils {
    //声明一个静态私有属性
    private static DataSource ds;

    static {
        try {
            //1.创建一个Properties对象
            Properties prop = Utils.getProperties("druid.properties");
            //3.根据配置文件创建datasource
            ds = DruidDataSourceFactory.
                    createDataSource(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取连接的方法
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    //关流的方法 需要关闭 Statement Connection 对象
    public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getQuery(String sql) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Connection conn = getConnection();
        try {
            pstmt = conn.prepareStatement(sql);
            resultSet = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
