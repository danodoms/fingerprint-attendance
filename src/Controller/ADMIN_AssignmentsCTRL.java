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
import Utilities.ImageUtil;
import Utilities.Modal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<Assignment> assignmentTable;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private HBox buttonContainerHBox;
    @FXML
    private TextField startTimeHourField;
    @FXML
    private TextField startTimeMinuteField;
    @FXML
    private TextField endTimeHourField;
    @FXML
    private TextField endTimeMinuteField;

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
        col_time.setCellValueFactory(new PropertyValueFactory<Assignment, String>("timeRange"));
        col_dateAssigned.setCellValueFactory(new PropertyValueFactory<Assignment, String>("dateAssigned"));
        
        //ASSIGNMENT CHOICEBOX
        departmentChoiceBox.getItems().addAll(Department.getDepartments());
        shiftChoiceBox.getItems().addAll(Shift.getShifts());
        
        
        positionChoiceBox.setOnMouseClicked(event -> {
            // Get the corresponding department from the departmentChoiceBox
            int selectedDepartmentId = departmentChoiceBox.getValue().getId();
            System.out.println("Selected Dept ID: "+selectedDepartmentId);
            positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartmentId));
        });
        
        shiftChoiceBox.setOnAction(event -> {
            //System.out.println(Arrays.toString(shiftChoiceBox.getValue().getStartTime().split(":")));
            
            String startTime = shiftChoiceBox.getValue().getStartTime();
            String endTime = shiftChoiceBox.getValue().getEndTime();
            
            
            if(!(startTime.equals(""))){
                String startTimeHour = shiftChoiceBox.getValue().getStartTime().split(":")[0];
                String startTimeMinute = shiftChoiceBox.getValue().getStartTime().split(":")[1];

                startTimeHourField.setText(startTimeHour);
                startTimeMinuteField.setText(startTimeMinute);
            }
            
            
            if(!(endTime.equals(""))){
                String endTimeHour = shiftChoiceBox.getValue().getEndTime().split(":")[0];
                String endTimeMinute = shiftChoiceBox.getValue().getEndTime().split(":")[1];

                endTimeHourField.setText(endTimeHour);
                endTimeMinuteField.setText(endTimeMinute);
            }
        });
        
        addBtn.setVisible(false);
        updateBtn.setVisible(false);
        deactivateBtn.setVisible(false);
        
        userImageView.setVisible(false);
        userNameLabel.setVisible(false);
        
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
        showAddBtnOnly();
        User selectedItem = userTable.getSelectionModel().getSelectedItem();
        loadAssignmentTable(selectedItem.getId());
        
        userNameLabel.setText(selectedItem.getFullName());
        userImageView.setImage(ImageUtil.byteArrayToImage(User.getUserImageByUserId(selectedItem.getId())));
        userImageView.setVisible(true);
        userNameLabel.setVisible(true);
        
        clearFields();
        
    }

    @FXML
    private void assignmentSelected(MouseEvent event) {
        showUpdateDeactivateBtnOnly();
        Assignment selectedItem = assignmentTable.getSelectionModel().getSelectedItem();
        String department = selectedItem.getDepartment();
        String position = selectedItem.getPosition();
        String shift = selectedItem.getShift();
        //String startTime = selectedItem.getStartTime();
        //String endTime = selectedItem.getEndTime();
        
        departmentChoiceBox.setValue(new Department(department));
        positionChoiceBox.setValue(new Position(position));
        shiftChoiceBox.setValue(new Shift(shift));

        
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
    
    public void clearFields(){
        departmentChoiceBox.setValue(null);
        positionChoiceBox.setValue(null);
        shiftChoiceBox.setValue(new Shift(""));
        
        startTimeHourField.clear();
        startTimeMinuteField.clear();
        endTimeHourField.clear();
        endTimeMinuteField.clear();
    }

    @FXML
    private void addAssignment(ActionEvent event) {
        int userId = userTable.getSelectionModel().getSelectedItem().getId();
        int positionId = positionChoiceBox.getValue().getId();
        int shiftId = shiftChoiceBox.getValue().getId();
        
        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();
        
        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateAssigned = currentDate.format(formatter);
        
        try{
            Assignment.addAssignment(userId, positionId, shiftId, startTime, endTime, dateAssigned);
        } catch(SQLException ex){
            Modal.showModal("Failed", "Database Error");
        }
        
        Modal.showModal("Success", "Assignment Added");
        loadAssignmentTable(userId);
    }

    @FXML
    private void updateAssignment(ActionEvent event) {
    }

    @FXML
    private void deactivateAssignment(ActionEvent event) {
    }
}
