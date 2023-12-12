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
import javafx.scene.layout.HBox;

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
    private HBox buttonContainerHBox;
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
    
    Department selectedDept= null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //DEPARTMENT TABLE
        col_id.setCellValueFactory(new PropertyValueFactory<Department, String>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<Department, String>("departmentName"));
        //col_description.setCellValueFactory(new PropertyValueFactory<Department, String>());
        
        loadDepartmentTable();
        //showAddBtnOnly();
    }
    
    private void loadDepartmentTable(){
        ObservableList<Department> departments = Department.getDepartments();
        departmentTable.setItems(departments);
    }


    @FXML
    private void departmentSelected(MouseEvent event) {
        //showUpdateDeactivateBtnOnly();
        selectedDept = departmentTable.getSelectionModel().getSelectedItem();
        
        departmentNameField.setText(selectedDept.getDepartmentName());
        //departmentDescTextArea.setText(selectedDept.getDescription());
    }
    
    
    
    
    //     public void showAddBtnOnly(){
//        // Clear existing children in the HBox
//        buttonContainerHBox.getChildren().clear();
//
//        // Add the addBtn with Hgrow set to ALWAYS
//        HBox.setHgrow(addBtn, javafx.scene.layout.Priority.ALWAYS);
//        buttonContainerHBox.getChildren().add(addBtn);
//
//        // Set visibility for other buttons
//        addBtn.setVisible(true);
//        updateBtn.setVisible(false);
//        deactivateBtn.setVisible(false);
//    }
//    
//    public void showUpdateDeactivateBtnOnly(){
//        // Clear existing children in the HBox
//        buttonContainerHBox.getChildren().clear();
//
//        // Add the addBtn with Hgrow set to ALWAYS
//        HBox.setHgrow(updateBtn, javafx.scene.layout.Priority.ALWAYS);
//        HBox.setHgrow(deactivateBtn, javafx.scene.layout.Priority.ALWAYS);
//        buttonContainerHBox.getChildren().add(updateBtn);
//        buttonContainerHBox.getChildren().add(deactivateBtn);
//
//        // Set visibility for other buttons
//        addBtn.setVisible(false);
//        updateBtn.setVisible(true);
//        deactivateBtn.setVisible(true);
//    }

    @FXML
    private void addDepartment(ActionEvent event) throws SQLException {
        String departmentName = departmentNameField.getText();
        String departmentDesc = departmentDescTextArea.getText();

        if(departmentName.isEmpty()){
            Modal.showModal("Error", "Please fill up all fields");
        }else{
            Department.addDepartment(departmentName);
            loadDepartmentTable();
        }
    }

    @FXML
    private void updateDepartment(ActionEvent event) throws SQLException {
        boolean actionIsConfirmed = Modal.showConfirmationModal("Update", "Do you want to proceed?", "This action will update the currently selected department");
        
        if(actionIsConfirmed){
            Department.updateDepartment(selectedDept.getId(), departmentNameField.getText());
            loadDepartmentTable();
        }
    }

    @FXML
    private void deactivateDepartment(ActionEvent event) throws SQLException {
        boolean actionIsConfirmed = Modal.showConfirmationModal("Deactivate", "Do you want to proceed?", "This action will deactivate the currently selected department");
        
        if(actionIsConfirmed){
            Department.deactivateDepartment(selectedDept.getId());
            loadDepartmentTable();
        }
       
    }
}
