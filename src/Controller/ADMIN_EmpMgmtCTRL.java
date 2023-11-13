/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Assignment;
import Model.Department;
import Model.Shift;
import Model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    private TableColumn<User, String> col_birthday;
    @FXML
    private TableColumn<Assignment, Integer> col_assignment_id;
    @FXML
    private TableColumn<Assignment, String> col_department;
    @FXML
    private TableColumn<Assignment, String> col_position;
    @FXML
    private TableColumn<Assignment, String> col_shift;

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
        col_fname.setCellValueFactory(new PropertyValueFactory<User, String>("fName"));
        col_mname.setCellValueFactory(new PropertyValueFactory<User, String>("mName"));
        col_lname.setCellValueFactory(new PropertyValueFactory<User, String>("lName"));
        col_suffix.setCellValueFactory(new PropertyValueFactory<User, String>("suffix"));
        col_privilege.setCellValueFactory(new PropertyValueFactory<User, String>("privilege"));
        col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        col_contact_num.setCellValueFactory(new PropertyValueFactory<User, String>("contactNum"));
        col_birthday.setCellValueFactory(new PropertyValueFactory<User, String>("birthDate"));

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
}
