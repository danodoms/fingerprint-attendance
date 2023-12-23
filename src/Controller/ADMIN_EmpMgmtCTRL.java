/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Assignment;
import Model.Position;
import Model.Shift;
import Model.User;
import Utilities.DatabaseUtil;
import Utilities.ImageUtil;
import Utilities.Modal;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_EmpMgmtCTRL implements Initializable {

    @FXML
    private TableView<User> userTable;
    private TableView<Assignment> assignmentTable;
    @FXML
    private ChoiceBox<String> privilegeFilter_choiceBox;
    private ChoiceBox<Shift> shiftFilter_choiceBox;
    @FXML
    private TableColumn<User, Integer> col_user_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private TableColumn<User, String> col_privilege;
    private TableColumn<User, String> col_email;
    private TableColumn<User, String> col_contact_num;
    private TableColumn<User, LocalDate> col_birthday;
    private TableColumn<Assignment, Integer> col_assignment_id;
    private TableColumn<Assignment, String> col_department;
    private TableColumn<Assignment, String> col_position;
    private TableColumn<Assignment, String> col_shift;
    @FXML
    private Button editUserBtn;
    @FXML
    private Button addEmpBtn;
    @FXML
    private Button deactivateUserBtn;
    
    PaneUtil paneUtil = new PaneUtil();
    @FXML
    private ChoiceBox<String> statusFilter_choiceBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private Label contactNumLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private ImageView userImageView;
    @FXML
    private TextField searchFilterField;

    User selectedUser;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //INIT privilege FILTER CHOICEBOX
        privilegeFilter_choiceBox.setValue("All");
        privilegeFilter_choiceBox.getItems().addAll("All","Employee","Admin","Records Officer");

        //init status filter choicebox
        statusFilter_choiceBox.setValue("Active");
        statusFilter_choiceBox.getItems().addAll("Active","Inactive");

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadUserTable();
        });

        //add event listener for privilegeFilter_choiceBox that calls loadUserTable() when changed
        privilegeFilter_choiceBox.setOnAction((event) -> {
            loadUserTable();
        });

        //add event listener for statusFilter_choiceBox that calls loadUserTable() when changed
        statusFilter_choiceBox.setOnAction((event) -> {
            loadUserTable();
        });
        
        //USER TABLE
        col_user_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        col_privilege.setCellValueFactory(new PropertyValueFactory<User, String>("privilege"));
        //col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        //col_contact_num.setCellValueFactory(new PropertyValueFactory<User, String>("contactNum"));
        //col_birthday.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthDate"));

        loadUserTable();
    }   
    
    public void loadUserTable(){
        ObservableList<User> users = User.getEmployees();

        //filter usertable based on privilegeFilter_choiceBox, store in new list
        ObservableList<User> filteredUsers = users.filtered(user -> {
            String privilegeFilter = privilegeFilter_choiceBox.getValue().toString();
            if(privilegeFilter.equalsIgnoreCase("All")){
                return true;
            }else{
                return user.getPrivilege().equalsIgnoreCase(privilegeFilter);
            }
        });

        //then, filter by statusFilter_choiceBox, store in new list, only active and inactive option
        filteredUsers = filteredUsers.filtered(user -> {
            String statusFilter = statusFilter_choiceBox.getValue().toString();
            if(statusFilter.equalsIgnoreCase("Active")){
                return user.getStatus() == 1;
            }else if(statusFilter.equalsIgnoreCase("Inactive")){
                return user.getStatus() == 0;
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
     
//    public void showAssignmentTable(int user_id){
//        ObservableList<Assignment> assignments = Assignment.getAssignmentByUserId(user_id);
//        assignmentTable.setItems(assignments);
//    }    

    @FXML
    private void userSelected(MouseEvent event) {
        selectedUser = userTable.getSelectionModel().getSelectedItem();
        Image userImage = ImageUtil.byteArrayToImage(User.getUserImageByUserId(selectedUser.getId()));
        
        userImageView.setImage(userImage);
        nameLabel.setText(selectedUser.getFullName());
        emailLabel.setText(selectedUser.getEmail());
        birthDateLabel.setText(selectedUser.getBirthDate()+"");
        contactNumLabel.setText(selectedUser.getContactNum());
        addressLabel.setText(User.getUserByUserId(selectedUser.getId()).getAddress());

        if(selectedUser.getStatus() == 1){
            deactivateUserBtn.setText("Deactivate");
        }else{
            deactivateUserBtn.setText("Activate");
        }
    }

    @FXML
    private void openEditUserPane(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        int selectedUserId = selectedUser.getId();
        User userFromDb = User.getUserByUserId(selectedUserId);
        try {
            // Load the AddUserForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.ADMIN_ADD_EMP));
            Parent root = loader.load();

            // Get the controller of the AddUserForm
            ADMIN_AddEmpCTRL addUserFormController = loader.getController();

            addUserFormController.setDataForEdit(userFromDb);

            // Show the AddUserForm in the original pane
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the onHidden event handler to reload userTable when add_employee_pane is closed
            secondStage.setOnHidden(e -> {
                // Reload userTable when the add_employee_pane is closed
                loadUserTable();
                System.out.println("YESSSSSSSSSSSSSSS USER TABLE IS RELOADED ASJDHJIASBDJAHSVBDFSHUJBDFZXHUVBFXDAUHFBFBDSHJKFGVJHKTGSDFHJVBGFDHUJFDHUJFGBFGBRUJHVGBSDFHUFVGBSDYUGVGUJIHKFRWFGVHU");
            });

            
            secondStage.show();
            //originalPane.getChildren().add(addUserForm);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
        //paneUtil.openPane(paneUtil.ADD_EMPLOYEE_PANE);
    }

    @FXML
    private void openAddEmpPane(ActionEvent event) {
         paneUtil.openModal(paneUtil.ADMIN_ADD_EMP);
    }

    @FXML
    private void invertUserStatus(ActionEvent event) throws SQLException {
        String actionType = selectedUser.getStatus() == 1 ? "Deactivate" : "Activate";
        String confirmationMessage = actionType + " this user?";
        String actionDescription = "This action will " + actionType.toLowerCase() + " the currently selected user";

        if (Modal.actionConfirmed(actionType, confirmationMessage, actionDescription)) {
            User.invertUserStatus(selectedUser.getId());
            loadUserTable();
        }
    }
}
