package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Model.*;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private ChoiceBox<Departments> departmentChoiceBox;
    @FXML
    private ChoiceBox<Positions> positionChoiceBox;
    @FXML
    private ChoiceBox<String> sexChoiceBox;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField addressField;
    @FXML
    private Button addEmployeeBtn;
    
    dbMethods dbMethods = new dbMethods();
    @FXML
    private ChoiceBox<?> shiftTypeChoiceBox;
    @FXML
    private Button selectImageBtn;
    @FXML
    private ImageView userImage;
    @FXML
    private Button enrollFingerprintBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         departmentChoiceBox.setItems(dbMethods.getDepartments());
         
         sexChoiceBox.setValue("Select Sex");
         sexChoiceBox.getItems().addAll("Male", "Female");
         
         
    }    

    @FXML
    private void addEmployee(ActionEvent event) {
    }

    @FXML
    private void updatePositionChoiceBox(MouseEvent event) {
        System.out.println("update position choice box");
        String department = departmentChoiceBox.getValue()+"";
        positionChoiceBox.setItems(dbMethods.getPositionsByDepartment(department));
    }

    @FXML
    private void selectImg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Handle the selected file, e.g., display its path
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + filePath);
            
            
            Image image = new Image(selectedFile.toURI().toString());

        // Set the image in the ImageView
        userImage.setImage(image);
        }
    }
    
}
