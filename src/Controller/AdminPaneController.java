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
    private Button attendanceViewer;
    /**
     * Initializes the controller class.
     */
    @FXML
    private BorderPane borderPaneOb;
    
    controllerMethods method = new controllerMethods();
    @FXML
    private Button logOutAdminBtn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void openAttendancePane(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource(method.adminAttendancePane));
        borderPaneOb.setCenter(view);
    }
    @FXML
    private void openEmpPane(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource(method.employeeMgmtPane));
        borderPaneOb.setCenter(view);
    }

    @FXML
    private void logOut(ActionEvent event) {
        method.exitAndOpenNewPane(logOutAdminBtn, method.loginPane);
    }
    
}
