package views;

//***IMPORT PACKAGES***
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.*;

/**
 * MainController Class
 *
 * @author David Roeske
 */
public class MainController implements Initializable {
    //***VARIABLE DECLARATION***
    ObservableList<String> logList = FXCollections.observableArrayList(); //For system tab        
    private Customer selectedCustomer; //Customer Variable
    private int selectedTab;    // Month or week
    private Appointment selectedAppointment; //Appoint Variable
 
    //***MAIN SCREEN ELEMENTS***
    //MAIN ANCHOR
    @FXML private AnchorPane MainScreen;  
    //SYSTEM ANCHOR
    @FXML private Label systemMessage;
    @FXML private Label systemMessageBox;    
    //CUSTOMER ANCHOR
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerId;
    @FXML private TableColumn<Customer, String> customerName;  
    @FXML private TableColumn<Customer, String> customerCity; 
    //BUTTONS
    @FXML private Button addCustomerButton;    
    @FXML private Button modifyCustomerButton;
    @FXML private Button infoCustomerButton; 
    @FXML private Button deleteCustomerButton; 
    //TASK ANCHOR
    @FXML private Button addAppointmentButton;    
    @FXML private Button modifyAppointmentButton;
    @FXML private Button deleteAppointmentButton; 
    //WEEKLY TAB
    @FXML private Tab weeklyTab;
    @FXML private Label weekCustomerLabel;   
    @FXML private TableView<Appointment> weekTable;  
    @FXML private TableColumn<Appointment, String> weekTitle;     
    @FXML private TableColumn<Appointment, String> weekDescription;   
    @FXML private TableColumn<Appointment, String> weekContact;    
    @FXML private TableColumn<Appointment, String> weekLocation;
    @FXML private TableColumn<Appointment, String> weekStart;  
    @FXML private TableColumn<Appointment, String> weekEnd;     
    //MONTHLY TAB
    @FXML private Tab monthlyTab;
    @FXML private Label monthCustomerLabel;    
    @FXML private TableView<Appointment> monthTable; 
    @FXML private TableColumn<Appointment, String> monthTitle;    
    @FXML private TableColumn<Appointment, String> monthDescription;    
    @FXML private TableColumn<Appointment, String> monthContact;   
    @FXML private TableColumn<Appointment, String> monthLocation;   
    @FXML private TableColumn<Appointment, String> monthStart;    
    @FXML private TableColumn<Appointment, String> monthEnd; 
    //REPORTS TAB
    @FXML private Button viewReport1Button; 
    @FXML private Button viewReport2Button; 
    @FXML private Button viewReport3Button; 
    //SYSTEM TAB
    @FXML private ListView<String> myListView;
    
