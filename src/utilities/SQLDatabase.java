package utilities;

//***IMPORT PACKAGES***
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Database Class
 *
 * @author David Roeske
 */
public class SQLDatabase {
    
    //Values needed to connect to database, once database is set up
    private static final String DATABASENAME = "";
    private static final String URL = "" + DATABASENAME;
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Driver in added library
    private static Connection myConnection;
     
    //***METHOD TO CONNECT TO DATABASE***
    public static void connect() {
        try {
            Class.forName(DRIVER);  // Load SQLDriver
            myConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);    // Call getConnection to create connection
            System.out.println("***Connected to SQL Database***");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Exceptions: " + e.getMessage()); 
        }
    }
    
    //***METHOD TO DISCONNECT FROM DATABASE***
    public static void disconnect() {
        try {
            myConnection.close();   // Call close to current connection 
            System.out.println("***Disconnected From SQL Database***");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
    
    //***GETTER***
    public static Connection getConnection() {
        return myConnection;
    }
    
} // End Database Class 