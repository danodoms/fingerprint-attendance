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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.util.Duration;
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
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;

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
         dt(); // Initialize the date label
        times(); // Start updating the time label
    }    
    
    
    public void dt() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, yyyy-MM-dd");
        String dd = sdf.format(d);
        dateLabel.setText(dd);
    }

    private void times() {
        SimpleDateFormat st = new SimpleDateFormat("hh:mm:ss a");

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Date dt = new Date();
                    String tt = st.format(dt);
                    timeLabel.setText(tt);
                }
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
