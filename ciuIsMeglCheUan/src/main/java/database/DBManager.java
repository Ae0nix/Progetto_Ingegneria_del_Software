package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static String dbUrl;
    private static String user;
    private static String pass;

    private static final ThreadLocal<Connection> threadConn = new ThreadLocal<>();

    private DBManager() {}

    public static void configure(String url, String username, String password) {
        dbUrl = url;
        user = username;
        pass = password;
    }

    public static Connection getConnection() throws SQLException {
        if (dbUrl == null || user == null || pass == null) {
            throw new SQLException("DBManager has not been configured with datasource properties.");
        }
        Connection conn = threadConn.get();
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(dbUrl, user, pass);
            threadConn.set(conn);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        Connection conn = threadConn.get();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        threadConn.remove();
    }
}