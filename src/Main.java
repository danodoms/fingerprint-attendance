/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import Controller.LoginController;
import Fingerprint.Selection;
import static Fingerprint.Selection.waitAndGetReader;
import com.digitalpersona.uareu.UareUException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author admin
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Automatically Select Reader
        Selection.reader = Selection.getReader();
        
        
       // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View//Login.fxml"));
        Parent root = loader.load();
        
        
        // Create a scene with the loaded FXML content
        Scene scene = new Scene(root);
        
        
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
 primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.out.println("Stage is closing");
              try {
                  Selection.reader.Close();
              } catch (UareUException ex) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      });        
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
