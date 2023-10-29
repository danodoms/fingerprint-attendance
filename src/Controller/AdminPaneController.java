package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class AdminPaneController implements Initializable {

    @FXML
    private Button addEmployeeBtn;
    @FXML
    private Button dashBoardBtn;
    
    @FXML
    private Button attendanceViewer;
    /**
     * Initializes the controller class.
     */
    @FXML
    private BorderPane borderPaneOb;
    @FXML
    private BorderPane dashboardBP;
    
    @FXML
    private AnchorPane view;
    controllerMethods method = new controllerMethods();
    
    @FXML
    private Button logOutAdminBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            view = FXMLLoader.load(getClass().getResource(method.adminDashboardPane));
        } catch (IOException ex) {
            Logger.getLogger(AdminPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        borderPaneOb.setCenter(view);
    } 
    @FXML
<<<<<<< HEAD
    private void openEmployeeMgmtPane(ActionEvent event) {
        method.openPane(method.EMPLOYEE_MGMT_PANE);
=======
    private void openDashboardPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(method.adminDashboardPane));
        borderPaneOb.setCenter(view);
    }
    @FXML
    private void openAttendancePane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(method.adminAttendancePane));
        borderPaneOb.setCenter(view);
    }
    @FXML
    private void openEmpPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(method.employeeMgmtPane));
        borderPaneOb.setCenter(view);
>>>>>>> henry
    }

    @FXML
    private void logOut(ActionEvent event) {
        method.exitAndOpenNewPane(logOutAdminBtn, method.LOGIN_PANE);
    }
    
}
