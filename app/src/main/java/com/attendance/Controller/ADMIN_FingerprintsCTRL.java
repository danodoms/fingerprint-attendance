/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Fingerprint;
import Model.User;
import Utilities.ImageUtil;
import Utilities.PaneUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_FingerprintsCTRL implements Initializable {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private Button enrollBtn;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label fingerprintCountLabel;
    @FXML
    private Label lastEnrollDateLabel;
    @FXML
    private Label nameLabel;
    
    PaneUtil paneUtil = new PaneUtil();
    @FXML
    private ChoiceBox statusFilterChoiceBox;
    @FXML
    private TextField searchFilterField;

    User userFromDb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //empty labels
        nameLabel.setText("");
        

        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));

        //INIT FILTER CHOICEBOX
        statusFilterChoiceBox.getItems().addAll("All", "Enrolled", "Unenrolled");
        statusFilterChoiceBox.setValue("All");

        //add event listener to statusFilterChoiceBox that call loadUserTable() when changed
        statusFilterChoiceBox.setOnAction((event) -> {
            loadUserTable();
        });

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadUserTable();
        });

        loadUserTable();
    }    
    
     public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();

        //filter usertable based on statusFilterChoiceBox, store in new list, do it like this: iterate to user list, and check if user has fingerprints on database or not
        ObservableList<User> filteredUsers = users.filtered(user -> {
            String statusFilter = statusFilterChoiceBox.getValue().toString();
            if(statusFilter.equalsIgnoreCase("All")){
                return true;
            }else if(statusFilter.equalsIgnoreCase("Enrolled")){
                return Fingerprint.getFingerprintCountByUserId(user.getId()) > 0;
            }else if(statusFilter.equalsIgnoreCase("Unenrolled")){
                return Fingerprint.getFingerprintCountByUserId(user.getId()) == 0;
            }
            return false;
        });

         //then, filter based on searchFilterField, store in new list
         filteredUsers = filteredUsers.filtered(user -> {
             String searchFilter = searchFilterField.getText().toLowerCase();
             if(searchFilter.isEmpty()){
                 return true;
             }else{
                 return user.getFullName().toLowerCase().contains(searchFilter);
             }
         });


        userTable.setItems(filteredUsers);
    }

    @FXML
    private void loadUserDetailsAction(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        userFromDb = User.getUserByUserId(selectedUser.getId());
        loadUserDetails(selectedUser);

    }

    public void loadUserDetails(User selectedUser){

        int fingerprintCount = Fingerprint.getFingerprintCountByUserId(selectedUser.getId());
        String lastFingerprintEnroll = Fingerprint.getLastFingerprintEnrollByUserId(selectedUser.getId());
        byte[] userImage = User.getUserByUserId(selectedUser.getId()).getImage();



        fingerprintCountLabel.setText(fingerprintCount+"");
        nameLabel.setText(userFromDb.getFullNameWithInitial());
        lastEnrollDateLabel.setText(lastFingerprintEnroll);
        userImageView.setImage(ImageUtil.byteArrayToImage(userImage));


        if(fingerprintCount > 0){
            enrollBtn.setText("Re-Enroll");
        }else{
            enrollBtn.setText("Enroll");
        }



        System.out.println("Fingerprint cOUNT: "+ fingerprintCount+"");

    }

    @FXML
    private void erollBtnAction(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        int selectedUserId = selectedUser.getId();
        
        String enrollBtnText = enrollBtn.getText();
        
        if(!(enrollBtnText.equalsIgnoreCase("Enroll"))){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Re-Enroll");
            alert.setHeaderText("Do you want to proceed?");
            alert.setContentText("This action will destroy this User's Fingerprints stored in the database");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");

            alert.getButtonTypes().setAll(yesButton, noButton);

            // Get the result of the prompt
            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    System.out.println("User clicked Yes");
                    // Perform actions for Yes
                    Fingerprint.destroyEnrolledFingerprintsByUserId(selectedUserId);
                    openFpEnrollmentPane();
                    loadUserDetails(selectedUser);
                    loadUserTable();
                } else if (response == noButton) {
                    System.out.println("User clicked No");
                    // Perform actions for No
                }
            });
        }else{
            openFpEnrollmentPane(); 
        }
    }
    
    private void openFpEnrollmentPane() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        int selectedUserId = selectedUser.getId();
        User userFromDb = User.getUserByUserId(selectedUserId);
        try {
            // Load the AddUserForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.FP_ENROLLMENT));
            Parent root = loader.load();

            // Get the controller of the AddUserForm
            FP_EnrollmentCTRL fpEnrollmentCtrl = loader.getController();



            // Show the AddUserForm in the original pane
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);

            // Pass data to the AddUserFormController
            fpEnrollmentCtrl.setDataForEnrollment(userFromDb, secondStage, this);

//            secondStage.setOnHidden(e -> {
//                loadUserTable();
//
//            });

            secondStage.show();
            //originalPane.getChildren().add(addUserForm);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
        //paneUtil.openPane(paneUtil.ADD_EMPLOYEE_PANE);
    }


     
    
}
