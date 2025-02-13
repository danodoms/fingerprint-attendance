/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Fingerprint.IdentificationThread;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FP_IdentificationCTRL implements Initializable {
    @FXML
    private ImageView fpIdentificationImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IdentificationThread identification = new IdentificationThread(fpIdentificationImage);
        identification.start();
        // TODO
    }    
    
}
