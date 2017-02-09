package com.github.show0611.showPlugins.mserver.utilities;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created by show0611 on 2017/01/28.
 */
public class SQLite {
    private static Class JDBC;
    private Connection con = null;
    private Statement stat = null;
    private File db;

    static {
        try {
            JDBC = Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void open(String path) throws SQLException, IOException {
        db = new File(path);
        con = DriverManager.getConnection(path);
        stat = con.createStatement();
    }

    public ResultSet executeQuery(String cmd) throws SQLException {
        return stat.executeQuery(cmd);
    }

    public boolean execute(String cmd) throws SQLException {
        return stat.execute(cmd);
    }
}
