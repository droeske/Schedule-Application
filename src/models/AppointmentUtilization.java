package models;

//***IMPORT PACKAGES***
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.SQLDatabase;

/**
 * AppointmentUtilization Class
 *
 * @author David Roeske
 */
public class AppointmentUtilization {
    //***METHOD TO ADD NEW APPOINTMENT TO APPOINTMENT TABLE IN DATABASE***
    public static boolean addAppointment(int appointmentid, String appointmenttitle, String appointmentdescription, String appointmentcontact, String appointmentlocation, 
                                         String appointmentdate, String appointmentStartTime, String appointmentEndTime) {
        try {
            String timestampStart = makeTimeStampStart(appointmentdate, appointmentStartTime, appointmentlocation);    // Call method to set timestamp start time
            String timestampEnd = makeTimeStampEnd(appointmentdate, appointmentStartTime, appointmentlocation, appointmentEndTime);    // Call method to set timestamp end time     
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database            

            String queryAppointment = "INSERT INTO appointment SET customerId='" + appointmentid +     // Add appointment entry on appointment table to string
                    "', title='" + appointmenttitle + 
                    "', description='" + appointmentdescription + 
                    "', contact='" + appointmentcontact + 
                    "', location='" + appointmentlocation + 
                    "', start='" + timestampStart + 
                    "', end='" + timestampEnd + 
                    "', url='', "
                    + "createDate=NOW(), "
                    + "createdBy='', "
                    + "lastUpdate=NOW(), "
                    + "lastUpdateBy=''";
            int appointmentChange = SQLStatement.executeUpdate(queryAppointment);   // Execute the update
            if(appointmentChange == 1) {   // It executed the update
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
    
    //***METHOD TO MODIFY EXISTING APPOINTMENT IN DATABASE***
    public static boolean modifyAppointment(int appointmentid, String appointmenttitle, String appointmentdescription, String appointmentlocation, String appointmentcontact, 
                                            String appointmentdate, String appointmentStartTime, String appointmentEndTime) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database            
            String timestampStart = makeTimeStampStart(appointmentdate, appointmentStartTime, appointmentlocation);    // Call method to set timestamp start time
            String timestampEnd = makeTimeStampEnd(appointmentdate, appointmentStartTime, appointmentlocation, appointmentEndTime);    // Call method to set timestamp end time            
            String queryAppointment = "UPDATE appointment SET title='" + appointmenttitle +    // Update appointment entries on appointment table to string
                           "', description='" + appointmentdescription + 
                           "', contact='" + appointmentcontact + 
                           "', location='" + appointmentlocation + 
                           "', start='" + timestampStart + 
                           "', end='" + timestampEnd + 
                           "' WHERE appointmentId='" + appointmentid + 
                           "'";
            int appointmentChange = SQLStatement.executeUpdate(queryAppointment);   // Execute the update
            if(appointmentChange == 1) {   // It executed the update
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
    
    //***METHOD TO DELETE APPOINTMENT FROM DATABASE***
    public static boolean deleteAppointment(int appointmentid) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryAppointment = "DELETE FROM appointment WHERE appointmentId = " + appointmentid;  // Delete PRIMART KEY ( is entire entry) to string
            int appointmentChange = SQLStatement.executeUpdate(queryAppointment);   // Execute the update
            if(appointmentChange == 1) {   // It executed the update
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }        
    
    //***METHOD TO MAKE TIMESTAMP FOR START TIME/DATE***
    public static String makeTimeStampStart(String dateGrab, String timeGrab, String locationGrab) {
        String hoursHold = timeGrab.split(":")[0];
        int hoursby12 = Integer.parseInt(hoursHold);
        if(hoursby12 < 9) {hoursby12 += 12;}
        DateTimeFormatter ourFormattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        String thisDate = String.format("%s %02d:%s", dateGrab, hoursby12, "00");
        LocalDateTime thisDateTime = LocalDateTime.parse(thisDate, ourFormattedDate);
        ZoneId zoneIDVariable = ZoneId.systemDefault(); 
        if(locationGrab.equals("New York")) {
            zoneIDVariable = ZoneId.of("America/New_York");
        }
        if(locationGrab.equals("Phoenix")) {
            zoneIDVariable = ZoneId.of("America/Phoenix");
        }
        if (locationGrab.equals("London")) {
            zoneIDVariable = ZoneId.of("Europe/London");
        }
        ZonedDateTime zoneDateTime = thisDateTime.atZone(zoneIDVariable);      
        ZonedDateTime universalDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        thisDateTime = universalDateTime.toLocalDateTime();
        Timestamp thisTimestamptoReturn = Timestamp.valueOf(thisDateTime);
        return thisTimestamptoReturn.toString();    // Return timestamp to string
    }
    
    //**METHOD TO MAKE TIMESTAMP FOR END TIME/DATE***
    public static String makeTimeStampEnd(String dateGrab, String timeGrab, String locationGrab, String timeEndGrab) {  // FIXED method for computing end time
        String thisDate = "";   // Initilize string to be checked here
        String hoursHold = timeGrab.split(":")[0];
        int hoursby12 = Integer.parseInt(hoursHold);
        if(hoursby12 < 9) {hoursby12 += 12;}
        // Get timeEnd and use conditional statement for setting values
        if (timeEndGrab.contains("15")){
            thisDate = String.format("%s %02d:%s", dateGrab, hoursby12, "15");            
        }
        if (timeEndGrab.contains("30")){
            thisDate = String.format("%s %02d:%s", dateGrab, hoursby12, "30");            
        }
        if (timeEndGrab.contains("45")){
            thisDate = String.format("%s %02d:%s", dateGrab, hoursby12, "45");            
        }
        if (timeEndGrab.contains("Hour")){
            hoursby12 += 1;       
            thisDate = String.format("%s %02d:%s", dateGrab, hoursby12, "00");
        }        
        DateTimeFormatter ourFormattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");        
        LocalDateTime thisDateTime = LocalDateTime.parse(thisDate, ourFormattedDate);
        ZoneId zoneIDVariable = ZoneId.systemDefault();  
        if(locationGrab.equals("New York")) {
            zoneIDVariable = ZoneId.of("America/New_York");
        }
        if(locationGrab.equals("Phoenix")) {
            zoneIDVariable = ZoneId.of("America/Phoenix");
        }
        if (locationGrab.equals("London")) {
            zoneIDVariable = ZoneId.of("Europe/London");
        }
        ZonedDateTime zoneDateTime = thisDateTime.atZone(zoneIDVariable);
        ZonedDateTime universalDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        thisDateTime = universalDateTime.toLocalDateTime();
        Timestamp thisTimestamptoReturn = Timestamp.valueOf(thisDateTime); 
        return thisTimestamptoReturn.toString();    // Return timestamp to string
    }     
    
    //***METHOD TO RETRIEVE A CUSTOMERS MONTHLY APPOINTMENTS***
    public static ObservableList<Appointment> retrieveMonthlyAppointments (int customerid) {
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();        
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            Appointment thisAppointment;            
            LocalDate startDate = LocalDate.now();  // Current month
            LocalDate endDate = LocalDate.now().plusMonths(1);  // Mark the end by addition (months to add)
            String queryAppointment = "SELECT * FROM appointment WHERE customerId = '" + customerid + // Query appointment table at customer id under date specs to string
                           "' AND start >= '" + startDate + 
                           "' AND start <= '" + endDate + 
                           "'"; 
            ResultSet queryResult = SQLStatement.executeQuery(queryAppointment);   // Our returned query
            while(queryResult.next()) {
                thisAppointment = new Appointment(
                                  queryResult.getInt("appointmentId"), 
                                  queryResult.getInt("customerId"), 
                                  queryResult.getString("title"), 
                                  queryResult.getString("description"),
                                  queryResult.getString("location"), 
                                  queryResult.getString("contact"),                       
                                  queryResult.getString("start"),
                                  queryResult.getString("end"));
                appointments.add(thisAppointment);
            }
            SQLStatement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return null;
        }
    }
    
    //***METHOD TO RETRIEVE A CUSTOMERS WEEKLY APPOINTMENTS***    
    public static ObservableList<Appointment> retrieveWeeklyAppointments(int customerid) {
             ObservableList<Appointment> appointments = FXCollections.observableArrayList();       
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database            
            Appointment appointment;
            LocalDate startDate = LocalDate.now();  // Current week
            LocalDate endDate = LocalDate.now().plusWeeks(1);  // Mark the end by addition (weeks to add)
            String queryAppointment = "SELECT * FROM appointment WHERE customerId = '" + customerid + // Query appointment table at customer id under date specs to string 
                           "' AND start >= '" + startDate + 
                           "' AND start <= '" + endDate + 
                           "'";
            ResultSet queryResult = SQLStatement.executeQuery(queryAppointment);   // Our returned query
            while(queryResult.next()) {
                appointment = new Appointment(
                              queryResult.getInt("appointmentId"), 
                              queryResult.getInt("customerId"), 
                              queryResult.getString("title"), 
                              queryResult.getString("description"),
                              queryResult.getString("location"), 
                              queryResult.getString("contact"),                        
                              queryResult.getString("start"),
                              queryResult.getString("end"));
                appointments.add(appointment);
            }
            SQLStatement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return null;
        }
    }
    
    //***METHOD TO CHECK IF APPOINTMENT IS IN 15 MINUTES - CALLED IN MAIN CONTROLLER***
    public static Appointment checkAppointment() {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database            
            Appointment thisAppointment;
            LocalDateTime currentDateTime = LocalDateTime.now();
            ZoneId zodeId = ZoneId.systemDefault();
            ZonedDateTime zoneDateTime = currentDateTime.atZone(zodeId);
            LocalDateTime localDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();    // Createing range from start now
            LocalDateTime localDateTimeplus15 = localDateTime.plusMinutes(15);   // Time 15 from now
            String usernameLoggedIn = UserUtilization.getCurrentUser().getUsername();    // Who is logged in            
            String queryAppointment = "SELECT * FROM appointment WHERE start BETWEEN '" + localDateTime + // Query the range of time from appointment table to string
                                      "' AND '" + localDateTimeplus15 + 
                                      "' AND contact='" + usernameLoggedIn + 
                                      "'";
            ResultSet queryResult = SQLStatement.executeQuery(queryAppointment);   // Our returned query
            if(queryResult.next()) {
                thisAppointment = new Appointment(
                              queryResult.getInt("appointmentId"), 
                              queryResult.getInt("customerId"), 
                              queryResult.getString("title"), 
                              queryResult.getString("description"),
                              queryResult.getString("location"), 
                              queryResult.getString("contact"),                        
                              queryResult.getString("start"),
                              queryResult.getString("end"));

                return thisAppointment;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    //***METHOD TO CHECK IF APPOINTMENT IS ALREADY SCHEDULED***
    public static boolean overlappingAppointment(int appointmentid, String appointmentlocation, String appointmentdate, String appointmenttime) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database            
            String timestampStart = makeTimeStampStart(appointmentdate, appointmenttime, appointmentlocation);    // Call method to set timestamp start time
            String query = "SELECT * FROM appointment WHERE start = '" + timestampStart + // Query to get time and location from appointment table to string
                           "' AND location = '" + appointmentlocation + 
                           "'";
            ResultSet queryResult = SQLStatement.executeQuery(query);   // Our returned query
            if(queryResult.next()) {
                if(queryResult.getInt("appointmentId") == appointmentid) {
                    SQLStatement.close();
                    return false;
                }
                SQLStatement.close();
                return true;
            } else {
                SQLStatement.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQL Excpection: " + e.getMessage());
            return true;
        }
    }

} // End AppointmentUtilization Class