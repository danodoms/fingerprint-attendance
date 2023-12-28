package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Fingerprint.IdentificationThread;
import Fingerprint.Selection;
import Fingerprint.ThreadFlags;
import Model.User;
import Session.Session;
import Utilities.Encryption;
import Utilities.PaneUtil;
import com.dlsc.gemsfx.DialogPane;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FXML Controller class
 *
 * @author admin
 */

public class LoginPaneCTRL implements Initializable {

    @FXML
    private Button loginAdminBtn;
    @FXML
    private Button loginRecordsOfficerBtn;

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private Button fpEnrollmentShortcutBtn;
    @FXML
    private Button fpIdentificationShortcutBtn;
    @FXML
    private ImageView fpImageview;
    @FXML
    private Pane loginPane;
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
    @FXML
    private Label titleLabel;
    @FXML
    private Pane fpIdentificationPane;
    @FXML
    private ImageView fpIdentificationUserImage;
    @FXML
    private Label fpIdentificationUserName;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox showPassCheckBox;
    @FXML
    private Label loginPrompt;
    
    PaneUtil paneUtil = new PaneUtil();
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label notationLabel;
    @FXML
    private Label scannerStatusLabel;
    @FXML
    private Label scannerStatusSubtextLabel;

    IdentificationThread identification;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        loginPrompt.setVisible(false);

        identification = new IdentificationThread(fpImageview);
        //set an image for fpImageview
        String fpAnimationImagePath = "/Images/fp_scan_animation.gif";
        String fpImagePath = "/Images/fp_placeholder.jpg";

        Image fpImage = new Image(fpImagePath);
        fpImageview.setImage(fpImage);

        // TODO
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    //I want the date to be displayed in this format: Monday, January 8
                    DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd");
                    dateLabel.setText(LocalDateTime.now().format(dateformatter));

                    //I want the time to be displayed in this format: 12:00:00
                    DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                    timeLabel.setText(LocalDateTime.now().format(timeformatter));

