/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Fingerprint;
import Model.User;
import Utilities.ImageUtil;
import Utilities.PaneUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        
        loadUserTable();
    }    
    
     public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();
        userTable.setItems(users);
    }

    @FXML
    private void loadUserDetails(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        int fingerprintCount = Fingerprint.getFingerprintCountByUserId(selectedUser.getId());
        String lastFingerprintEnroll = Fingerprint.getLastFingerprintEnrollByUserId(selectedUser.getId());
        byte[] userImage = User.getUserByUserId(selectedUser.getId()).getImage();
        
        
       
        fingerprintCountLabel.setText(fingerprintCount+"");
        nameLabel.setText(selectedUser.getFullName());
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

            // Pass data to the AddUserFormController
            fpEnrollmentCtrl.setDataForEnrollment(userFromDb);

            // Show the AddUserForm in the original pane
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);

//            secondStage.setOnHidden(e -> {
//                loadUserTable();
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
