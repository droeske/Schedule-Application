package models;

//***IMPORT PACKAGES***
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Appointment Class
 *
 * @author David Roeske
 */
public class Appointment {
    //***VARIABLE DECLARATION***     
    private final SimpleIntegerProperty appointmentId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty appointmentCustomerId = new SimpleIntegerProperty();
    private final SimpleStringProperty appointmentTitle = new SimpleStringProperty(); 
    private final SimpleStringProperty appointmentDescription = new SimpleStringProperty();
    private final SimpleStringProperty appointmentLocation = new SimpleStringProperty();    
    private final SimpleStringProperty appointmentContact = new SimpleStringProperty();    
    private final SimpleStringProperty appointmentStart = new SimpleStringProperty();
    private final SimpleStringProperty appointmentEnd = new SimpleStringProperty();

    //***CONSTRUCTORS***     
    public Appointment() {} // Empty constructor
    
    public Appointment(int appointmentid, int appointmentcustomerId, String appointmenttitle, String appointmentdescription, String appointmentlocation, 
                      String appointmentcontact, String appointmentstart, String appointmentend) {
        setAppointmentId(appointmentid);
        setAppointmentCustomerId(appointmentcustomerId);
        setAppointmentTitle(appointmenttitle);  
        setAppointmentDescription(appointmentdescription);
        setAppointmentLocation(appointmentlocation);
        setAppointmentContact(appointmentcontact);        
        setAppointmentStart(appointmentstart);
        setAppointmentEnd(appointmentend);  
    }
    
    //***GETTERS***      
    public int getAppointmentId() {
        return appointmentId.get();
    }
    
    public int getAppointmentCustomerId() {
        return appointmentCustomerId.get();
    }
    
    public String getAppointmentTitle() {
        return appointmentTitle.get();
    }
    
    public String getAppointmentDescription() {
        return appointmentDescription.get();
    }
    
    public String getAppointmentLocation() {
        return appointmentLocation.get();
    }
    
    public String getAppointmentContact() {
        return appointmentContact.get();
    }    
    
    public String getAppointmentStart() {
        return appointmentStart.get();
    } 
    
    public String getAppointmentEnd() {
        return appointmentEnd.get();
    }

    //***GETTERS - PROPERTIES***
    public StringProperty getAppointmentTitleProperty() {
        return this.appointmentTitle;
    }
    
    public StringProperty getAppointmentDescriptionProperty() {
        return this.appointmentDescription;
    }
    
    public StringProperty getAppointmentLocationProperty() {
        return this.appointmentLocation;
    }
    
    public StringProperty getAppointmentContactProperty() {
        return this.appointmentContact;
    }
    
    public StringProperty getAppointmentStartProperty() {
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // Set DateTime format we need 
 	LocalDateTime firstLocalDateTime = LocalDateTime.parse(this.appointmentStart.getValue(), formatDateTime); // Pass in our format to localDatTime
        ZonedDateTime zoneDateTime = firstLocalDateTime.atZone(ZoneId.of("UTC"));    // Use firstLocalDateTime(formatted)
        ZoneId zoneID = ZoneId.systemDefault(); // This instance zone
        ZonedDateTime universalTime = zoneDateTime.withZoneSameInstant(zoneID); 
        StringProperty ourDateTime = new SimpleStringProperty(universalTime.toLocalDateTime().toString()); // Set our time
        return ourDateTime;  // Return our date and time as start timestamp
    } 
    
    public StringProperty getAppointmentEndProperty() {
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // Set DateTime format we need 
 	LocalDateTime firstLocalDateTime = LocalDateTime.parse(this.appointmentEnd.getValue(), formatDateTime); // Pass in our format to localDatTime
        ZonedDateTime zoneDateTime = firstLocalDateTime.atZone(ZoneId.of("UTC"));    // Use firstLocalDateTime(formatted)
        ZoneId zoneID = ZoneId.systemDefault(); // This instance zone
        ZonedDateTime universalTime = zoneDateTime.withZoneSameInstant(zoneID); 
        StringProperty ourDateTime = new SimpleStringProperty(universalTime.toLocalDateTime().toString()); // Set our time
        return ourDateTime;  // Return our date and time as end timestamp
    }

    //***SETTERS***
    public void setAppointmentId(int appointmentid) {
        this.appointmentId.set(appointmentid);
    }
    
    public void setAppointmentCustomerId(int appointmentcustomerid) {
        this.appointmentCustomerId.set(appointmentcustomerid);
    }
    
    public void setAppointmentTitle(String appointmenttitle) {
        this.appointmentTitle.set(appointmenttitle);
    }
    
    public void setAppointmentDescription(String appointmentdescription) {
        this.appointmentDescription.set(appointmentdescription);
    }
    
    public void setAppointmentLocation(String appointmentlocation) {
        this.appointmentLocation.set(appointmentlocation);
    }
    
    public void setAppointmentContact(String appointmentcontact) {
        this.appointmentContact.set(appointmentcontact);
    }    
    
    public void setAppointmentStart(String appointmentstart) {
        this.appointmentStart.set(appointmentstart);
    }    
    
    public void setAppointmentEnd(String appointmentend) {
        this.appointmentEnd.set(appointmentend);
    }
    
