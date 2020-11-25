package models;

//***IMPORT PACKAGES***
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.SQLDatabase;

/**
 * CustomerUtilization Class (Modifies both customer and address tables)
 *
 * @author David Roeske
 */
public class CustomerUtilization {
    //***VARIABLE DECLARATION***
    private static ObservableList<Customer> entireCustomerTable = FXCollections.observableArrayList(); // For listener tracking

    //***METHOD TO ADD NEW CUSTOMER AND ADDRESS TO CUSTOMER/ADDRESS TABLES IN DATABASE***
    public static boolean addCustomer(String customername, String address, int cityid, String phonenumber, String postalcode) {
        try {
            java.util.Date date = new Date();   // New date set
            String formatedDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date); // Format datetime for entry
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryAddress = "INSERT INTO address SET address='" +address+ // Add address entries on address table to string
                              "',address2='',cityId='" +cityid+ 
                              "',postalCode='" +postalcode+ 
                              "',phone='" +phonenumber+ 
                              "',createDate='" +formatedDateTime+ 
                              "',createdBy='',lastUpdate='" +formatedDateTime+ 
                              "',lastUpdateBy=''";
            int addressChange = SQLStatement.executeUpdate(queryAddress);   // Execute the update
            if(addressChange == 1) {    // It executed the update
                int addressId = entireCustomerTable.size() + 1; // Move up by one in index
                String queryCustomer = "INSERT INTO customer SET addressID='" +addressId+ // Add customer entries on customer table to string
                                  "',customerName='" +customername+ 
                                  "',active='" +1+ 
                                  "',createDate='" +formatedDateTime+ 
                                  "',createdBy='',lastUpdate='" +formatedDateTime+ 
                                  "',lastUpdateBy=''";                
                int customerChange = SQLStatement.executeUpdate(queryCustomer);   // Execute the update
                if(customerChange == 1) {    // It executed the update
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }        
       
    //***METHOD TO MODIFY EXISTING CUSTOMER AND ADDRESS IN DATABASE***
    public static boolean modifyCustomer(int customerandaddressid, String customername, String address, int cityid, String phonenumber, String postalcode) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryAddress = "UPDATE address SET address='" + address +    // Update address entries on address table to string
                              "', cityId=" + cityid + 
                              ", postalCode='" + postalcode + 
                              "', phone='" + phonenumber + 
                              "' WHERE addressId=" + customerandaddressid;
            int addressChange = SQLStatement.executeUpdate(queryAddress);   // Execute the update
            if(addressChange == 1) {    // It executed the update
                String queryCustomer = "UPDATE customer SET customerName='" + customername +    // Update customer entries on customer table to string
                                  "', addressId=" + customerandaddressid + 
                                  " WHERE customerId=" + customerandaddressid;
                int customerChange = SQLStatement.executeUpdate(queryCustomer);   // Execute the update
                if(customerChange == 1) {    // It executed the update
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
    
    //***METHOD TO DELETE CUSTOMER AND ADDRESS FROM DATABASE***
    public static boolean deleteCustomer(int customerandaddressid) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryAddress = "DELETE FROM address WHERE addressId=" + customerandaddressid;  // Delete PRIMART KEY ( is entire entry) to string
            int addressChange = SQLStatement.executeUpdate(queryAddress);   // Execute the update
            if(addressChange == 1) {    // It executed the update
                String queryCustomer = "DELETE FROM customer WHERE customerId=" + customerandaddressid;  // Delete PRIMART KEY (is entire entry) to string
                int customerChange = SQLStatement.executeUpdate(queryCustomer);   // Execute the update
                if(customerChange == 1) {   // It executed the update
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }

    //***METHOD TO GET CUSTOMER INFORMATION FROM QUERY USING ID***
    public static Customer getSingleCustomer(int customerid) {
        try {
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryCustomer = "SELECT * FROM customer WHERE customerId='" + customerid + 
                           "'";
            ResultSet queryResult = SQLStatement.executeQuery(queryCustomer);   // Our returned query
            if(queryResult.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(queryResult.getString("customerName"));
                SQLStatement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }
    
    //***METHOD TO GET ENTIRE CUSTOMER TABLE FROM DATABASE***
    public static ObservableList<Customer> getAllCustomers() {
        try {
            entireCustomerTable.clear();   // Clear previous list view
            Statement SQLStatement = SQLDatabase.getConnection().createStatement();    // Create statement/connection for SQL database
            String queryCustomer = "SELECT customer.customerId, customer.customerName, address.address, "       // Query to select from customer and address tables to string
                                 + "address.phone, address.postalCode, city.city"
                                 + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                                 + "INNER JOIN city ON address.cityId = city.cityId";
            ResultSet queryResult = SQLStatement.executeQuery(queryCustomer);   // Our returned query
            while(queryResult.next()) {
                Customer customer = new Customer(
                                    queryResult.getInt("customerId"), 
                                    queryResult.getString("customerName"), 
                                    queryResult.getString("address"),
                                    queryResult.getString("city"),
                                    queryResult.getString("phone"),
                                    queryResult.getString("postalCode"));
                entireCustomerTable.add(customer); // Add customer
            }
            SQLStatement.close();
            return entireCustomerTable;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            return null;
        }
    }    
    
} // End CustomerUtilization Class