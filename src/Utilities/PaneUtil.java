/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class PaneUtil {
   
    public final String LOGIN_PANE = "/View/Login.fxml";
    public final String ADMIN_PANE = "/View/AdminPane.fxml";
    public final String RECORDS_OFFICER_PANE = "/View/RecordsOfficer.fxml";
    public final String EMPLOYEE_MGMT_PANE = "/View/EmployeeMgmt.fxml";
    public final String ADMIN_ATTENDANCE_PANE = "/View/Admin_attendance.fxml";
    public final String ADMIN_DASHBOARD_PANE = "/View/Admin_dashboard.fxml";
    public final String FP_ENROLLMENT_PANE = "/View/fpEnrollment.fxml";
    public final String FP_IDENTIFICATION_PANE = "/View/fpIdentification.fxml";
    public final String FP_IDENTIFICATION_SUCCESS = "/View/fpIdentificationSuccess.fxml";
    
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
