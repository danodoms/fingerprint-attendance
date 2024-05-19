/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Controller.FP_IdentificationSuccessCTRL;
import Model.User;
import Utilities.PaneUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 *
 * @author admin
 */
public class IdentificationModal {
    PaneUtil paneUtil = new PaneUtil();
    
//      public void displayIdentificationSuccess(int delayInMilliseconds, String userName, byte[] imageData) {
//        try {
//            // Load the FXML file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_IDENTIFICATION_SUCCESS));
//            Parent root = loader.load();
//
//            FP_IdentificationSuccessCTRL controller = loader.getController();  // Access the controller
//            controller.setUserData(userName, imageData);// Set user data
//
//            // Create a new stage for the window
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//
//            Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(delayInMilliseconds), event -> stage.close())
//            );
//            timeline.play();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void displayIdentificationSuccess(int delayTimeInMs, User user) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_IDENTIFICATION_SUCCESS));
            Parent root = loader.load();

            FP_IdentificationSuccessCTRL controller = loader.getController();  // Access the controller
            controller.setUserData(delayTimeInMs, user);// Set user data

            // Create a new stage for the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(delayTimeInMs), event -> stage.close())
            );
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayConfirmAction(int delayTimeInMs) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_CONFIRM_ACTION));
            Parent root = loader.load();


            // Create a new stage for the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(delayTimeInMs), event -> stage.close())
            );
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
      
      
    public void displayIdentificationFail(int delayInMilliseconds) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_IDENTIFICATION_FAIL));
            Parent root = loader.load();

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
