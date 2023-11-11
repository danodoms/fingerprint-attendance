package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Fingerprint.IdentificationThread;
import com.digitalpersona.uareu.UareUException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    @FXML
    private Label dateTimeLabel;
    @FXML
    private ImageView fpImageview;
    @FXML
    private Pane loginPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Pane fpEnrollmentPane;
    @FXML
    private ImageView userImage;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label fpCountLabel;
    @FXML
    private Button enrollFpBtn;
    @FXML
    private Label lastEnrollDateLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm:ss");
                    dateTimeLabel.setText(LocalDateTime.now().format(formatter));
                });
            }
        };
        timer.start();
        
        IdentificationThread identification = new IdentificationThread(fpImageview);
        identification.start();
        
        

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
