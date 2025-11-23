package src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static Connection getConnection(String dbName) throws SQLException {
        try {
            // Example dbName = "resources/company"
            String url = "jdbc:sqlite:" + dbName + ".db";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("SQLite connection failed");
        }
    }
}

