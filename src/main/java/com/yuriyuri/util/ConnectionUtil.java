package com.yuriyuri.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
    private ConnectionUtil(){
        throw new AssertionError("no");
    }

    public static Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql:///qg_bank", "root", "1234");
        return conn;
    }
}