                    //I want the notation to be displayed in this format: am
                    DateTimeFormatter notationformatter = DateTimeFormatter.ofPattern("a");
                    notationLabel.setText(LocalDateTime.now().format(notationformatter));


                });
            }
        };
        timer.start();



        
        showPassCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passwordField.setPromptText(passwordField.getText());
                passwordField.setText("");
            } else {
                passwordField.setText(passwordField.getPromptText());
                passwordField.setPromptText("");
            }
        });


        identification.start();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        // Execute a task using the executor
        executor.execute(() -> {
            System.out.println("///////////////////// Runnning LoginPaneCTRL executor thread ///////////////////");

            while(ThreadFlags.programIsRunning) {
                //System.out.println("Identification Thread is running: " + identification.isAlive());

                    if (Selection.readerIsConnected_noLogging()) {
                        Platform.runLater(() -> {
                            scannerStatusLabel.setText("Reader Connected");
                            scannerStatusSubtextLabel.setText("READY FOR CAPTURE");
                            //System.out.println("Reader Connected");
                        });
                    } else {
                        Platform.runLater(() -> {
                            scannerStatusLabel.setText("Reader Disconnected");
                            scannerStatusSubtextLabel.setText("WAITING FOR READER");
                            //System.out.println("Reader Disconnected");
                        });
                    }

                    //sleep thread for 5 seconds
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


        // Shutdown the executor to release resources
        executor.shutdown();
        System.out.println("///////////////////// LoginPaneCTRL executor thread stopped ///////////////////");
//        if(ThreadFlags.programIsRunning == false){
//            identification.stopThread();
//        }


    }


    @FXML
    private void authenticate(ActionEvent event) {
//        System.out.println("authenticating");
//        String enteredEmail = emailField.getText();
//        String enteredPassword = passwordField.getText();
//
//        User user = User.getUserByEmail(enteredEmail);
//
//        if(user == null){
//            System.out.println("Email does not exist");
//            loginPrompt.setVisible(true);
//            loginPrompt.setText("Email does not exist");
//        }else{
//
//            String email = user.getEmail();
//            String password = user.getPassword();
//
//            if(enteredEmail.equals("") && enteredPassword.equals("")){
//             loginPrompt.setText("Fields can't be empty");
//             loginPrompt.setVisible(true);
//            }else if(enteredEmail.equals("")){
//                loginPrompt.setText("Email can't be empty");
//                loginPrompt.setVisible(true);
//            }else if(enteredPassword.equals("")){
//                loginPrompt.setText("Password can't be empty");
//                loginPrompt.setVisible(true);
//            }else if(Encryption.verifyPassword(enteredPassword, password)){
//                System.out.println("password matched");
//                proceedUserLogin(user);
//            }else{
//                System.out.println("password mismatched");
//                loginPrompt.setVisible(true);
//                loginPrompt.setText("Incorrect Password");
//            }
//        }

        System.out.println("authenticating");

        String enteredEmail = emailField.getText();
        String enteredPassword = passwordField.getText();

        //check if fields are empty
        if (enteredEmail.equals("") && enteredPassword.equals("")) {
            showLoginPrompt("Fields can't be empty");
            return;
        }

        //check if email is empty
        if (enteredEmail.equals("")) {
            showLoginPrompt("Email can't be empty");
            return;
        }

        //check if password is empty
        if (enteredPassword.equals("")) {
            showLoginPrompt("Password can't be empty");
            return;
        }

        //get user by email
        User user = User.getUserByEmail(enteredEmail);

        //check if email exists
        if (user == null) {
            showLoginPrompt("Email does not exist");
            return;
        }

        //get user's password
        String email = user.getEmail();
        String password = user.getPassword();

        User userFromDb = User.getUserByUserId(user.getId());

        //check if password matches
        if (Encryption.verifyPassword(enteredPassword, password)) {
            System.out.println("password matched");

            Session.getInstance().setLoggedInUser(userFromDb);
            proceedUserLogin(user);
        } else {
            System.out.println("password mismatched");
            showLoginPrompt("Incorrect Password");
        }
    }

    private void showLoginPrompt(String message) {
        loginPrompt.setVisible(true);
        loginPrompt.setText(message);
    }
    
    private void proceedUserLogin(User authdUser){
        User user = authdUser;
        
        String currentUser = user.getEmail();
        String privilege = user.getPrivilege();
        
        if(privilege.equalsIgnoreCase("employee")){
            System.out.println("Access denied");
        }else if(privilege.equalsIgnoreCase("admin") || privilege.equalsIgnoreCase("records officer")){
            paneUtil.exitAndOpenNewPane(loginAdminBtn, paneUtil.CONTAINER_PANE);
            System.out.println("Logged in as " + currentUser+"");
        }
    }
    
    
    
    @FXML
    private void openAdminPane(ActionEvent event) {
        User user = new User("dev@dev.com", "developer", "admin");
        Session.getInstance().setLoggedInUser(user);
        paneUtil.exitAndOpenNewPane(loginAdminBtn, paneUtil.CONTAINER_PANE);
        System.out.println("Logged in as admin");
        identification.stopThread();
    }

    
    
    @FXML
    private void openRecordsOfficerPane(ActionEvent event) {
        User user = new User("dev@dev.com", "developer", "records officer");
        Session.getInstance().setLoggedInUser(user);
        paneUtil.exitAndOpenNewPane(loginRecordsOfficerBtn, paneUtil.CONTAINER_PANE);
        System.out.println("Logged in as records officer");
        identification.stopThread();
    }

    
    
    @FXML
    private void openFpEnrollmentPane(ActionEvent event) {
        paneUtil.openPane(paneUtil.FP_ENROLLMENT);
    }

    
    
    @FXML
    private void openFpIdentificationPane(ActionEvent event) {
        paneUtil.openPane(paneUtil.FP_IDENTIFICATION);
    }

    
    
    
}
