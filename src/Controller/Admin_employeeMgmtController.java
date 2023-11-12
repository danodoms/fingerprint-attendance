package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
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
import Fingerprint.*;
import com.digitalpersona.uareu.Reader;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;



/**
 * FXML Controller class
 *
 * @author admin
 */
public class Admin_employeeMgmtController implements Initializable {

    @FXML
    private TextField userIDfield, passwordField, FnameField, LnameField, MnameField;
    @FXML
    private TextField emailField, contactNumField, addressField, startTimeField, endTimeField;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
    @FXML
    private ChoiceBox<Shift> shiftTypeChoiceBox;
    @FXML
    private ChoiceBox<String> sexChoiceBox, userTypeChoiceBox, userSuffixChoiceBox;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private Button addEmployeeBtn, selectImageBtn, enrollFingerprintBtn;
    @FXML
    private ImageView userImage;
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
//    @FXML
//    private ChoiceBox<Shift> shiftTypeChoiceBox;
//    @FXML
//    private Button selectImageBtn;
//    @FXML
//    private ImageView userImage;
//    @FXML
//    private Button enrollFingerprintBtn;
//    @FXML
//    private TextField passwordField;
//    @FXML
//    private TextField startTimeField;
//    @FXML
//    private TextField endTimeField;
//    @FXML
//    private ChoiceBox<String> userTypeChoiceBox;
//    @FXML
//    private TextField userIDfield;
//    @FXML
//    private ChoiceBox<String> userSuffixChoiceBox;
    
    byte[] imageBytes;
    
    
    

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //USER ID TEXT FIELD INITIALIZATION
         userIDfield.setText(User.getNextUserId()+"");
         
         //USER SUFFIX INITIALIZATION
         userSuffixChoiceBox.setValue("Select Suffix");
         userSuffixChoiceBox.getItems().addAll("None","Jr.", "II", "III", "IV");
        
         //USER TYPE CHOICE BOX INITIALIZATION
         userTypeChoiceBox.setValue("Select User Type");
         userTypeChoiceBox.getItems().addAll("Employee", "Admin", "Records Officer");
         
         
         //DEPARTMENT CHOICE BOX INITIALIZATION
         departmentChoiceBox.setItems(Department.getDepartments());
         departmentChoiceBox.setOnAction(this::updatePositionChoiceBox);
         
         //SEX CHOICE BOX INITIALIZATION
         sexChoiceBox.setValue("Select Sex");
         sexChoiceBox.getItems().addAll("Male", "Female");
         
         //SHIFT TYPE CHOICE BOX INITIALIZATION
         shiftTypeChoiceBox.setItems(Shift.getShifts());
         shiftTypeChoiceBox.setOnAction(this::showShiftDetails);
    }    

    @FXML
    private void addEmployee(ActionEvent event) {
        String fName = FnameField.getText();
        String mName = MnameField.getText();
        String lName = LnameField.getText();
        String suffix = userSuffixChoiceBox.getValue();
        String email = emailField.getText();
        String password = passwordField.getText();
        String privilege = userTypeChoiceBox.getValue();
        String contactNum = contactNumField.getText();
        String sex = sexChoiceBox.getValue();
        LocalDate birthDate = dateOfBirthPicker.getValue();
        String address = addressField.getText();
        byte[] image = imageBytes;
        
        User.addUser(fName, mName, lName, suffix, email, password, privilege, 0, sex, birthDate, address, image);
    }

    private void updatePositionChoiceBox(ActionEvent event) {
        System.out.println("update position choice box");
        Department selectedDepartment = departmentChoiceBox.getValue();
        positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartment.getId()));
    }
    

    @FXML
    private void selectImg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                // Read the selected image file into a byte array
                imageBytes = readImageFile(selectedFile);

                if (imageBytes != null) {
                    // Store the imageBytes in the database or perform other actions
                    System.out.println("Image selected and read as bytes.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Display the selected image in an ImageView (optional)
            String filePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
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

    @FXML
    private void openFingerprintPane(ActionEvent event) {
        method.openPane(method.FP_ENROLLMENT_PANE);
//        
//        Reader m_reader = Selection.getReader();
//        if(m_reader != null){
//            Enrollment.Run(m_reader);
//        }
    }
    private byte[] readImageFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }
}

    