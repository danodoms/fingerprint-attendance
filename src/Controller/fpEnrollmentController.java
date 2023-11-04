/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.digitalpersona.uareu.*;
import Fingerprint.*;
import static Fingerprint.Prompt.promptLabel;
import javafx.beans.binding.*;
import javafx.beans.value.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author admin
 */
public class fpEnrollmentController implements Initializable { 

    @FXML
    private Label readerStatusLabel;
    @FXML
    private ImageView fingerprintImage;
    
    @FXML
    private Label enrollFingerprintLabel;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        promptLabel = enrollFingerprintLabel;
        
        setReaderStatusLabel();
        
        EnrollmentThread enrollment = new EnrollmentThread(fingerprintImage);
        enrollment.start();
 
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
    
}
