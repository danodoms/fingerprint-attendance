/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import Fingerprint.Selection;
import Fingerprint.ThreadFlags;
import com.digitalpersona.uareu.UareUException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utilities.PaneUtil;

/**
 *
 * @author admin
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
//        Application.setUserAgentStylesheet("/Style/admin_pane.css");
        //Automatically Select Reader
        //Selection.reader = Selection.getReader();
        Selection.waitAndGetReader();


        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("View/LoginPane.fxml"));
        Parent root = loader.load();

        // Create a scene with the loaded FXML content
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Style/admin_pane.css").toExternalForm());

        // Set the application icon
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/Images/program_icon.png")));

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();



        //add listener to close the reader when the stage is closed
//        primaryStage.setOnHidden(event -> {
//            ThreadFlags.programIsRunning = false;
//            System.out.println("Stage is closing");
//            //print the flag
//            System.out.println("Program is running: " + ThreadFlags.programIsRunning);
//
//        });







//
//         primaryStage.setOnHidden(new EventHandler<WindowEvent>() {
//          public void handle(WindowEvent we) {
//              System.out.println("Stage is closing");
//              try {
//                  ThreadFlags.programIsRunning = false;
//
//                  Selection.closeReader();
//              } catch (UareUException ex) {
//                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//              }
//          }
//      });

        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
