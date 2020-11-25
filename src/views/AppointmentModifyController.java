package views;

//IMPORT PACKAGES
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Appointment;
import models.AppointmentUtilization;

/**
 * AppointmentModifyController Class
 *
 * @author David Roeske
 */
public class AppointmentModifyController implements Initializable {
    //***VARIABLE DECLARATION***     
    private final ObservableList<String> listOfContacts = FXCollections.observableArrayList("David", "Ann", "Joe", "Sue", "Martin", "Jill", "test"); //Contacts (users)
    private final ObservableList<String> listOfTimes = FXCollections.observableArrayList("9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM");   //Active times for appointments
    private final ObservableList<String> listOfTitles = FXCollections.observableArrayList("Formal","Informal"); // Appointment titles
    private final ObservableList<String> listOfDescriptions = FXCollections.observableArrayList("Introduction","Market Research","Investigation","Training","Issue Resolution",   // Appointment description
                                                                                   "Project Planning","Project Development","Project Status Update","Solution Brainstorm","General");
    private final ObservableList<String> listOfDurations = FXCollections.observableArrayList("15:00 Minutes","30:00 Minutes","45:00 Minutes","1 Hour");   //Active durations for appointments
    //***APPOINTMENT MODIFY SCREEN ELEMENTS***
    //TEXTFIELDS
    @FXML private TextField customerName;
    @FXML private TextField appointmentLocation;
    @FXML private DatePicker appointmentDate;
    //COMBOBOXES
    @FXML private ComboBox appointmentContact;    
    @FXML private ComboBox appointmentTime;
    @FXML private ComboBox appointmentTitle;
    @FXML private ComboBox appointmentDescription;
    @FXML private ComboBox appointmentDuration;
    
   //***METHOD TO FIGURE WHERE CONTACT LOCATION IS***    
    @FXML public void handleLocation() {
        String contactName = appointmentContact.getSelectionModel().getSelectedItem().toString();
        if (contactName.equals("David") || contactName.equals("Ann") || contactName.equals("Joe")) {
            appointmentLocation.setText("Phoenix");
        }
        if(contactName.equals("Sue") || contactName.equals("Martin")) {
            appointmentLocation.setText("New York");
        }
        if(contactName.equals("Jill") || contactName.equals("test")) {
            appointmentLocation.setText("London");
        }
    }
    
        
    //***METHOD TO POPULATE ALL APPOINTMENT FIELDS***    
    public void fillAppointmentFields(String customername, Appointment appointmentObj) {
        customerName.setText(customername);
        appointmentContact.setValue(appointmentObj.getAppointmentContact());
        appointmentLocation.setText(appointmentObj.getAppointmentLocation());
        appointmentTitle.setValue(appointmentObj.getAppointmentTitle());
        appointmentDescription.setValue(appointmentObj.getAppointmentDescription());        
        appointmentTime.setValue(appointmentObj.appointmentTimeGrab());
        appointmentDuration.setValue(appointmentObj.appointmentDurationGrab());
        appointmentDate.setValue(appointmentObj.appointmentDateGrab());
    }
    
    //***METHOD TO VALIDATE EACH FIELD HAS INFORMATION ENTERED***   
    public boolean isValidEntries(int contact, int title, int description, int time, LocalDate date) {
        if(contact == -1 || title == -1 || description == -1 || time == -1 || date == null) {
            return false;
        } else {
            return true;
        }
    }
    
    //***METHOD TO DISPLAY ERROR - RETURNED TO MAIN CONTROLLER***
    public String showError(){
        return "Check fields for correct information!";
    }

    //***METHOD TO UPDATE A PREEXISTING APPOINTMENT IN DATABASE***
    public boolean modifyAppointmentAction(int contactid) {
        int time = appointmentTime.getSelectionModel().getSelectedIndex();  
        int duration = appointmentDuration.getSelectionModel().getSelectedIndex();         
        int contact = appointmentContact.getSelectionModel().getSelectedIndex();
        LocalDate date = appointmentDate.getValue();        
        int title = appointmentTitle.getSelectionModel().getSelectedIndex();
        int description = appointmentDescription.getSelectionModel().getSelectedIndex();
        if(AppointmentUtilization.overlappingAppointment(contactid, appointmentLocation.getText(), date.toString(), listOfTimes.get(time))) {
            return false;
        }
        if(!isValidEntries(contact, title, description, time, date)) {
            return false;
        }
        if(AppointmentUtilization.modifyAppointment(contactid, listOfTitles.get(title), listOfDescriptions.get(description), appointmentLocation.getText(), listOfContacts.get(contact), date.toString(), listOfTimes.get(time), listOfDurations.get(duration))) {
            return true;
        } else {
            return false;
        }
    }
    
    //***INITIALIZE***    
    @Override public void initialize(URL url, ResourceBundle rb) {
        appointmentDate.setDayCellFactory(p -> new DateCell() {
            @Override public void updateItem(LocalDate thedate, boolean isblank) {
                super.updateItem(thedate, isblank);
                setDisable(thedate.isBefore(LocalDate.now()) || thedate.getDayOfWeek() == DayOfWeek.SUNDAY || thedate.getDayOfWeek() == DayOfWeek.SATURDAY || isblank);
                if(thedate.getDayOfWeek() == DayOfWeek.SUNDAY || thedate.getDayOfWeek() == DayOfWeek.SATURDAY || thedate.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #6e1515;");
                }
            }
        });
        appointmentContact.setItems(listOfContacts);
        appointmentTime.setItems(listOfTimes);
        appointmentDuration.setItems(listOfDurations);
        appointmentTitle.setItems(listOfTitles);
        appointmentDescription.setItems(listOfDescriptions);  
    }    
    
} // End AppointmentModifyController Class