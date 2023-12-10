/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Assignment;
import Model.Department;
import Model.Shift;
import Model.User;
import Utilities.DatabaseUtil;
import Utilities.ImageUtil;
import Utilities.PaneUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    @FXML
    private ChoiceBox<Department> departmentFilter_choiceBox;
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
    private ChoiceBox<?> statusFilter_choiceBox;
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadUserTable();
        
        privilegeFilter_choiceBox.setValue("All");
        privilegeFilter_choiceBox.getItems().addAll("All","employee","admin","records officer");
        
        departmentFilter_choiceBox.setValue(new Department("All"));
        departmentFilter_choiceBox.getItems().addAll(new Department("All"));
        departmentFilter_choiceBox.getItems().addAll(Department.getDepartments());
        
        //USER TABLE
        col_user_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        col_privilege.setCellValueFactory(new PropertyValueFactory<User, String>("privilege"));
        //col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        //col_contact_num.setCellValueFactory(new PropertyValueFactory<User, String>("contactNum"));
        //col_birthday.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthDate"));
    }   
    
    public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();
        userTable.setItems(users);
    }
     
//    public void showAssignmentTable(int user_id){
//        ObservableList<Assignment> assignments = Assignment.getAssignmentByUserId(user_id);
//        assignmentTable.setItems(assignments);
//    }    

    @FXML
    private void userSelected(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        Image userImage = ImageUtil.byteArrayToImage(User.getUserImageByUserId(selectedUser.getId()));
        
        userImageView.setImage(userImage);
        nameLabel.setText(selectedUser.getFullName());
        emailLabel.setText(selectedUser.getEmail());
        birthDateLabel.setText(selectedUser.getBirthDate()+"");
        contactNumLabel.setText(selectedUser.getContactNum());
        addressLabel.setText(User.getUserByUserId(selectedUser.getId()).getAddress());
        
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

            // Pass data to the AddUserFormController
            addUserFormController.setDataForEdit(userFromDb);

            // Show the AddUserForm in the original pane
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);
            
            // Set the onHidden event handler to reload userTable when add_employee_pane is closed
            secondStage.setOnHidden(e -> {
                // Reload userTable when the add_employee_pane is closed
                loadUserTable();
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
    private void deactivateUser(ActionEvent event) {
        User selectedItem = userTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int id = selectedItem.getId();
            String query = "UPDATE user SET user_status = 0 WHERE user_id = " + id;

            DatabaseUtil.executeQuery(query);
            loadUserTable();
            //clearFields();
            //assignment_table.getItems().clear();
        } else {
            // Handle case when no row is selected or handle error.
            // You can show a message or perform other actions here.
        }
    }
}
