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
import models.Customer;
import models.CustomerUtilization;

/**
 *  CustomerModifyController Class
 *
 * @author David Roeske
 */
public class CustomerModifyController implements Initializable {

    //***VARIABLE DECLARATION***
    private int customerid = 0;
    private ObservableList<String> listOfCities = FXCollections.observableArrayList("Phoenix","New York","London"); 
    
    //***CUSTOMER MODIFY SCREEN ELEMENTS***
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
    
    //***METHOD TO POPULATE ALL CUSTOMER FIELDS***
    public void fillCustomerFields(Customer customerObj) {
        customerid = customerObj.getCustomerId();
        customername.setText(customerObj.getCustomerName());
        customeraddress.setText(customerObj.getCustomerAddress());
        customercity.getSelectionModel().select(customerObj.getCustomerCity());
        customerphonenumber.setText(customerObj.getCustomerPhonenumber());
        customerpostalcode.setText(customerObj.getCustomerPostalCode()); 
        setCountry();   // Method to set country based on city selection         
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
    
    //***METHOD TO UPDATE CURRENT CUSTOMER INFORMATION IN DATABASE***
    public boolean modifyCustomerAction() {
        String customerName = customername.getText();
        String customerAddress = customeraddress.getText();
        int customerCity = customercity.getSelectionModel().getSelectedIndex() + 1;
        String customerPhonenumber = customerphonenumber.getText();
        String customerPostalcode = customerpostalcode.getText();
        if(!isValidEntries(customerName, customerAddress, customerCity, customerPhonenumber, customerPostalcode)){
            return false;   // Failed to provide content in each field
        } else {
            return CustomerUtilization.modifyCustomer(customerid, customerName, customerAddress, customerCity, customerPhonenumber, customerPostalcode); // Update new values
        }
    }    
    
    //***INITIALIZE***
    @Override public void initialize(URL url, ResourceBundle rb) {
        customercity.setItems(listOfCities);
        //System.out.println("Initializing customerModify");
    }    
    
} // End CustomerModifyController Class
