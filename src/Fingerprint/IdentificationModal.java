/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Controller.FpIdentificationSuccessController;
import Utilities.PaneUtil;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author admin
 */
public class IdentificationModal {
    PaneUtil paneUtil = new PaneUtil();
    
      public void displayIdentificationSuccess(int delayInMilliseconds, String userName) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_IDENTIFICATION_SUCCESS));
            Parent root = loader.load();
            
            FpIdentificationSuccessController controller = loader.getController();  // Access the controller
            controller.setUserData(userName);// Set user data

            // Create a new stage for the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(delayInMilliseconds), event -> stage.close())
            );
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
