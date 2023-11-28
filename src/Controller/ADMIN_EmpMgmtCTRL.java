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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableView<Assignment> assignmentTable;
    @FXML
    private ChoiceBox<String> privilegeFilter_choiceBox;
    @FXML
    private ChoiceBox<Department> departmentFilter_choiceBox;
    @FXML
    private ChoiceBox<Shift> shiftFilter_choiceBox;
    @FXML
    private TableColumn<User, Integer> col_user_id;
    @FXML
    private TableColumn<User, String> col_fname;
    @FXML
    private TableColumn<User, String> col_mname;
    @FXML
    private TableColumn<User, String> col_lname;
    @FXML
    private TableColumn<User, String> col_suffix;
    @FXML
    private TableColumn<User, String> col_privilege;
    @FXML
    private TableColumn<User, String> col_email;
    @FXML
    private TableColumn<User, String> col_contact_num;
    @FXML
    private TableColumn<User, LocalDate> col_birthday;
    @FXML
    private TableColumn<Assignment, Integer> col_assignment_id;
    @FXML
    private TableColumn<Assignment, String> col_department;
    @FXML
    private TableColumn<Assignment, String> col_position;
    @FXML
    private TableColumn<Assignment, String> col_shift;
    @FXML
    private Button editUserBtn;

    
    PaneUtil paneUtil = new PaneUtil();
    @FXML
    private Button addEmpBtn;
    @FXML
    private Button deactivateUserBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showUserTable();
        
        privilegeFilter_choiceBox.setValue("All");
        privilegeFilter_choiceBox.getItems().addAll("All","employee","admin","records officer");
        
        shiftFilter_choiceBox.setValue(new Shift("All"));
        shiftFilter_choiceBox.getItems().addAll(new Shift("All"));
        shiftFilter_choiceBox.getItems().addAll(Shift.getShifts());
        
        departmentFilter_choiceBox.setValue(new Department("All"));
        departmentFilter_choiceBox.getItems().addAll(new Department("All"));
        departmentFilter_choiceBox.getItems().addAll(Department.getDepartments());
        
        //USER TABLE
        col_user_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_fname.setCellValueFactory(new PropertyValueFactory<User, String>("fname"));
        col_mname.setCellValueFactory(new PropertyValueFactory<User, String>("mname"));
        col_lname.setCellValueFactory(new PropertyValueFactory<User, String>("lname"));
        col_suffix.setCellValueFactory(new PropertyValueFactory<User, String>("suffix"));
        col_privilege.setCellValueFactory(new PropertyValueFactory<User, String>("privilege"));
        col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        col_contact_num.setCellValueFactory(new PropertyValueFactory<User, String>("contactNum"));
        col_birthday.setCellValueFactory(new PropertyValueFactory<User, LocalDate>("birthDate"));

        //ASSIGNMENT TABLE
        col_assignment_id.setCellValueFactory(new PropertyValueFactory<Assignment, Integer>("id"));
        col_position.setCellValueFactory(new PropertyValueFactory<Assignment, String>("position"));
        col_department.setCellValueFactory(new PropertyValueFactory<Assignment, String>("department"));
        col_shift.setCellValueFactory(new PropertyValueFactory<Assignment, String>("shift"));
    }    
    
     public void showUserTable(){
        ObservableList<User> users = User.getUsers();
        userTable.setItems(users);
    }
     
    public void showAssignmentTable(int user_id){
        ObservableList<Assignment> assignments = Assignment.getAssignmentByUserId(user_id);
        assignmentTable.setItems(assignments);
    }    

    @FXML
    private void updateAssignmentTable(MouseEvent event) {
        User selectedItem = userTable.getSelectionModel().getSelectedItem();
        showAssignmentTable(selectedItem.getId());
    }

    @FXML
    private void openEditUserPane(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        try {
            // Load the AddUserForm.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource(paneUtil.ADD_EMPLOYEE_PANE));
            Parent root = loader.load();

            // Get the controller of the AddUserForm
            ADMIN_AddEmpCTRL addUserFormController = loader.getController();

            // Pass data to the AddUserFormController
            addUserFormController.setDataForEdit(selectedUser);

            // Show the AddUserForm in the original pane
            Stage secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initModality(Modality.APPLICATION_MODAL);
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
         paneUtil.openModal(paneUtil.ADD_EMPLOYEE_PANE);
    }

    @FXML
    private void deactivateUser(ActionEvent event) {
        User selectedItem = userTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            int id = selectedItem.getId();
            String query = "UPDATE user SET user_status = 0 WHERE user_id = " + id;

            DatabaseUtil.executeQuery(query);
            showUserTable();
            //clearFields();
            //assignment_table.getItems().clear();
        } else {
            // Handle case when no row is selected or handle error.
            // You can show a message or perform other actions here.
        }
    }
}