    //***BUTTON FOR METHOD TO ADD CUSTOMER TO DATABASE***
    @FXML public void addCustomerAction() {
        //Dialog Creation
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(MainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerAdd.fxml")); // Load window to input data
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerAddController controller = fxmlLoader.getController();
        dialog.showAndWait().ifPresent((response -> {
            if(response == saveButton) {  // Save customer to database
                if(controller.addCustomerAction()) {
                    systemMessageOperation(0,"Successfully added customer to database");
                    customerTable.setItems(CustomerUtilization.getAllCustomers());
                } else {
                    systemMessageOperation(1, "Customer failed to be added to database");
                    //Alert Window
                    Alert CustomerFailedAlert = new Alert(Alert.AlertType.ERROR);
                    CustomerFailedAlert.setTitle("Error");
                    CustomerFailedAlert.setHeaderText("Customer Error");
                    CustomerFailedAlert.setContentText(controller.showError());
                    CustomerFailedAlert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            
                            addCustomerAction();
                        }
                    }));
                }
            }
        }));
    }
    
    //***BUTTON FOR METHOD TO MODIFY PREEXISTING CUSTOMER TO DATABASE***
    @FXML public void modifyCustomerAction() {
        customerSelectionReturn(); // Return what customer table cell is selected
        //Dialog Creation
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(MainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CustomerModify.fxml")); // Load window to input data
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CustomerModifyController controller = fxmlLoader.getController();
        controller.fillCustomerFields(selectedCustomer);
        dialog.showAndWait().ifPresent((response -> {
            if(response == saveButton) {  // Save customer to database
                if(controller.modifyCustomerAction()) {
                    systemMessageOperation(0,"Successfully modified customer in database");
                    customerTable.setItems(CustomerUtilization.getAllCustomers());
                } else {
                    systemMessageOperation(1,"Failed to modify customer in database");
                    //Alert Window
                    Alert CustomerFailedAlert = new Alert(Alert.AlertType.ERROR);
                    CustomerFailedAlert.setTitle("Error");
                    CustomerFailedAlert.setHeaderText("Customer Modification Error");
                    CustomerFailedAlert.setContentText(controller.showError());
                    CustomerFailedAlert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            modifyCustomerAction();
                        }
                    }));
                }
            }
        }));
    }   
    
    //***BUTTON FOR METHOD TO PULL INFORMATION FOR CUSTOMER SELECTED***
    @FXML public void infoCustomerAction() {
        customerSelectionReturn(); // Return what customer table cell is selected
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerInfo.fxml")); // Load window to view data
            Parent sceneToShow = (Parent) fxmlLoader.load();
            CustomerInfoController controller = fxmlLoader.getController();
            controller.fillCustomerFields(selectedCustomer);           
            Stage stage = new Stage();
            stage.setTitle("Customer Details");
            stage.setScene(new Scene(sceneToShow));
            stage.show();
            systemMessageOperation(0,"Successfully loaded customer information");
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }        

    }    
    
    //***BUTTON FOR METHOD TO DELETE CUSTOMER FROM DATABASE***
    @FXML public void deleteCustomerAction() {
        customerSelectionReturn(); // Return what customer table cell is selected
        //Alert Window
        Alert customerDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        customerDeleteAlert.setTitle("Delete");
        customerDeleteAlert.setHeaderText("Delete");
        customerDeleteAlert.setContentText("Delete customer from database?");
        customerDeleteAlert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                CustomerUtilization.deleteCustomer(selectedCustomer.getCustomerId());    // Delete customer from database
                customerTable.setItems(CustomerUtilization.getAllCustomers());   // Reload customer table for view
                systemMessageOperation(0,"Successfully deleted customer from database");
            }
        }));
    }  

    //***BUTTON FOR METHOD TO ADD APPOINTMENT TO DATABASE*** 
    @FXML public void addAppointmentAction() {
        customerSelectionReturn(); // Return what customer table cell is selected
        //Dialog Creation
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(MainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentAdd.fxml")); // Load window to input data
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentAddController controller = fxmlLoader.getController();
        controller.getNameofCustomer(selectedCustomer.getCustomerName());
        dialog.showAndWait().ifPresent((response -> {
            if(response == saveButton) {  // Save appointment to database
                if(controller.addAppointmentAction(selectedCustomer.getCustomerId())) {
                    systemMessageOperation(0,"Successfully added appointment to database");
                    monthTable.setItems(AppointmentUtilization.retrieveMonthlyAppointments(selectedCustomer.getCustomerId()));
                    weekTable.setItems(AppointmentUtilization.retrieveWeeklyAppointments(selectedCustomer.getCustomerId()));
                } else {
                    systemMessageOperation(1,"Failed to add appointment to database");
                    //Alert Window
                    Alert appointmentFailedAlert = new Alert(Alert.AlertType.ERROR);
                    appointmentFailedAlert.setTitle("Error");
                    appointmentFailedAlert.setHeaderText("Appointment Error");
                    appointmentFailedAlert.setContentText(controller.showError());
                    appointmentFailedAlert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            addAppointmentAction();
                        }
                    }));
                }
            }
        }));
    }
      
    //***BUTTON FOR METHOD TO MODIFY PREEXISTING APPOINTMENT IN DATABASE***   
    @FXML public void modifyAppointmentAction() {
        tabSelectionReturn();   // Is it week or month
        //Dialog Creation
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(MainScreen.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AppointmentModify.fxml")); // Load window to input data
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButton);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        AppointmentModifyController controller = fxmlLoader.getController();
        controller.fillAppointmentFields(selectedCustomer.getCustomerName(), selectedAppointment);
        dialog.showAndWait().ifPresent((response -> {
            if(response == saveButton) {  // Save appointment to database
                if(controller.modifyAppointmentAction(selectedAppointment.getAppointmentId())) {
                    systemMessageOperation(0,"Successfully modified appointment in database");
                    monthTable.setItems(AppointmentUtilization.retrieveMonthlyAppointments(selectedCustomer.getCustomerId()));
                    weekTable.setItems(AppointmentUtilization.retrieveWeeklyAppointments(selectedCustomer.getCustomerId()));
                } else {
                    systemMessageOperation(1,"Failed to modify appointment in database");
                    //Alert Window
                    Alert appointmentFailedAlert = new Alert(Alert.AlertType.ERROR);
                    appointmentFailedAlert.setTitle("Error");
                    appointmentFailedAlert.setHeaderText("Appointment Modification Error");
                    appointmentFailedAlert.setContentText(controller.showError());
                    appointmentFailedAlert.showAndWait().ifPresent((response2 -> {
                        if(response2 == ButtonType.OK) {
                            modifyAppointmentAction();
                        }
                    }));
                }
            }
        }));
    }    
    
    //***BUTTON FOR METHOD TO DELETE SELECTED APPOINTMENT***
    @FXML public void deleteAppointmentAction() {
        tabSelectionReturn();   // Is it week or month
        //Alert Window
        Alert appointmentDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        appointmentDeleteAlert.setTitle("Delete");
        appointmentDeleteAlert.setHeaderText("Delete");
        appointmentDeleteAlert.setContentText("Continue to delete appointment?");
        appointmentDeleteAlert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                systemMessageOperation(0,"Successfully deleted appointment from database");
                AppointmentUtilization.deleteAppointment(selectedAppointment.getAppointmentId());
                if(selectedTab == 1) {  // Month is selected
                   monthTable.setItems(AppointmentUtilization.retrieveMonthlyAppointments(selectedCustomer.getCustomerId())); 
                }
                if(selectedTab == 2) {  // Week is selected
                    weekTable.setItems(AppointmentUtilization.retrieveWeeklyAppointments(selectedCustomer.getCustomerId()));
                }
            }
        }));
    }    
      
    //***EVENT FOR METHOD TO LOAD DATA BASED ON TABLE CELL SELECTION***
    @FXML public void whatCellIsSelected(MouseEvent event) {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem(); // What is selected
        int selectedCell = selectedCustomer.getCustomerId();    // Store in variable pass to appointment tables
        monthCustomerLabel.setText(selectedCustomer.getCustomerName());
        weekCustomerLabel.setText(selectedCustomer.getCustomerName());
        monthTable.setItems(AppointmentUtilization.retrieveMonthlyAppointments(selectedCell));
        weekTable.setItems(AppointmentUtilization.retrieveWeeklyAppointments(selectedCell));
    }
    
   //***BUTTON FOR METHOD TO OPEN NEW WINDOW TO VIEW FIRST REPORT*** 
   @FXML public void viewReport1Action() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportDisplay.fxml"));
            Parent sceneShow = (Parent) fxmlLoader.load();
            ReportDisplayController controller = fxmlLoader.getController();
            controller.populateFields(1);   // Load first report call
            Stage stage = new Stage();
            stage.setTitle("Report");
            stage.setScene(new Scene(sceneShow));
            stage.show();        
            systemMessageOperation(0,"Successfully loaded requested report");
        } catch (IOException e) {
            systemMessageOperation(2,"Failed to load report");
            System.out.println("IO Exception: " + e.getMessage());
        }       
   }  
   
   //***BUTTON FOR METHOD TO OPEN NEW WINDOW TO VIEW SECOND REPORT***  
   @FXML public void viewReport2Action() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportDisplay.fxml"));
            Parent sceneShow = (Parent) fxmlLoader.load();
            ReportDisplayController controller = fxmlLoader.getController();
            controller.populateFields(2);   // Load second report call
            Stage stage = new Stage();
            stage.setTitle("Report");
            stage.setScene(new Scene(sceneShow));
            stage.show();
            systemMessageOperation(0,"Successfully loaded requested report");            
        } catch (IOException e) {
            systemMessageOperation(2,"Failed to load report");            
            System.out.println("IO Exception: " + e.getMessage());
        }    
   } 
   
   //***BUTTON FOR METHOD TO OPEN NEW WINDOW TO VIEW THIRD REPORT***    
   @FXML public void viewReport3Action() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportDisplay.fxml"));
            Parent sceneShow = (Parent) fxmlLoader.load();
            ReportDisplayController controller = fxmlLoader.getController();
            controller.populateFields(3);   // Load third report call
            Stage stage = new Stage();
            stage.setTitle("Report");
            stage.setScene(new Scene(sceneShow));
            stage.show();      
            systemMessageOperation(0,"Successfully loaded requested report");            
        } catch (IOException e) {
            systemMessageOperation(2,"Failed to load report");            
            System.out.println("IO Exception: " + e.getMessage());
        }        
   }     
    
   //***BUTTON FOR METHOD TO OPEN TEXT VIEWER FOR LOG FILE***
   @FXML public void logOpenAction() {
        File file = new File("databaselog.txt");
        if(file.exists()) {     // Check to see if file exists
            if(Desktop.isDesktopSupported()) {  // Can we open
                try {
                    systemMessageOperation(0,"Successfully opened system log file"); 
                    Desktop.getDesktop().open(file);    // Open file
                } catch (IOException e) {
                    systemMessageOperation(2,"Failed to load system log file"); 
                    System.out.println("IO Exception: " + e.getMessage());
                }
            }
        }
    }
    
   //***BUTTON FOR METHOD TO EXIT PROGRAM***
   @FXML void exitAction(ActionEvent event) {
        //Alert window
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.initModality(Modality.NONE);
        exitAlert.setTitle("Exiting");
        exitAlert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> ask = exitAlert.showAndWait();
        if (ask.get() == ButtonType.OK)
            System.exit(0); //Exit program
    } 
    
   //***METHOD TO SET CUSTOMER THAT IS SELECTED***
   public void customerSelectionReturn() {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            System.out.println("1");
        } else {
            return;
        }        
    }  
      
   //***METHOD TO SET TAB FOR APPOINTMENTS THAT IS SELECTED***
   public void tabSelectionReturn() {
             if(monthlyTab.isSelected()) {
            if(monthTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = monthTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }
        if(weeklyTab.isSelected()) {
            if(weekTable.getSelectionModel().getSelectedItem() != null) {
                selectedAppointment = weekTable.getSelectionModel().getSelectedItem();
            } else {
                return;
            }
        }    
    }   
    
   //***METHOD FOR FADE CREATION***
   private FadeTransition createFade(Node node, int messageType, String message) {
        FadeTransition fade = new FadeTransition(Duration.seconds(6), node);
        fade.setFromValue(2);
        fade.setToValue(0);
        if (messageType == 0) { // Successfull operation
            systemMessage.setText(message);      
            systemMessageBox.setBackground(new Background(new BackgroundFill(Color.web("#cfe4f7"), new CornerRadii(10), Insets.EMPTY)));
        } else if (messageType == 1) {  // Failed operation
            systemMessage.setText(message);    
            systemMessageBox.setBackground(new Background(new BackgroundFill(Color.web("#e2ff55"), new CornerRadii(10), Insets.EMPTY)));
        }
        return fade;
    } 
    
   //***METHOD FOR SYSTEM ON SCREEN OUTPUT - SYSTEM MESSAGE BOX***
   public void systemMessageOperation(int value, String message) {
        FadeTransition fader = createFade(systemMessage, value, message);
        SequentialTransition fadeMessage1 = new SequentialTransition(
                systemMessage,
                fader
        ); 
        FadeTransition fader2 = createFade(systemMessageBox, value, message);
        SequentialTransition fadeMessage2 = new SequentialTransition(
                systemMessageBox,
                fader2
        );      
        fadeMessage1.play();
        fadeMessage2.play();        
    }    

   //***METHOD TO DISPLAY ALERT IF APPOINTMENT IS WITHIN 15 MINUTES - CALLED IN INITIALIZE***
   public void appointmentAlert(){
         Appointment appointmentObj = AppointmentUtilization.checkAppointment();
        if(appointmentObj != null) {
            Customer customer = CustomerUtilization.getSingleCustomer(appointmentObj.getAppointmentCustomerId());
            String text = String.format("You have an " + appointmentObj.getAppointmentDescription() +   // Message body for appointment15Alert window
                    " appointment with " + customer.getCustomerName() + 
                    " at " + appointmentObj.appointment15MinuteAlert());
            //Alert window
            Alert appointment15Alert = new Alert(Alert.AlertType.WARNING);
            appointment15Alert.setTitle("Appointment Reminder: " + appointmentObj.getAppointmentTitle());
            appointment15Alert.setHeaderText("You Have An Appointment Within 15 Minutes");
            appointment15Alert.setContentText(text);
            appointment15Alert.showAndWait();                 
        }
    }
   
   //***INITIALIZE***
   @Override public void initialize(URL url, ResourceBundle rb) {
        systemMessageOperation(0,"Welcome to Schedule Program"); // Welcome message to system area
        appointmentAlert(); // Method call to see if there is an appointment in 15 minutes
        //Populate customer table
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        customerTable.setItems(CustomerUtilization.getAllCustomers());   
        //Populate weekly table - condensed lambda statements
        weekTitle.setCellValueFactory(cellData -> {         return cellData.getValue().getAppointmentTitleProperty();});        
        weekDescription.setCellValueFactory(cellData -> {   return cellData.getValue().getAppointmentDescriptionProperty();});
        weekContact.setCellValueFactory(cellData -> {       return cellData.getValue().getAppointmentContactProperty();});
        weekLocation.setCellValueFactory(cellData -> {      return cellData.getValue().getAppointmentLocationProperty();});
        weekStart.setCellValueFactory(cellData -> {         return cellData.getValue().getAppointmentStartProperty();});
        weekEnd.setCellValueFactory(cellData -> {           return cellData.getValue().getAppointmentEndProperty();});        
        //Populate monthly table - condensed lambda statements
        monthTitle.setCellValueFactory(cellData -> {        return cellData.getValue().getAppointmentTitleProperty();});       
        monthDescription.setCellValueFactory(cellData -> {  return cellData.getValue().getAppointmentDescriptionProperty();});
        monthContact.setCellValueFactory(cellData -> {      return cellData.getValue().getAppointmentContactProperty();});
        monthLocation.setCellValueFactory(cellData -> {     return cellData.getValue().getAppointmentLocationProperty();});
        monthStart.setCellValueFactory(cellData -> {        return cellData.getValue().getAppointmentStartProperty();});
        monthEnd.setCellValueFactory(cellData -> {          return cellData.getValue().getAppointmentEndProperty();});
        try {   
            File databaseLog = new File("databaseLog.txt");    //Load log entry to system tab
            Scanner inputFile = new Scanner(databaseLog);
            while (inputFile.hasNext()) {
                logList.add(inputFile.nextLine());
            }
            myListView.setItems(logList);   // Populate list with log entries from file
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }   
    }
   
} // End MainController Class
