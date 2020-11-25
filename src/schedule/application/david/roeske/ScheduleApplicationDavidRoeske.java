//***IMPORT PACKAGES***
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.SQLDatabase;

/**
 * Scheduler Class
 *
 * @author David Roeske
 */
public class ScheduleApplicationDavidRoeske extends Application {
    
    //***METHOD CALL AT PROGRAM LAUNCH***
    @Override public void start(Stage stage) throws Exception {

        Parent sceneToShow = FXMLLoader.load(getClass().getResource("views/Login.fxml"));  // Load Login Screen
        Scene scene = new Scene(sceneToShow);
        stage.setScene(scene);
        stage.show();   // Show stage(scene)
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SQLDatabase.connect();  // Call to connect to database
        launch(args);
        SQLDatabase.disconnect();   // Call to disconnect from database
    }
    
} // End Scheduler Class
