package models;

//***IMPORT PACKAGES***
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Customer Class
 *
 * @author David Roeske
 */
public final class Customer {
    //***VARIABLE DECLARATION***    
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty customerAddress = new SimpleStringProperty();
    private final SimpleStringProperty customerCity = new SimpleStringProperty();
    private final SimpleStringProperty customerPhonenumber = new SimpleStringProperty();   
    private final SimpleStringProperty customerPostalCode = new SimpleStringProperty();

    //***CONSTRUCTORS***     
    public Customer() {}    // Empty constructor
     
    public Customer(int id, String customername, String customeraddress, String customercity, String customerphonenumber, String customerpostalcode) {
        setCustomerId(id);      
        setCustomerName(customername);
        setCustomerAddress(customeraddress);
        setCustomerCity(customercity);
        setCustomerPhonenumber(customerphonenumber);
        setCustomerPostalCode(customerpostalcode);
    }
    
    //***GETTERS***     
    public int getCustomerId() {
        return customerId.get();
    }
    
    public String getCustomerName() {
        return customerName.get();
    }
    
    public String getCustomerAddress() {
        return customerAddress.get();
    }
    
    public String getCustomerCity() {
        return customerCity.get();
    }
    
    public String getCustomerPhonenumber() {
        return customerPhonenumber.get();
    }
    
    public String getCustomerPostalCode() {
        return customerPostalCode.get();
    }
    
    //***SETTERS***     
    public void setCustomerId(int customerid) {
        this.customerId.set(customerid);
    }
    
    public void setCustomerName(String customername) {
        this.customerName.set(customername);
    }
    
    public void setCustomerAddress(String customeraddress) {
        this.customerAddress.set(customeraddress);
    }
    
    public void setCustomerCity(String customercity) {
        this.customerCity.set(customercity);
    }
    
    public void setCustomerPhonenumber(String customerphonenumber) {
        this.customerPhonenumber.set(customerphonenumber);
    }
    
    public void setCustomerPostalCode(String customerpostalcode) {
        this.customerPostalCode.set(customerpostalcode);
    }
    
} // End Customer Class
