/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Utilities.PaneUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class RO_PaneCTRL implements Initializable {

    @FXML
    private Button fileEmpRecordBtn;
    @FXML
    private Button logOutRecordsOfficerBtn;
    
    PaneUtil method = new PaneUtil();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void logOut(ActionEvent event) {
        method.exitAndOpenNewPane(fileEmpRecordBtn, method.LOGIN_PANE);
    }
    
}
