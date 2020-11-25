package views;

//***IMPORT PACKAGES***
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import static java.util.Locale.FRANCE;
import static java.util.Locale.FRENCH;
import static java.util.Locale.US;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.UserUtilization;

/**
 * LoginController Class
 *
 * @author David Roeske
 */
public class LoginController implements Initializable {
    //***VARIABLE DECLARATION***
    private String header;  // Error window header
    private String title;   // Error window title
    private String body;    // Error window main text field
    
    //***LOGIN SCREEN ELEMENTS***    
    //FIELDS
    @FXML private TextField userTextField;
    @FXML private PasswordField passTextField;  
    //LABELS
    @FXML private Label mainMessage;      
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    //BUTTONS
    @FXML private Button loginButton;

    //***BUTTON TO HANDLE LOGIN PROCESSING***
    @FXML public void handleLogin(ActionEvent event) throws IOException {
        String username = userTextField.getText();
        String password = passTextField.getText();
        boolean validUser = UserUtilization.systemLogon(username, password);
        if(validUser) { // Successfull logon
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            Parent sceneToShow = FXMLLoader.load(getClass().getResource("Main.fxml")); // Load main program screen
            Scene scene = new Scene(sceneToShow);
            stage.setScene(scene);
            stage.show();   // Show stage (scene)
        } else {    // Failure to logon
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(body);
            alert.showAndWait();
        }
    }
    
    //***INITIALIZE***
    @Override public void initialize(URL url, ResourceBundle rb) {
        
        // Set locale
        Locale locale = Locale.getDefault();        // Check region (english)  
       //Locale locale = Locale.FRENCH;             // Check region (french)
       // Locale locale = new Locale("es", "ES");   // Check region (spanish)
       //Locale locale = Locale.JAPANESE;           // Check default unsupported language
        String returnLanguage = locale.getDisplayLanguage(Locale.ENGLISH);  // Return display language to check as or in conditional statements
        if (locale.equals(US) || returnLanguage == "English") {    // If english
            mainMessage.setText("Please enter your username and password:");   
            usernameLabel.setText("Username:");       
            passwordLabel.setText("Password:");
            loginButton.setText("Login");
            header = "Login Error";
            title = "Error";
            body = "Invalid username or password";
        } else if (locale.equals(FRENCH) || locale.equals(FRANCE) || returnLanguage == "French") {    // If french
            mainMessage.setText("Veuillez s'il vous plaît entrer votre nom d'utilisateur et votre mot de passe:");   
            usernameLabel.setText("Nom d'utilisateur:");       
            passwordLabel.setText("Mot de passe:");
            loginButton.setText("S'identifier");
            header = "Erreur d'identification";
            title = "Erreur";
            body = "Nom d'utilisateur ou mot de passe invalide";
        } else if (locale.equals("ES") || returnLanguage == "Spanish") {  // If spanish
            mainMessage.setText("Porfavor introduzca su nombre de usuario y contraseña:");   
            usernameLabel.setText("Nombre de usuario:");       
            passwordLabel.setText("Contraseña:");
            loginButton.setText("Iniciar sesión");
            header = "Error de inicio de sesión";
            title = "Error";
            body = "Usuario o contraseña invalido";
        } else {
            mainMessage.setText("Please enter your username and password:");   
            usernameLabel.setText("Username:");       
            passwordLabel.setText("Password:");
            loginButton.setText("Login");
            header = "Login Error";
            title = "Error";
            body = "Invalid username or password";
        }
    }    
    
} // End LoginController Class
