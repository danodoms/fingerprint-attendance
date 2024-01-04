package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Model.User;
import Utilities.DatabaseUtil;
import Utilities.Filter;
import Utilities.ImageUtil;
import Utilities.PaneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;



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
    @FXML
    private TextField repeatPasswordField;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil paneUtil = new PaneUtil();
    boolean editMode = false;
    int selectedUserId = 0;
    User selectedUser;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label repeatPasswordLabel;
    
    

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
    private void addEmployee(ActionEvent event) throws IOException {
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

            User candidateUser = new User(image, email, password, privilege, fname, mname, lname, suffix, sex, birthDate, contactNum, address); //Creates candidate user that will be checked by the prompt generator
            String prompt = "";
            if(editMode){
                prompt = generatePrompt(candidateUser, repeatPassword, "update");
            }else{
                prompt = generatePrompt(candidateUser, repeatPassword, "add");
            }
            

            try {
                if(prompt.isEmpty()){
                    if(editMode){
                        if(password.isEmpty()){
                            User.updateUserWithoutPassword(selectedUserId, fname, mname, lname, suffix, email, privilege, contactNum, sex, birthDate, address, image);
                        }else{
                            User.updateUser(selectedUserId, fname, mname, lname, suffix, email, password, privilege, contactNum, sex, birthDate, address, image);
                        }
                        showModal("Success","Saved Changes");
                    }else{
                        User.addUser(fname, mname, lname, suffix, email, password, privilege, contactNum, sex, birthDate, address, image);
                        showModal("Success","Employee added successfully!");
                    }

                    clearFields();

                    //close
                    Stage stage = (Stage) addEmployeeBtn.getScene().getWindow();
                    stage.close();
                }else{
                    showModal("Failed", prompt);
                }
            } catch (SQLException ex) {
                System.out.println("Database error, check inputs");
                showModal("Failed", "Database error");
                ex.printStackTrace();
            }


    }

    
    private String generatePrompt(User user, String repeatedPassword, String mode){
        ArrayList<String> promptList = new ArrayList<>();
        String filterPrompt = "";
        
        String fnamePrompt = Filter.REQUIRED.name(user.getFname(), "First Name");
        String mnamePrompt = Filter.OPTIONAL.name(user.getMname(), "Middle Name");
        String lnamePrompt = Filter.REQUIRED.name(user.getLname(), "Last Name");
        String emailPrompt = Filter.REQUIRED.email(user.getEmail());

        String emailInUsePrompt = "";
        String passwordPrompt = "";

        if(mode.equalsIgnoreCase("add")){
            passwordPrompt = Filter.REQUIRED.password(user.getPassword(), repeatedPassword);
            emailInUsePrompt = Filter.REQUIRED.isEmailInUse(user.getEmail());
        }else{
            passwordPrompt = Filter.OPTIONAL.password(user.getPassword(), repeatedPassword);
            emailInUsePrompt = Filter.REQUIRED.isEmailInUseExceptCurrentUser(user.getEmail(), selectedUser.getEmail());
        }
        
        String privilegePrompt = Filter.REQUIRED.privilege(user.getPrivilege());
        
        //add prompts to promptList
        promptList.add(fnamePrompt);
        promptList.add(mnamePrompt);
        promptList.add(lnamePrompt);
        promptList.add(emailPrompt);
        promptList.add(passwordPrompt);
        promptList.add(privilegePrompt);
        promptList.add(emailInUsePrompt);
        
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
        paneUtil.openPane(paneUtil.FP_ENROLLMENT);
    }
    
    private byte[] readImageFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }
    
    public void setDataForEdit(User user, Stage stage, ADMIN_EmpMgmtCTRL adminEmpMgmtCTRL, boolean isEditMode) {
        editMode = isEditMode;
        selectedUser = user;

        if(isEditMode){
            selectedUserId = user.getId();
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
            //passwordField.setText(user.getPassword());
            privilegeChoiceBox.setValue(user.getPrivilege());
            contactNumField.setText(user.getContactNum());

            if(user.getSex() == null){
                sexChoiceBox.setValue("Select");
            }else{
                sexChoiceBox.setValue(user.getSex());
            }

            dateOfBirthPicker.setValue(user.getBirthDate());
            addressField.setText(user.getAddress());
            userImage.setImage(ImageUtil.byteArrayToImage(user.getImage()));

            // Set other fields as needed
            addEmployeeBtn.setText("Save Changes");
            passwordLabel.setText("Change Password");
            repeatPasswordLabel.setText("Repeat New Password");
        }


        stage.setOnHiding(event -> {
            adminEmpMgmtCTRL.loadUserTable();
            if(isEditMode){
                adminEmpMgmtCTRL.loadUserDetails(user.getId());
            }

        });
    }
    
//    private Image byteArrayToImage(byte[] byteArray) {
//        // Convert byte array to JavaFX Image
//        try{
//        return new Image(new java.io.ByteArrayInputStream(byteArray));
//        } catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }




    //CODE BY BARDDDDDD AIIIIIII
//    public static boolean checkFileSize(File imageFile) {
//        return imageFile.length() > (1024 * 1024); // 1MB
//    }
//
//    public static BufferedImage loadImage(String imagePath) throws IOException {
//        return ImageIO.read(new File(imagePath));
//    }
//
//    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
//        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//        resizedImage.getGraphics().drawImage(resultingImage, 0, 0, null);
//        return resizedImage;
//    }
//
//    public static void saveImage(BufferedImage resizedImage, String outputPath, String format) throws IOException {
//        ImageIO.write(resizedImage, format, new File(outputPath));
//    }

}

    