    //***METHOD TO RETURN LOCALTIME TO MAIN CONTROLLER***
    public String appointment15MinuteAlert() {
        //First format
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // Set DateTime format we need
 	LocalDateTime firstLocalDateTime = LocalDateTime.parse(this.appointmentStart.getValue(), formatDateTime); // Pass in our format to localDatTime
        //Next step
        ZonedDateTime zoneDateTime = firstLocalDateTime.atZone(ZoneId.of("UTC"));    // Use firstLocalDateTime(formatted)
        ZoneId zoneID = ZoneId.systemDefault(); // This instance zone
        ZonedDateTime universalTime = zoneDateTime.withZoneSameInstant(zoneID); 
        //Next step
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm");     
	LocalTime ourTime = LocalTime.parse(universalTime.toString().substring(11,16), tFormatter); // Set our time      
        return ourTime.toString();  // Return our time cast as string
    }
         
    //***METHOD TO GET THE APPOINTMENT DATE***
    public LocalDate appointmentDateGrab() { 
        //Variable declaration
        LocalDate localDateNow;   
        ZoneId zoneIDVariable;// = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime;       
        Timestamp appointmentStartTimestamp = Timestamp.valueOf(this.appointmentStart.get()); // Getting appointmentStart, setting timestamp

        if(this.appointmentLocation.get().equals("New York")) {     // Check to see location of appointment to set zone
            zoneIDVariable = ZoneId.of("America/New_York");     // Set zoneId
        }
        if(this.appointmentLocation.get().equals("Phoenix")) {
            zoneIDVariable = ZoneId.of("America/Phoenix");     // Set zoneId
        } else{
        //if(this.appointmentLocation.get().equals("London")) {
            zoneIDVariable = ZoneId.of("Europe/London");     // Set zoneId
        }
        zoneDateTime = appointmentStartTimestamp.toLocalDateTime().atZone(zoneIDVariable);
        localDateNow = zoneDateTime.toLocalDate();
        return localDateNow;
    }

    //***METHOD TO GET THE APPOINTMENT DURATION***   
    public String appointmentDurationGrab() {
        //Variable declaration        
        LocalTime localTimeNow = null;
        ZoneId zoneIDVariable = ZoneId.systemDefault();        
        ZonedDateTime zoneDateTime;
        String duration = "";
        Timestamp appointmentStartTimestamp = Timestamp.valueOf(this.appointmentEnd.get()); // Getting appointmentStart, setting timestamp
            zoneDateTime = appointmentStartTimestamp.toLocalDateTime().atZone(zoneIDVariable);
            localTimeNow = zoneDateTime.toLocalTime();
        int hoursby12 = Integer.parseInt(localTimeNow.toString().split(":")[0]);    // Parse the localtime
        int minutes = Integer.parseInt(localTimeNow.toString().split(":")[1]);
        if (minutes == 15) {
            duration = "15 Minutes";
        }
        if (minutes == 30) {
            duration = "30 Minutes";
        }
        if (minutes == 45) {
            duration = "45 Minutes";
        }
        if (minutes == 00) {
            duration = "1 Hour";
        }
        return duration;
    }
    
    //***METHOD TO GET THE APPOINTMENT TIME***   
    public String appointmentTimeGrab() {
        //Variable declaration        
        LocalTime localTimeNow = null;
        ZoneId zoneIDVariable = ZoneId.systemDefault();        
        ZonedDateTime zoneDateTime;
        Timestamp appointmentStartTimestamp = Timestamp.valueOf(this.appointmentStart.get()); // Getting appointmentStart, setting timestamp
        
        if(this.appointmentLocation.get().equals("New York")) {     // Check to see location of appointment to set zone
            zoneIDVariable = ZoneId.of("America/New_York");
            zoneDateTime = appointmentStartTimestamp.toLocalDateTime().atZone(zoneIDVariable);
            localTimeNow = zoneDateTime.toLocalTime().minusHours(4);    // Figure offset
        }
        if(this.appointmentLocation.get().equals("Phoenix")) {
            zoneIDVariable = ZoneId.of("America/Phoenix");
            zoneDateTime = appointmentStartTimestamp.toLocalDateTime().atZone(zoneIDVariable);
            localTimeNow = zoneDateTime.toLocalTime().minusHours(7);    // Figure offset
        }
        if(this.appointmentLocation.get().equals("London")) {
            zoneIDVariable = ZoneId.of("Europe/London");
            zoneDateTime = appointmentStartTimestamp.toLocalDateTime().atZone(zoneIDVariable);
            localTimeNow = zoneDateTime.toLocalTime().plusHours(1);    // Figure offset
        }
        String dayOrNight = "";       
        int hoursby12 = Integer.parseInt(localTimeNow.toString().split(":")[0]);    // Parse the localtime
        if(hoursby12 > 12) {
            hoursby12 -= 12;
        }
        if(hoursby12 < 9 || hoursby12 == 12) {    // Is it morning or night
            dayOrNight = "PM";
        } else {
            dayOrNight = "AM";
        }
        String fixedTime = hoursby12 + ":00 " + dayOrNight;  // Concat strings for fixed time 
        return fixedTime;
    }
  
} // End Appointment Class
