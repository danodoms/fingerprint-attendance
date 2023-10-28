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
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FingerprintController implements Initializable {
    
    private ReaderCollection m_collection;
    private Reader           m_reader;


    @FXML
    private Label readerStatusLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setHardwareStatusLabel();
//System.out.println(hardwareStatusLabel.getText());
    }   
    
    private void setHardwareStatusLabel(){
        String newText = readerStatusLabel.getText();
        if(Selection.readerConnected()){
            newText += " Connected";
            
        }else{
            newText += " Disconnected";
        }
            
        readerStatusLabel.setText(newText);
    }
}
