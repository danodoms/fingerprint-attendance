/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Fingerprint.EnrollmentThread;
import Fingerprint.Selection;
import Model.User;
import Utilities.ImageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static Fingerprint.Prompt.promptLabel;
/**
 * FXML Controller class
 *
 * @author admin
 */
public class FP_EnrollmentCTRL implements Initializable { 

    @FXML
    private Label readerStatusLabel;
    @FXML
    private ImageView fingerprintImage;
    @FXML
    private Label enrollFingerprintLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView userImageView;
    
    private int userIdToEnroll;

    EnrollmentThread enrollmentThread;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        promptLabel = enrollFingerprintLabel;
        
        setReaderStatusLabel();
        
        //make the enrollment thread stop when the window is closed, use lambda expression



        //stop enrollment thread when window is closed


        
        

    }
    
    private void setReaderStatusLabel(){
        String newText = readerStatusLabel.getText();
        if(Selection.readerIsConnected()){
            newText += " Connected";
            
        }else{
            newText += " Disconnected";
        }
            
        readerStatusLabel.setText(newText);
    }
    
    public void setDataForEnrollment(User user, Stage stage, ADMIN_FingerprintsCTRL adminFingerprintsCTRL){
        userIdToEnroll = user.getId();


        nameLabel.setText(user.getFullNameWithInitial());
        userImageView.setImage(ImageUtil.byteArrayToImage(user.getImage()));
        
        
        enrollmentThread = new EnrollmentThread(fingerprintImage, userIdToEnroll);
        enrollmentThread.start();

        //gets the stage from the class that will use this controller and adds function to it
        stage.setOnHiding(event -> {
            enrollmentThread.stopEnrollmentThread();
                    adminFingerprintsCTRL.loadUserTable();
                    adminFingerprintsCTRL.loadUserDetails(user);
        });

    }
    
}
