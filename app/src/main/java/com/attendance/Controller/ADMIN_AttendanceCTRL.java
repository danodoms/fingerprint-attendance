/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Controller;

import Utilities.DatabaseUtil;
import Utilities.DateUtil;
import Utilities.PaneUtil;
import com.attendance.Model.*;
import static com.attendance.Model.Attendance.getAdministrative;
import static com.attendance.Model.Attendance.getAttendance;
import static com.attendance.Model.Attendance.getInstruction;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.ResourceBundle;

import com.dlsc.gemsfx.YearMonthPicker;
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
public class ADMIN_AttendanceCTRL implements Initializable {  
    @FXML
    private TableView<Attendance> adminTableView;
    @FXML
    private TableColumn<Attendance, String> status;
    @FXML
    private TableColumn<Attendance, String> nameCol,tiCol,toCol,timeNotation;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetBtn;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
    @FXML
    private YearMonthPicker yearMonthPicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadAttendanceTable();
        //init department choicebox
        departmentChoiceBox.setValue(new Department("All"));
        departmentChoiceBox.getItems().addAll(new Department("All"));
        departmentChoiceBox.getItems().addAll(Department.getActiveDepartments());

        departmentChoiceBox.setOnAction((event) -> {
            loadAttendanceTable();
        });


        yearMonthPicker.setOnAction((event) -> {
            loadAttendanceTable();
        });


        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tiCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        timeNotation.setCellValueFactory(new PropertyValueFactory<>("notation"));
        status.setCellValueFactory(new PropertyValueFactory<>("attendance_status"));


        //init search field
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            loadAttendanceTable();
        });




    }

    @FXML
    public void clearChoiceBox(ActionEvent event){
        resetFilters();
    }
//    @FXML
    public void resetFilters(){
        searchBar.setText("");
        departmentChoiceBox.setValue(new Department("All"));
        adminTableView.setItems(Attendance.getAttendance());
    }

    public void loadAttendanceTable(){
        ObservableList<Attendance> attendanceList = Attendance.getAttendance();


//        ObservableList<Attendance> filteredAttendance = attendanceList.filtered(attendance -> {
//            String departmentFilter = departmentChoiceBox.getValue().toString();
//            if(departmentFilter.equalsIgnoreCase("All")){
//                return true;
//            }else{
//                return attendance.getDeptName().equalsIgnoreCase(departmentFilter);
//            }
//        });

        //then filter by search bar
        ObservableList<Attendance> filteredAttendance = attendanceList.filtered(attendance -> {
            String searchBarFilter = searchBar.getText().toLowerCase();
            if(searchBarFilter.isEmpty()){
                return true;
            }else{
                return attendance.getName().toLowerCase().contains(searchBarFilter);
            }
        });

        //filter using yearmonthpicker
//        filteredAttendance = filteredAttendance.filtered(attendance -> {
//            LocalDate date = DateUtil.utilDateToLocalDate(attendance.getDate());
//            LocalDate yearMonthPickerDate = LocalDate.from(yearMonthPicker.getValue());
//            if(yearMonthPickerDate == null){
//                return true;
//            }else{
//                return date.getMonthValue() == yearMonthPickerDate.getMonthValue() && date.getYear() == yearMonthPickerDate.getYear();
//            }
//        });


        adminTableView.setItems(filteredAttendance);
    }

}
