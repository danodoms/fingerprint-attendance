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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class PaneUtil {
    public final String LOGIN_PANE = "/View/LoginPane.fxml";
    public final String ADMIN_PANE = "/View/ADMIN_Pane.fxml";
    public final String RECORDS_OFFICER_PANE = "/View/RO_Pane.fxml";
    public final String EMPLOYEE_MGMT_PANE = "/View/ADMIN_EmpMgmt.fxml";
    public final String ADD_EMPLOYEE_PANE = "/View/ADMIN_AddEmp.fxml";
    public final String ADMIN_ATT_REP_PANE = "/View/ADMIN_AttReports.fxml";
    public final String ADMIN_ATTENDANCE_PANE = "/View/ADMIN_Attendance.fxml";
    public final String ADMIN_DASHBOARD_PANE = "/View/ADMIN_Dashboard.fxml";
    
    //FINGERPRINT
    public final String FP_ENROLLMENT_PANE = "/View/FP_Enrollment.fxml";
    public final String FP_IDENTIFICATION_PANE = "/View/FP_Identification.fxml";
    public final String FP_IDENTIFICATION_SUCCESS = "/View/FP_IdentificationSuccess.fxml";
    public final String FP_IDENTIFICATION_FAIL = "/View/FP_IdentificationFail.fxml";
    
    
    
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
