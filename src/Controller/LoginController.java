package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author admin
 */

public class LoginController implements Initializable {

    @FXML
    private Button loginAdminBtn;
    @FXML
    private Button loginRecordsOfficerBtn;
    

    /**
     * Initializes the controller class.
     */
    
    controllerMethods method = new controllerMethods();
    @FXML
    private Button fpEnrollmentShortcutBtn;
    @FXML
    private Button fpIdentificationShortcutBtn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openAdminPane(ActionEvent event) {
        method.exitAndOpenNewPane(loginAdminBtn, method.ADMIN_PANE);
        System.out.println("Logged in as admin");
    }

    @FXML
    private void openRecordsOfficerPane(ActionEvent event) {
        method.exitAndOpenNewPane(loginAdminBtn, method.RECORDS_OFFICER_PANE);
        System.out.println("Logged in as records officer");
    }

    @FXML
    private void openFpEnrollmentPane(ActionEvent event) {
        method.openPane(method.FP_ENROLLMENT_PANE);
    }

    @FXML
    private void openFpIdentificationPane(ActionEvent event) {
        method.openPane(method.FP_IDENTIFICATION_PANE);
    }
}
