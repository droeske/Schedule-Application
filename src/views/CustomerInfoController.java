package views;

//***IMPORT PACKAGES***
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import models.Customer;

/**
 * CustomerInfoController Class
 *
 * @author David Roeske
 */
public class CustomerInfoController implements Initializable {
    //***VARIABLE DECLARATION***
    private int customerid;
    private final String cityFrom = "";
    private final String countrySet = "";
    private ObservableList<String> listOfCities = FXCollections.observableArrayList("Phoenix","New York","London");   
    
    //***CUSTOMER INFO SCREEN ELEMENTS***
    //LABELS
    @FXML private Label idLabel;    
    @FXML private Label nameLabel;
    @FXML private Label cityShownLabel;
    @FXML private Label addressLabel;  
    @FXML private Label countryLabel;
    @FXML private Label phonenumberLabel;    
    @FXML private Label postalcodeLabel;
    //COMBOBOX
    @FXML private ComboBox customercity; 
    
    //***METHOD TO SET COUNTRY TO WHICH CITY IS SELECTED***
    @FXML public void setCountry() {
        String currentCity = customercity.getSelectionModel().getSelectedItem().toString(); // Set city selection to string
        cityShownLabel.setText(currentCity); 
        if(currentCity.equals("Phoenix")) {
            countryLabel.setText("United States");    
        }
        if(currentCity.equals("New York")) {
            countryLabel.setText("United States");  
        }
        if (currentCity.equals("London")) {
            countryLabel.setText("England");       
        }
    }
    
    //***METHOD TO POPULATE ALL CUSTOMER FIELDS***    
    public void fillCustomerFields(Customer customerObj) {
        customerid = customerObj.getCustomerId();
        idLabel.setText(String.valueOf(customerid));
        nameLabel.setText(customerObj.getCustomerName());
        addressLabel.setText(customerObj.getCustomerAddress());
        customercity.getSelectionModel().select(customerObj.getCustomerCity());     
        phonenumberLabel.setText(customerObj.getCustomerPhonenumber());
        postalcodeLabel.setText(customerObj.getCustomerPostalCode());
        setCountry();   // Method to set country based on city selection        
    }   
    
    //***METHOD TO GET TEXT FROM FIELDS***
    public void infoCustomerAction() {
        String customerName = nameLabel.getText();
        String customerAddress = addressLabel.getText();
        int customerCity = customercity.getSelectionModel().getSelectedIndex() + 1;
        String customerPhonenumber = phonenumberLabel.getText();
        String customerPostalcode = postalcodeLabel.getText();    
    }

    //***INITIALIZE***    
    @Override public void initialize(URL url, ResourceBundle rb) {
        customercity.setItems(listOfCities);
        //System.out.println("Initializing customerInfo");
    }    
    
} // End CustomerInfoController Class
