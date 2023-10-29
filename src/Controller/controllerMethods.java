/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.IOException;
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
public class controllerMethods {
    
    final String loginPane = "/View/Login.fxml";
    final String adminPane = "/View/AdminPane.fxml";
    final String recordsOfficerPane = "/View/RecordsOfficer.fxml";
    final String employeeMgmtPane = "/View/EmployeeMgmt.fxml";
    final String adminAttendancePane = "/View/Admin_attendance.fxml";
    final String adminDashboardPane = "/View/Admin_dashboard.fxml";
    
    public void openAttenPane(String fxmlPath){
         try {
            // Close the current window
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxmlPath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void exitAndOpenNewPane(Button btnName, String fxmlPath){
         try {
            // Close the current window
            Stage stage = (Stage) btnName.getScene().getWindow();
            stage.close();

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
    
    public void openWindow(String fxmlPath){
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
    
}
