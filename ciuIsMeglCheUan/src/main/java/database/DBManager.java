package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    //? DOBBIAMO METTERLE NEL DIAGRAMMA DEI PACKAGE?
    private static final String DB_NOME = "database_prova";
    private static final String USER = "root";
    private static final String PASS = "";

    private static Connection conn = null;  //static per mantenere un'unica connessione condivisa nella JVM

    private DBManager() {}

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB_NOME, USER, PASS);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
