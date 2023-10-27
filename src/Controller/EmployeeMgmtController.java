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
import javafx.scene.control.ComboBox;
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
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
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
    private ChoiceBox<Shift> shiftTypeChoiceBox;
    @FXML
    private Button selectImageBtn;
    @FXML
    private ImageView userImage;
    @FXML
    private Button enrollFingerprintBtn;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private ChoiceBox<String> userTypeChoiceBox;
    @FXML
    private TextField userIDfield;
    @FXML
    private ChoiceBox<String> userSuffixChoiceBox;
    

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //USER ID TEXT FIELD INITIALIZATION
         userIDfield.setText(dbMethods.getNextUserId()+"");
         
         //USER SUFFIX INITIALIZATION
         userSuffixChoiceBox.setValue("Select Suffix");
         userSuffixChoiceBox.getItems().addAll("None","Jr.", "II", "III", "IV");
        
         //USER TYPE CHOICE BOX INITIALIZATION
         userTypeChoiceBox.setValue("Select User Type");
         userTypeChoiceBox.getItems().addAll("Employee", "Admin", "Records Officer");
         
         
         //DEPARTMENT CHOICE BOX INITIALIZATION
         departmentChoiceBox.setItems(dbMethods.getDepartments());
         departmentChoiceBox.setOnAction(this::updatePositionChoiceBox);
         
         //SEX CHOICE BOX INITIALIZATION
         sexChoiceBox.setValue("Select Sex");
         sexChoiceBox.getItems().addAll("Male", "Female");
         
         //SHIFT TYPE CHOICE BOX INITIALIZATION
         shiftTypeChoiceBox.setItems(dbMethods.getShifts());
         shiftTypeChoiceBox.setOnAction(this::showShiftDetails);
    }    

    @FXML
    private void addEmployee(ActionEvent event) {
    }

    private void updatePositionChoiceBox(ActionEvent event) {
        System.out.println("update position choice box");
        Department selectedDepartment = departmentChoiceBox.getValue();
        positionChoiceBox.setItems(dbMethods.getPositionsByDepartmentId(selectedDepartment.getId()));
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
    
    //Display start time and end time on startTime and endTime fields on employee mgmt pane
    private void showShiftDetails(ActionEvent event) {
        
        //Stores the selected shift
        Shift selectedShift = shiftTypeChoiceBox.getValue();
        
        //Stores the id of the selected shift
        int id = selectedShift.getId();
        
        String startTime = selectedShift.getStartTime();
        String endTime = selectedShift.getEndTime();
        
        startTimeField.setText(startTime);
        endTimeField.setText(endTime);
        
        
        //DEBUGGER
        System.out.println("SelectedShiftID: " + id);
        System.out.println("Selected Shift: " + selectedShift);
        System.out.println("SelectedShiftStart: " + selectedShift.getStartTime());
        System.out.println("SelectedShiftEnd: " + selectedShift.getEndTime());
        
    }

    
    
    
}
