package views;

//***IMPORT PACKAGES***
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.CustomerUtilization;

/**
 * CustomerAddController Class
 *
 * @author David Roeske
 */
public class CustomerAddController implements Initializable {
    //***VARIABLE DECLARATION***    
    private ObservableList<String> listOfCities = FXCollections.observableArrayList("Phoenix","New York","London"); 
    
    //***CUSTOMER ADD SCREEN ELEMENTS***
    //TEXTFIELDS
    @FXML private TextField customername;
    @FXML private TextField customeraddress;  
    @FXML private TextField customercountry;
    @FXML private TextField customerphonenumber;   
    @FXML private TextField customerpostalcode;
    //COMBOBOX
    @FXML private ComboBox customercity; 
    
    //***METHOD TO SET COUNTRY TO WHICH CITY IS SELECTED*** 
    @FXML public void setCountry() {
        String currentCity = customercity.getSelectionModel().getSelectedItem().toString(); // Set city selection to string
        if(currentCity.equals("Phoenix")) {
            customercountry.setText("United States");    
        }
        if(currentCity.equals("New York")) {
            customercountry.setText("United States");  
        }
        if (currentCity.equals("London")) {
            customercountry.setText("England");       
        }
    }
    
    //***METHOD TO VALIDATE EACH FIELD HAS INFORMATION ENTERED***
    public boolean isValidEntries(String name, String address, int city, String Phonenumber, String Postalcode) {
        if(name.isEmpty() || address.isEmpty() || city == 0 || Phonenumber.isEmpty() || Postalcode.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    //***METHOD TO DISPLAY ERROR - RETURNED TO MAIN CONTROLLER***
    public String showError(){
        return "Data not entered in one or more fields!";
    }
    
    //***METHOD TO ADD CUSTOMER TO DATABASE***
    public boolean addCustomerAction() {
        String customerName = customername.getText();
        String customerAddress = customeraddress.getText();
        int customerCity = customercity.getSelectionModel().getSelectedIndex() + 1;
        String customerPhonenumber = customerphonenumber.getText();
        String customerPostalcode = customerpostalcode.getText();
        if(!isValidEntries(customerName, customerAddress, customerCity, customerPhonenumber, customerPostalcode)){
            return false;   // Failed to provide content in each field
        } else {
            return CustomerUtilization.addCustomer(customerName, customerAddress, customerCity, customerPhonenumber, customerPostalcode); // Return the added customer
        }
    }  
    
    //***INITIALIZE***    
    @Override public void initialize(URL url, ResourceBundle rb) {
        customercity.setItems(listOfCities);
        //System.out.println("Initializing customerAdd");
    }    
    
} // End CustomerAddController Class
