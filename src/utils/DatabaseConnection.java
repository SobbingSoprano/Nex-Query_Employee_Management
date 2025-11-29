/**
 * Database connection utility class using Singleton pattern.
 * This class manages the MySQL database connection for the application.
 * Ensures only one database connection instance exists throughout the application.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database credentials - UPDATE THESE WITH YOUR DATABASE INFO
    private static final String URL = "jdbc:mysql://localhost:3306/Schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Update with your MySQL password
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MySQL JDBC Driver not found.");
            System.err.println("Please ensure mysql-connector-java is in the lib folder.");
            e.printStackTrace();
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to database.");
            System.err.println("Connection details:");
            System.err.println("  URL: " + URL);
            System.err.println("  Username: " + USERNAME);
            System.err.println("Please check:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database name is correct (currently: 'Schema')");
            System.err.println("  3. Username and password are correct");
            System.err.println("  4. Port 3306 is accessible");
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
