/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 *
 * @author admin
 */
public class PaneUtil {
    public final String LOGIN_PANE = "/app/src/main/java/org/attendance/View/LoginPane.fxml";
    public final String CONTAINER_PANE = "/app/src/main/java/org/attendance/View/ContainerPane.fxml";
    public final String RO_PANE = "/app/src/main/java/org/attendance/View/RO_Pane.fxml";
    public final String RO_DASHBOARD = "/app/src/main/java/org/attendance/View/RO_Dashboard.fxml";
    public final String ADMIN_EMP_MGMT = "/app/src/main/java/org/attendance/View/ADMIN_EmpMgmt.fxml";
    public final String ADMIN_ADD_EMP = "/app/src/main/java/org/attendance/View/ADMIN_AddEmp.fxml";
    public final String ADMIN_ASSIGNMENTS = "/app/src/main/java/org/attendance/View/ADMIN_Assignments.fxml";
    public final String ADMIN_DEPARTMENTS = "/app/src/main/java/org/attendance/View/ADMIN_Departments.fxml";
    public final String ADMIN_POSITIONS = "/app/src/main/java/org/attendance/View/ADMIN_Positions.fxml";
    public final String ADMIN_SHIFTS= "/app/src/main/java/org/attendance/View/ADMIN_Shifts.fxml";
    public final String ADMIN_ATT_REPORTS = "/app/src/main/java/org/attendance/View/ADMIN_AttReports.fxml";
    public final String ADMIN_ATTENDANCE = "/app/src/main/java/org/attendance/View/ADMIN_Attendance.fxml";
    public final String ADMIN_DASHBOARD = "/app/src/main/java/org/attendance/View/ADMIN_Dashboard.fxml";
    public final String ADMIN_FINGERPRINTS = "/app/src/main/java/org/attendance/View/ADMIN_Fingerprints.fxml";
    public final String ADMIN_EMP_CALENDAR_PANE = "/app/src/main/java/org/attendance/View/ADMIN_UserCalendar.fxml";
    public final String ADMIN_EMP_TIMEOFF_PANE = "/app/src/main/java/org/attendance/View/ADMIN_Timeoff.fxml";
    
    //FINGERPRINT
    public final String FP_ENROLLMENT = "/app/src/main/java/org/attendance/View/FP_Enrollment.fxml";
    public final String FP_IDENTIFICATION = "/app/src/main/java/org/attendance/View/FP_Identification.fxml";
    public final String FP_IDENTIFICATION_SUCCESS = "/app/src/main/java/org/attendance/View/FP_IdentificationSuccess.fxml";
    public final String FP_IDENTIFICATION_FAIL = "/app/src/main/java/org/attendance/View/FP_IdentificationFail.fxml";
    public final String FP_CONFIRM_ACTION = "/app/src/main/java/org/attendance/View/FP_ConfirmAction.fxml";
    
    
    
    public void openAttenPane(String fxmlPath){
         try {
            // Close the current window
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxmlPath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void openPane(String fxmlPath){
         try {
            // Load the second FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new stage for the second window
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.show();

             // Set the application icon
             secondStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/app/src/main/java/org/attendance/Images/program_icon.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openModal(String fxmlPath){
         try {
            // Load the second FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new stage for the second window
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);
            secondStage.show();

             // Set the application icon
             secondStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/app/src/main/java/org/attendance/Images/program_icon.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void exitPane(Button btnName){
            // Close the current window
            Stage stage = (Stage) btnName.getScene().getWindow();
            stage.close();
    }
    
        
    
    public void exitAndOpenNewPane(Button btnName, String fxmlPath){
            exitPane(btnName);
            openPane(fxmlPath);
    }
    
    
    
    public void openAndClosePane(String fxmlPath, int delayInMilliseconds) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new stage for the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the window after the specified delay
            //Thread.sleep(delayInMilliseconds);
            //stage.close();
            
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(delayInMilliseconds), event -> stage.close())
            );
            timeline.play();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
