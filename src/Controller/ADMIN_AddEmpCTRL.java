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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;



/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_AddEmpCTRL implements Initializable {

    @FXML
    private TextField passwordField, FnameField, LnameField, MnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contactNumField;
    @FXML
    private ChoiceBox<String>userSuffixChoiceBox;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private Button addEmployeeBtn, selectImageBtn;
    @FXML
    private ImageView userImage;
    
    
    
    byte[] imageBytes;
    @FXML
    private ChoiceBox<String> privilegeChoiceBox;
    @FXML
    private ChoiceBox<String> sexChoiceBox;
    @FXML
    private TextField addressField;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //USER SUFFIX INITIALIZATION
         userSuffixChoiceBox.setValue("None");
         userSuffixChoiceBox.getItems().addAll("None","Jr.", "II", "III", "IV");
        
         //USER TYPE CHOICE BOX INITIALIZATION
         privilegeChoiceBox.setValue("Select");
         privilegeChoiceBox.getItems().addAll("Employee", "Admin", "Records Officer");
         
         //SEX CHOICE BOX INITIALIZATION
         sexChoiceBox.setValue("Select");
         sexChoiceBox.getItems().addAll("Male", "Female");
    }    

    @FXML
    private void addEmployee(ActionEvent event) {
        String fName = FnameField.getText();
        String mName = MnameField.getText();
        String lName = LnameField.getText();
        String suffix = userSuffixChoiceBox.getValue();
        if(suffix.equals("None")){
            suffix = null;
        }
        
        String email = emailField.getText();
        String password = passwordField.getText();
        String privilege = privilegeChoiceBox.getValue();
        String contactNum = contactNumField.getText();
        String sex = sexChoiceBox.getValue();
        if(sex.equals("Select")){
            sex = null;
        }
        LocalDate birthDate = dateOfBirthPicker.getValue();
        String address = addressField.getText();
        byte[] image = imageBytes;
        
        //filterAddEmployee();
//        if()
        
        
        try {
            User.addUser(fName, mName, lName, suffix, email, password, privilege, contactNum, sex, birthDate, address, image);
        } catch (SQLException ex) {
            System.out.println("Database error, check inputs");
            showModal("Database error, check inputs");
            
            ex.printStackTrace();
        }
        
        showModal("Employee added successfully!");
        clearFields();
    }
    
    
    private void showModal(String message) {
        Stage successStage = new Stage();
        successStage.initModality(Modality.APPLICATION_MODAL);
        successStage.setTitle("Success");

        Label successLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> successStage.close());

        VBox modalContent = new VBox(successLabel, closeButton);
        modalContent.setSpacing(10);

        // Center the content
        modalContent.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(modalContent, 250, 150);
        successStage.setScene(successScene);
        successStage.show();
    }
    
    private void clearFields(){
        userImage.setImage(null);
        emailField.clear();
        passwordField.clear();
        privilegeChoiceBox.setValue("Select");
        FnameField.clear();
        MnameField.clear();
        LnameField.clear();
        userSuffixChoiceBox.setValue("None");
        sexChoiceBox.setValue("Select");
        dateOfBirthPicker.setValue(null);
        contactNumField.clear();
        addressField.clear();
    }
    

//    private void updatePositionChoiceBox(ActionEvent event) {
//        System.out.println("update position choice box");
//        Department selectedDepartment = departmentChoiceBox.getValue();
//        positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartment.getId()));
//    }
    

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
//    private void showShiftDetails(ActionEvent event) {
//        
//        //Stores the selected shift
//        Shift selectedShift = shiftTypeChoiceBox.getValue();
//        
//        //Stores the id of the selected shift
//        int id = selectedShift.getId();
//        
//        String startTime = selectedShift.getStartTime();
//        String endTime = selectedShift.getEndTime();
//        
//        startTimeField.setText(startTime);
//        endTimeField.setText(endTime);
//        
//        
//        //DEBUGGER
//        System.out.println("SelectedShiftID: " + id);
//        System.out.println("Selected Shift: " + selectedShift);
//        System.out.println("SelectedShiftStart: " + selectedShift.getStartTime());
//        System.out.println("SelectedShiftEnd: " + selectedShift.getEndTime());
//        
//    }

    private void openFingerprintPane(ActionEvent event) {
        method.openPane(method.FP_ENROLLMENT_PANE);
    }
    
    private byte[] readImageFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }
}

    