package views;

//***IMPORT PACKAGES***
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utilities.SQLDatabase;

/**
 * ReportDisplayController Class
 *
 * @author David Roeske
 */
public class ReportDisplayController implements Initializable {
    //***REPORT DISPLAY SCREEN ELEMENTS***
    @FXML private TextArea reportTextArea;    
    @FXML private Label reportLabel; 
    
    //***METHOD TO SELECT WHICH DESIRED REPORT WILL RUN***
    public void populateFields(int value) {
      runReport(value); // Call method, pass argument as report to run parameter
    }
    
    //***METHOD TO DISPLAY CHOOSEN REPORT***
    public void runReport(int i) {
        if (i == 1) {   //Report: Number of appointment types by month
            try {
                reportLabel.setText("Number of appointment types by month");
                Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
                String queryAppointment = "SELECT description, MONTHNAME(start) as 'Month', "   // Query pull information from appointment table to string
                                        + "COUNT(*) as 'Total' FROM appointment "
                                        + "GROUP BY description, MONTH(start)";
                ResultSet queryResult = SQLStatement.executeQuery(queryAppointment);   // Our returned query
                StringBuilder reportInformation = new StringBuilder();  // New stringbuilder from report information
                reportInformation.append(String.format("%-50s %-50s %15s \n", "Appointment Month", "Appointment Type", "Total Appointments:"));
                reportInformation.append(String.join("", Collections.nCopies(111, "*")));
                reportInformation.append("\n");
                while(queryResult.next()) { // Until there is nothing else, print lines
                    reportInformation.append(String.format("%-55s %-45s %25d \n", 
                                            queryResult.getString("Month"), 
                                            queryResult.getString("description"), 
                                            queryResult.getInt("Total")));
                }
                SQLStatement.close();
                reportTextArea.setText(reportInformation.toString()); // Set text area with built string 
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            } 
        }
        
        if (i == 2) {   //Report: Schedule list for each consultant        
            try {
                reportLabel.setText("Schedule list for each consultant");
                Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
                String queryAppointment = "SELECT appointment.contact, appointment.description, "   // Query pull information from appointment and customer tables, join fields,
                                        + "customer.customerName, start, end "                      // and group to string
                                        + "FROM appointment JOIN customer ON "
                                        + "customer.customerId = appointment.customerId " 
                                        + "GROUP BY appointment.contact, MONTH(start), start";
                ResultSet queryResult = SQLStatement.executeQuery(queryAppointment);   // Our returned query
                StringBuilder reportInformation = new StringBuilder();  // New stringbuilder from report information
                reportInformation.append(String.format("%-30s %-30s %-30s %-30s\n", 
                    "Consultant:", "Customer:", "Appointment Type:", "Start Time"));
                reportInformation.append(String.join("", Collections.nCopies(111, "*")));
                reportInformation.append("\n");
                while(queryResult.next()) { // Until there is nothing else, print lines
                    reportInformation.append(String.format("%-30s %-30s %-30s %-30s\n", 
                                            queryResult.getString("contact"), 
                                            queryResult.getString("customerName"),
                                            queryResult.getString("description"),
                                            queryResult.getString("start")));
                }
                SQLStatement.close();
                reportTextArea.setText(reportInformation.toString()); // Set text area with built string 
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }
        
        if (i == 3) {   //Report: Total number of Appointments in the system         
            try {
                reportLabel.setText("Total number of Appointments in the system");
                Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
                String queryCustomer = "SELECT customer.customerName, COUNT(*) as 'Total' " // Query pull information from customer and appointment tables as select all to string
                                    + "FROM customer JOIN appointment " 
                                    + "ON customer.customerId = appointment.customerId GROUP BY customerName";
                ResultSet queryResult = SQLStatement.executeQuery(queryCustomer);   // Our returned query
                StringBuilder reportInformation = new StringBuilder();  // New stringbuilder from report information
                reportInformation.append(String.format("%-10s %70s \n", "Customer Name:", "Total Appointments:"));
                reportInformation.append(String.join("", Collections.nCopies(111, "*")));
                reportInformation.append("\n");
                while(queryResult.next()) { // Until there is nothing else, print lines
                    reportInformation.append(String.format("%-10s %60d \n", 
                                            queryResult.getString("customerName"), 
                                            queryResult.getInt("Total")));
                }
                SQLStatement.close();
                reportTextArea.setText(reportInformation.toString()); // Set text area with built string 
            } catch (SQLException e) {
                System.out.println("SQL Excpetion: " + e.getMessage());
            }      
        }
    }
    
    //***INITIALIZE***
    @Override public void initialize(URL url, ResourceBundle rb) {
    }    
    
} // End ReportDisplayController Class
