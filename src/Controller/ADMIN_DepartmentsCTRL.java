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

    Department selectedDept= null;
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

        addBtn.setDisable(true);
        loadDepartmentTable();
        //showAddBtnOnly();

        departmentNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                addBtn.setDisable(true);
            }else{
                addBtn.setDisable(false);
            }
        });
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
        addBtn.setDisable(true);
        selectedDept = departmentTable.getSelectionModel().getSelectedItem();
        
        departmentNameField.setText(selectedDept.getDepartmentName());
        departmentDescTextArea.setText(selectedDept.getDescription());
    }
    
    


    @FXML
    private void addDepartment(ActionEvent event) throws SQLException {
        String departmentName = departmentNameField.getText();
        String departmentDesc = departmentDescTextArea.getText();

        if(departmentName.isEmpty()){
            Modal.showModal("Error", "Please fill up all fields");
        }else{
            Department.addDepartment(departmentName, departmentDesc);
            loadDepartmentTable();
            clearFields();
        }
    }

    @FXML
    private void updateDepartment(ActionEvent event) throws SQLException {
        boolean actionIsConfirmed = Modal.showConfirmationModal("Update", "Do you want to proceed?", "This action will update the currently selected department");
        
        if(actionIsConfirmed){
            Department.updateDepartment(selectedDept.getId(), departmentNameField.getText(), departmentDescTextArea.getText());
            loadDepartmentTable();
            clearFields();
        }
    }

    @FXML
    private void deactivateDepartment(ActionEvent event) throws SQLException {
        boolean actionIsConfirmed = Modal.showConfirmationModal("Deactivate", "Do you want to proceed?", "This action will deactivate the currently selected department");
        
        if(actionIsConfirmed){
            Department.deactivateDepartment(selectedDept.getId());
            loadDepartmentTable();
            clearFields();
        }
       
    }

    private void clearFields(){
        departmentNameField.setText("");
        departmentDescTextArea.setText("");
    }
}
