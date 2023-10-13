package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class EmployeeMgmtController implements Initializable {

    @FXML
    private TextField FnameField;
    @FXML
    private TextField LnameField;
    @FXML
    private TextField MnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactNumField;
    @FXML
    private ChoiceBox<String> departmentChoiceBox;
    @FXML
    private ChoiceBox<String> positionChoiceBox;
    @FXML
    private ChoiceBox<String> sexChoiceBox;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField addressField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
