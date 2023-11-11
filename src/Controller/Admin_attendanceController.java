/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import static Model.Attendance.getAttendance;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 *
 * @author User
 */
public class Admin_attendanceController implements Initializable {  
    @FXML
    private TableView<Attendance> adminTableView;
    @FXML
    private TableColumn<Attendance, String> status;
    @FXML
    private TableColumn<Attendance, String> nameCol,tiCol,toCol;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
    @FXML
    private ChoiceBox<Shift> shiftTypeChoiceBox;
    @FXML
    private TextField startTimeField, endTimeField, searchBar;
    @FXML
    private Button resetBtn;
    
    
    dbMethods dbMethods = new dbMethods();
    controllerMethods method = new controllerMethods();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();
    }

    private void updatePositionChoiceBox(ActionEvent event) {
        System.out.println("update position choice box");
        Department selectedDepartment = departmentChoiceBox.getValue();
        positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartment.getId()));     
    }
    
    private void showShiftDetails(ActionEvent event) {
        Shift selectedShift = shiftTypeChoiceBox.getValue();
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
        setTable();
    }
    
    @FXML
    public void setTable(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tiCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        status.setCellValueFactory(new PropertyValueFactory<>("attendance_status"));
        adminTableView.setItems(Attendance.getAttendance());
        searchBar.setText("");
        searchBar.setPromptText("Search name...");
        
        shiftTypeChoiceBox.setValue(new Shift("All"));
        shiftTypeChoiceBox.getItems().addAll(new Shift("All"));
        shiftTypeChoiceBox.getItems().addAll(Shift.getShifts());
        shiftTypeChoiceBox.setOnAction(this::showShiftDetails);
         
        departmentChoiceBox.setValue(new Department("All"));
        departmentChoiceBox.getItems().addAll(new Department("All"));
        departmentChoiceBox.getItems().addAll(Department.getDepartments());
        departmentChoiceBox.setOnAction(this::updatePositionChoiceBox);
        datePicker.setValue(null);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        datePicker.setConverter(new StringConverter<LocalDate>(){   
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                     return LocalDate.parse(string, dateFormatter);
                }else{
                    return null;
                }
            }
        });
    }
    
    @FXML
    private void filterTable(KeyEvent event) {
    ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
    String keyword = searchBar.getText().toLowerCase(); // Get the search keyword in lowercase
        for (Attendance attendance : getAttendance()) {    
            if ((attendance.getName().toLowerCase()).contains(keyword)){
                filteredData.add(attendance);
            adminTableView.setItems(filteredData);
            }
        }
    }  
    
    @FXML
    private void filterTableView(ActionEvent event) { 
        LocalDate selectedDate = datePicker.getValue();
        if(selectedDate != null){
            try {
                // Create a filtered list based on the selected date
                ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
                for (Attendance attendance : getAttendance()) {
                    if (attendance.getDate().toString().equals(selectedDate.toString())) {
                        filteredData.add(attendance);
                    }
                }
                // Update the TableView with the filtered data
                adminTableView.setItems(filteredData);
            }catch (Exception e) {
                e.printStackTrace(); // This prints the exception details to the console
            }
        }
    }
    
    private void filterbyDate(){
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        if(!searchBar.getText().equals(" ") && datePicker.getValue()==null){
            for (Attendance attendance : getAttendance()) { // Assuming 'table' is your TableView
                // Check if any of the columns contain the search keyword (case-insensitive).
                if ((attendance.getName().toLowerCase()).contains(keyword)){
                    filteredData.add(attendance);
                    adminTableView.setItems(filteredData);
                }
            }
        }else if(!searchBar.getText().equals(" ") && datePicker.getValue()!=null){
            for (Attendance attendance : getAttendance()) { // Assuming 'table' is your TableView
                // Check if any of the columns contain the search keyword (case-insensitive).
                if ((attendance.getDate().toString().equals(datePicker.getValue().toString())) &&
                        ((attendance.getName().toLowerCase()).contains(keyword))){
                    filteredData.add(attendance);
                adminTableView.setItems(filteredData);
                }
            }
        }
    }
}
