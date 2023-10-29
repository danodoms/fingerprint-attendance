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
    
<<<<<<< HEAD
=======
//<<<<<<< HEAD
//    final String LOGIN_PANE = "/View/Login.fxml";
//    final String ADMIN_PANE = "/View/AdminPane.fxml";
//    final String RECORDS_OFFICER_PANE = "/View/RecordsOfficer.fxml";
//    final String EMPLOYEE_MGMT_PANE = "/View/EmployeeMgmt.fxml";
//    final String FINGERPRINT_PANE = "/View/Fingerprint.fxml";
//=======
>>>>>>> main
    final String LOGIN_PANE = "/View/Login.fxml";
    final String ADMIN_PANE = "/View/AdminPane.fxml";
    final String RECORDS_OFFICER_PANE = "/View/RecordsOfficer.fxml";
    final String EMPLOYEE_MGMT_PANE = "/View/EmployeeMgmt.fxml";
<<<<<<< HEAD
    final String FINGERPRINT_PANE = "/View/Fingerprint.fxml";
//    final String loginPane = "/View/Login.fxml";
//    final String adminPane = "/View/AdminPane.fxml";
    final String recordsOfficerPane = "/View/RecordsOfficer.fxml";
    final String employeeMgmtPane = "/View/EmployeeMgmt.fxml";
    final String adminAttendancePane = "/View/Admin_attendance.fxml";
    final String adminDashboardPane = "/View/Admin_dashboard.fxml";
=======
    final String ADMIN_ATTENDANCE_PANE = "/View/Admin_attendance.fxml";
    final String ADMIN_DASHBOARD_PANE = "/View/Admin_dashboard.fxml";
    final String FINGERPRINT_PANE = "/View/Fingerprint.fxml";
>>>>>>> main
    
    public void openAttenPane(String fxmlPath){
         try {
            // Close the current window
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxmlPath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//>>>>>>> henry
    
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
    
}
