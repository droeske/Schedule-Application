package models;

//***IMPORT PACKAGES***
import java.io.*;
import java.sql.*;
import java.time.ZonedDateTime;
import utilities.SQLDatabase;

/**
 * UserUtilization Class
 *
 * @author David Roeske
 */
public class UserUtilization {
    //***VARIABLE DECLARATION***      
    private static User currentUsername;
    
    //***GETTER***      
    public static User getCurrentUser() {
        return currentUsername;
    }
    
    //***METHOD TO OUTPUT LOGIN STATUS***
    public static void writeLogMethod(String username, boolean ifLogin) {
        try (FileWriter filewrite = new FileWriter("databaselog.txt", true);    // Specify file to write to
            BufferedWriter buffwrite = new BufferedWriter(filewrite);   // Append file
            PrintWriter printwrite = new PrintWriter(buffwrite)) {      // Write to file contents below
            printwrite.print("::USERNAME::   " + username + "   ::TIMESTAMP::   " + ZonedDateTime.now());
            if (ifLogin) {  // Logon was successfull
                printwrite.println("   ::LOGIN::   -Success-");
            } 
            if (!ifLogin){  // Logon failed
                printwrite.println("   ::LOGIN::   -Failure-");
            }
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }
    
    //***METHOD TO ATTEMPT TO LOGON TO SYSTEM***
    public static Boolean systemLogon(String username, String password) {   // We will check both username and password
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String query = "SELECT * FROM user WHERE userName='" + username +   // Query SQL database
                           "' AND password='" + password + 
                           "'";
            ResultSet queryResult = SQLStatement.executeQuery(query);   // Our returned query
            if(queryResult.next()) {
                currentUsername = new User();
                currentUsername.setUsername(queryResult.getString("userName"));
                SQLStatement.close();
                writeLogMethod(username, true); // Call method to write in log - User logged on
                return true;
            } else {
                writeLogMethod(username, false); //Call method to write in log - User failed logon
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exceptions: " + e.getMessage());
            return false;
        }
    }
    
} // End UserUtilization Class