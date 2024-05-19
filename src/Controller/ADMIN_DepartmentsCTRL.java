/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Department;
import Utilities.Modal;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_DepartmentsCTRL implements Initializable {

    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private TableColumn<Department, String> col_id;
    @FXML
    private TableColumn<Department, String> col_name;
    @FXML
    private TableColumn<Department, String> col_description;
    @FXML
    private TextField departmentNameField;
    @FXML
    private TextArea departmentDescTextArea;
    @FXML
    private ChoiceBox statusFilterChoiceBox;

    Department selectedDept = null;
    @FXML
    private TextField searchField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //DEPARTMENT TABLE
        col_id.setCellValueFactory(new PropertyValueFactory<Department, String>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));
        col_description.setCellValueFactory(new PropertyValueFactory<Department, String>("description"));

        //STATUS FILTER
        statusFilterChoiceBox.getItems().addAll("Active", "Inactive");
        statusFilterChoiceBox.setValue("Active");

        statusFilterChoiceBox.setOnAction((event) -> {
            loadDepartmentTable();
        });

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadDepartmentTable();
        });

        loadDepartmentTable();
        //showAddBtnOnly();


        //adds event listener to departmentNameField that disables addBtn if empty
//        departmentNameField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue.isEmpty()){
//                addBtn.setDisable(true);
//            }else{
//                addBtn.setDisable(false);
//            }
//        });
    }
    
    private void loadDepartmentTable(){
        ObservableList<Department> departments = Department.getDepartments();

        //filter the departements based on the status filter, store in a new observable list, use native methods
        ObservableList<Department> filteredDepartments = departments.filtered(department -> {
            String statusFilter = statusFilterChoiceBox.getValue().toString();
            if(statusFilter.equals("Active")){
                return department.getStatus() == 1;
            }else{
                return department.getStatus() == 0;
            }
        });

        //add code that filters department based on searchField
        filteredDepartments = filteredDepartments.filtered(department -> {
            String searchFilter = searchField.getText().toLowerCase();
            if(searchFilter.isEmpty()){
                return true;
            }else{
                return department.getDepartmentName().toLowerCase().contains(searchFilter);
            }
        });

        departmentTable.setItems(filteredDepartments);

    }


    @FXML
    private void departmentSelected(MouseEvent event) {
        //showUpdateDeactivateBtnOnly();
        selectedDept = departmentTable.getSelectionModel().getSelectedItem();
        
        departmentNameField.setText(selectedDept.getDepartmentName());
        departmentDescTextArea.setText(selectedDept.getDescription());

        if(selectedDept.getStatus() == 1){
            deactivateBtn.setText("Deactivate");
        }else{
            deactivateBtn.setText("Activate");
        }
    }
    
    


    @FXML
    private void addDepartment(ActionEvent event) throws SQLException {
        String departmentName = departmentNameField.getText();
        String departmentDesc = departmentDescTextArea.getText();

        if(departmentName.isEmpty()){
            Modal.showModal("Add Department", "Please fill up all fields");
            return;
        }

        if(Department.departmentNameExists(departmentName)){
            //show modal that department already exists
            Modal.showModal("Add Department", "Department already exists");
        }else{
            boolean actionIsConfirmed = Modal.actionConfirmed("Add Department", "Add this department?", "This action will add the new department");

            if(actionIsConfirmed){
                Department.addDepartment(departmentName, departmentDesc);
                loadDepartmentTable();
                clearFields();
            }
        }
    }

    @FXML
    private void updateDepartment(ActionEvent event) throws SQLException {
        boolean actionIsConfirmed = Modal.actionConfirmed("Update", "Update this department?", "This action will update the currently selected department");
        
        if(actionIsConfirmed){
            Department.updateDepartment(selectedDept.getId(), departmentNameField.getText(), departmentDescTextArea.getText());
            loadDepartmentTable();
            clearFields();
        }
    }

    @FXML
    private void invertDepartmentStatus(ActionEvent event) throws SQLException {
        String actionType = selectedDept.getStatus() == 1 ? "Deactivate" : "Activate";
        String confirmationMessage = actionType + " this department?";
        String actionDescription = "This action will " + actionType.toLowerCase() + " the currently selected department";

        if (Modal.actionConfirmed(actionType, confirmationMessage, actionDescription)) {
            Department.invertDepartmentStatus(selectedDept.getId());
            loadDepartmentTable();
            clearFields();
        }
    }

    private void clearFields(){
        departmentNameField.setText("");
        departmentDescTextArea.setText("");
    }
}
