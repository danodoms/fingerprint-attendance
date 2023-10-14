package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class AdminPaneController implements Initializable {

    @FXML
    private Button addEmployeeBtn;

    /**
     * Initializes the controller class.
     */
    
    controllerMethods method = new controllerMethods();
    @FXML
    private Button logOutAdminBtn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    

        
    @FXML
    private void openEmployeeMgmtPane(ActionEvent event) {
        method.openWindow(method.employeeMgmtPane);
    }

    @FXML
    private void logOut(ActionEvent event) {
        method.exitAndOpenNewPane(logOutAdminBtn, method.loginPane);
    }
    
}
