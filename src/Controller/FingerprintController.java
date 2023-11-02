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
public class FingerprintController implements Initializable {
    
    private ReaderCollection m_collection;
    private Reader           m_reader;
    private ObservableObjectValue<Reader> ObservableReader;

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
        m_reader = Selection.getReader();
        
        setReaderStatusLabel();
//        CaptureThread capT = new CaptureThread(m_reader, fingerprintImage);
//        capT.start();
        EnrollmentThread enrollment = new EnrollmentThread(m_reader, fingerprintImage);
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
