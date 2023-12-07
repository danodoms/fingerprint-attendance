/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Assignment;
import Model.Department;
import Model.Position;
import Model.Shift;
import Model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_AssignmentsCTRL implements Initializable {

    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<Assignment, String> col_department;
    @FXML
    private TableColumn<Assignment, String> col_position;
    @FXML
    private TableColumn<Assignment, String> col_shift;
    @FXML
    private TableColumn<Assignment, String> col_time;
    @FXML
    private TableColumn<Assignment, String> col_dateAssigned;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label userNameLabel;
    @FXML
    private ChoiceBox<String> userDeptFilterChoiceBox;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
    @FXML
    private ChoiceBox<Shift> shiftChoiceBox;
    @FXML
    private Spinner<String> startTimeSpinner;
    @FXML
    private Spinner<String> endTimeSpinner;
    @FXML
    private TableView<Assignment> assignmentTable;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private HBox buttonContainerHBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //USER TABLE
        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        
        //ASSIGNMENT TABLE
        col_department.setCellValueFactory(new PropertyValueFactory<Assignment, String>("department"));
        col_position.setCellValueFactory(new PropertyValueFactory<Assignment, String>("position"));
        col_shift.setCellValueFactory(new PropertyValueFactory<Assignment, String>("shift"));
        col_shift.setCellValueFactory(new PropertyValueFactory<Assignment, String>("shift"));
        
        //ASSIGNMENT CHOICEBOX
        departmentChoiceBox.getItems().addAll(Department.getDepartments());
        shiftChoiceBox.getItems().addAll(Shift.getShifts());
        
        
        positionChoiceBox.setOnMouseClicked(event -> {
            // Get the corresponding department from the departmentChoiceBox
            int selectedDepartmentId = departmentChoiceBox.getValue().getId();
            System.out.println("Selected Dept ID: "+selectedDepartmentId);
            positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartmentId));

        });
        
        addBtn.setVisible(false);
        updateBtn.setVisible(false);
        deactivateBtn.setVisible(false);
        
        loadUserTable();
    }    
    
    public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();
        userTable.setItems(users);
    }
    
    public void loadAssignmentTable(int user_id){
        ObservableList<Assignment> assignments = Assignment.getActiveAssignmentsByUserId(user_id);
        assignmentTable.setItems(assignments);
    }    

    @FXML
    private void userSelected(MouseEvent event) {
        User selectedItem = userTable.getSelectionModel().getSelectedItem();
        loadAssignmentTable(selectedItem.getId());
        
        showAddBtnOnly();
    }

    @FXML
    private void assignmentSelected(MouseEvent event) {
        Assignment selectedItem = assignmentTable.getSelectionModel().getSelectedItem();
        String department = selectedItem.getDepartment();
        String position = selectedItem.getPosition();
        String shift = selectedItem.getShift();
        //String startTime = selectedItem.getStartTime();
        //String endTime = selectedItem.getEndTime();
        
        //departmentChoiceBox.set;

        showUpdateDeactivateBtnOnly();
    }
    
    public void showAddBtnOnly(){
        // Clear existing children in the HBox
        buttonContainerHBox.getChildren().clear();

        // Add the addBtn with Hgrow set to ALWAYS
        HBox.setHgrow(addBtn, javafx.scene.layout.Priority.ALWAYS);
        buttonContainerHBox.getChildren().add(addBtn);

        // Set visibility for other buttons
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        deactivateBtn.setVisible(false);
    }
    
    public void showUpdateDeactivateBtnOnly(){
        // Clear existing children in the HBox
        buttonContainerHBox.getChildren().clear();

        // Add the addBtn with Hgrow set to ALWAYS
        HBox.setHgrow(updateBtn, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(deactivateBtn, javafx.scene.layout.Priority.ALWAYS);
        buttonContainerHBox.getChildren().add(updateBtn);
        buttonContainerHBox.getChildren().add(deactivateBtn);

        // Set visibility for other buttons
        addBtn.setVisible(false);
        updateBtn.setVisible(true);
        deactivateBtn.setVisible(true);
    }
}
