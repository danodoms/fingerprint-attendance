/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import Fingerprint.*;
import static Fingerprint.Prompt.promptLabel;
import Model.User;
import Utilities.ImageUtil;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        promptLabel = enrollFingerprintLabel;
        
        setReaderStatusLabel();
        
       
        
        
        // Set an event handler for the window hiding event
        //Stage stage = (Stage) nameLabel.getScene().getWindow();
        //stage.setOnHiding(event -> enrollment.stopEnrollmentThread());
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
    
    public void setDataForEnrollment(User user){
        userIdToEnroll = user.getId();
        
        nameLabel.setText(user.getLname());
        userImageView.setImage(ImageUtil.byteArrayToImage(user.getImage()));
        
        
        EnrollmentThread enrollment = new EnrollmentThread(fingerprintImage, userIdToEnroll);
        enrollment.start();
        
    }
    
}
