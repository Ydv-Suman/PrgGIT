package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection = null;

    public static Connection getConnection(String dbName) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:resources/" + dbName + ".db";
            connection =  DriverManager.getConnection(url);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("SQLite connection failed");
        }
    }

    public static void closeconnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

