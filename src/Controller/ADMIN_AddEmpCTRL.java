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
import Utilities.Filter;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    boolean editMode = false;
    @FXML
    private TextField repeatPasswordField;

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
//        if(editMode == false){
            String fname = FnameField.getText();
            String mname = MnameField.getText();
            String lname = LnameField.getText();
            String suffix = userSuffixChoiceBox.getValue();
                if(suffix.equals("None")){
                    suffix = null;
                }

            String email = emailField.getText();
            String password = passwordField.getText();
            String repeatPassword = repeatPasswordField.getText();
            String privilege = privilegeChoiceBox.getValue();
                if(privilege.equals("None")){
                       privilege = null;
                   }
            String contactNum = contactNumField.getText();
            String sex = sexChoiceBox.getValue();
                if(sex.equals("Select")){
                    sex = null;
                }
            LocalDate birthDate = dateOfBirthPicker.getValue();
            String address = addressField.getText();
            byte[] image = imageBytes;

            User candidateUser = new User(image, email, password, privilege, fname, mname, lname, suffix, sex, birthDate, contactNum, address);
            String prompt = generatePrompt(candidateUser, repeatPassword);

            try {
                if(prompt.equals("")){
                    if(editMode == true){
                        User.updateUser(fname, mname, lname, suffix, email, password, privilege, contactNum, sex, birthDate, address, image);
                        showModal("Success","Saved Changes");
                    }else{
                        User.addUser(fname, mname, lname, suffix, email, password, privilege, contactNum, sex, birthDate, address, image);
                        showModal("Success","Employee added successfully!");
                    }
                    clearFields();

                    ADMIN_EmpMgmtCTRL adminEmpCtrl = new ADMIN_EmpMgmtCTRL();
                    adminEmpCtrl.loadUserTable();
                }else{
                    showModal("Failed", prompt);
                }
            } catch (SQLException ex) {
                System.out.println("Database error, check inputs");
                showModal("Failed", "Database error");
                ex.printStackTrace();
            }
//        }else{
//            
//        }
//        
        
        
    }
    
    private String generatePrompt(User user, String repeatedPassword){
        ArrayList<String> promptList = new ArrayList<>();
        String filterPrompt = "";
        
        String fnamePrompt = Filter.REQUIRED.name(user.getFname(), "First Name");
        String mnamePrompt = Filter.OPTIONAL.name(user.getMname(), "Middle Name");
        String lnamePrompt = Filter.REQUIRED.name(user.getLname(), "Last Name");
        String emailPrompt = Filter.REQUIRED.email(user.getEmail());
        String passwordPrompt = Filter.REQUIRED.password(user.getPassword(), repeatedPassword);
        String privilegePrompt = Filter.REQUIRED.privilege(user.getPrivilege());
        
        //add prompts to promptList
        promptList.add(fnamePrompt);
        promptList.add(mnamePrompt);
        promptList.add(lnamePrompt);
        promptList.add(emailPrompt);
        promptList.add(passwordPrompt);
        promptList.add(privilegePrompt);
        
        //Combines all prompts from promptlist and also adds new line for each
        for(int i=0; i<promptList.size(); i++){
            if(!(promptList.get(i).equals("")) && i == promptList.size()-1){
                filterPrompt += promptList.get(i);
            }else if(!(promptList.get(i).equals(""))){
                filterPrompt += promptList.get(i) + "\n";
            }
        }
        
        return filterPrompt;
    }
    
    
    private void showModal(String title, String message) {
        Stage successStage = new Stage();
        successStage.initModality(Modality.APPLICATION_MODAL);
        successStage.setTitle(title);

        Label successLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> successStage.close());

        VBox modalContent = new VBox(successLabel, closeButton);
        modalContent.setSpacing(10);

        // Center the content
        modalContent.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(modalContent, 350, 250);
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
        repeatPasswordField.clear();
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
    
    public void setDataForEdit(User user) {
        editMode = true;
        
        // Set the user details in the form
        String suffix = user.getSuffix();
        
        if(suffix == null || suffix.equals("")){
            suffix = "None";
        }
        
        FnameField.setText(user.getFname());
        MnameField.setText(user.getMname());
        LnameField.setText(user.getLname());
        userSuffixChoiceBox.setValue(suffix);
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        privilegeChoiceBox.setValue(user.getPrivilege());
        contactNumField.setText(user.getContactNum());
        sexChoiceBox.setValue(user.getSex());
        dateOfBirthPicker.setValue(user.getBirthDate());
        addressField.setText(user.getAddress());
        userImage.setImage(byteArrayToImage(user.getImage()));

        // Set other fields as needed
        addEmployeeBtn.setText("Save Changes");
    }
    
    private Image byteArrayToImage(byte[] byteArray) {
        // Convert byte array to JavaFX Image
        try{
        return new Image(new java.io.ByteArrayInputStream(byteArray));
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
    }
}

    