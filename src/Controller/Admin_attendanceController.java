/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author User
 */
public class Admin_attendanceController implements Initializable {  

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView adminTableView;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
    @FXML
    private ChoiceBox<Shift> shiftTypeChoiceBox;
    @FXML
    private TextField startTimeField, endTimeField;
    @FXML
    private Button resetBtn;
    
    dbMethods dbMethods = new dbMethods();
    controllerMethods method = new controllerMethods();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //DEPARTMENT CHOICE BOX INITIALIZATION
         departmentChoiceBox.setItems(Department.getDepartments());
         departmentChoiceBox.setOnAction(this::updatePositionChoiceBox);
         
         //SHIFT TYPE CHOICE BOX INITIALIZATION
         shiftTypeChoiceBox.setItems(Shift.getShifts());
         shiftTypeChoiceBox.setOnAction(this::showShiftDetails);
        
    }
    private void updatePositionChoiceBox(ActionEvent event) {
        System.out.println("update position choice box");
        Department selectedDepartment = departmentChoiceBox.getValue();
        positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartment.getId()));
    }
    private void showShiftDetails(ActionEvent event) {
        
        //Stores the selected shift
        Shift selectedShift = shiftTypeChoiceBox.getValue();
        
        //Stores the id of the selected shift
        int id = selectedShift.getId();
        
        String startTime = selectedShift.getStartTime();
        String endTime = selectedShift.getEndTime();
        
        startTimeField.setText(startTime);
        endTimeField.setText(endTime);
        
        //DEBUGGER
        System.out.println("SelectedShiftID: " + id);
        System.out.println("Selected Shift: " + selectedShift);
        System.out.println("SelectedShiftStart: " + selectedShift.getStartTime());
        System.out.println("SelectedShiftEnd: " + selectedShift.getEndTime());
        
    }
    @FXML
    public void clearChoiceBox(ActionEvent event){
        departmentChoiceBox.setValue(null);
        positionChoiceBox.setValue(null);
        shiftTypeChoiceBox.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
    }
}
