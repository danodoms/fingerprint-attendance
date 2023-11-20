/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import Model.*;
import static Model.Attendance.getAdministrative;
import static Model.Attendance.getAttendance;
import static Model.Attendance.getInstruction;
import java.net.URL;
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
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetBtn;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();
        departmentChoiceBox.setValue(new Department("All"));
        departmentChoiceBox.getItems().addAll(new Department("All"));
        departmentChoiceBox.getItems().addAll(Department.getDepartments());
        departmentChoiceBox.setOnAction(event -> filterByDept());
    }

    @FXML
    public void clearChoiceBox(ActionEvent event){
        setTable();
    }
//    @FXML
    public void setTable(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tiCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        timeNotation.setCellValueFactory(new PropertyValueFactory<>("notation"));
        status.setCellValueFactory(new PropertyValueFactory<>("attendance_status"));
        adminTableView.setItems(Attendance.getAttendance());
        searchBar.setText("");
        searchBar.setPromptText("Search name...");
        departmentChoiceBox.setValue(new Department("All"));
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
    private void filterBySearchBar(KeyEvent event) {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        LocalDate selectedDate = datePicker.getValue();
        
            if(selectedDate==null && departmentChoiceBox.getValue().toString().equals("All")){
                for (Attendance attendance : getAttendance()) {   
                    if ((attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }
            else if(selectedDate!=null &&departmentChoiceBox.getValue().toString().equals("All")){
                for (Attendance attendance : getAttendance()) {
                    if (attendance.getDate().toString().equals(selectedDate.toString()) && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }else if(selectedDate!=null && departmentChoiceBox.getValue().toString().equals("Administrative")){
                for (Attendance attendance : getAdministrative()) {  
                    if (attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }else if(departmentChoiceBox.getValue().toString().equals(attendance.getDeptName())
                            && attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }else if(selectedDate!=null && departmentChoiceBox.getValue().toString().equals("Instruction")){
                for (Attendance attendance : getInstruction()) {
                    if (attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                        
                    }
                 }
            }
        adminTableView.setItems(filteredData);
    }  
    
    @FXML
    private void filterByDate(ActionEvent event) {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        LocalDate selectedDate = datePicker.getValue();
        
            if(selectedDate==null && departmentChoiceBox.getValue().toString().equals("All")){
                for (Attendance attendance : getAttendance()) {   
                    if ((attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }
            else if(selectedDate!=null &&departmentChoiceBox.getValue().toString().equals("All")){
                for (Attendance attendance : getAttendance()) {
                    if (attendance.getDate().toString().equals(selectedDate.toString()) && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }else if(selectedDate!=null && departmentChoiceBox.getValue().toString().equals("Administrative")){
                for (Attendance attendance : getAdministrative()) {  
                    if (attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }else if(departmentChoiceBox.getValue().toString().equals(attendance.getDeptName())
                            && attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
            }else if(selectedDate!=null && departmentChoiceBox.getValue().toString().equals("Instruction")){
                for (Attendance attendance : getInstruction()) {
                    if (attendance.getDate().toString().equals(selectedDate.toString()) 
                            && (attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                 }
            }
        adminTableView.setItems(filteredData);
    }
     
    private void filterByDept() {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        LocalDate selectedDate = datePicker.getValue();
        
            if(departmentChoiceBox.getValue().toString().equals("All")){
                if(selectedDate==null && keyword.isEmpty()){
                    for (Attendance attendance : getAttendance()) {   
                        filteredData.add(attendance);
                    }
                }else if(selectedDate!=null && !keyword.isEmpty()){
                    for (Attendance attendance : getAttendance()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString()) && (attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }   
                }else if(selectedDate!=null && keyword.isEmpty()){
                    for (Attendance attendance : getAttendance()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString())){
                            filteredData.add(attendance);
                        }
                    }
                }else if(selectedDate==null && !keyword.isEmpty()){
                    for (Attendance attendance : getAttendance()) {
                        if ((attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }
                }
            }else if(departmentChoiceBox.getValue().toString().equals("Administrative")){
                if(selectedDate==null && keyword.isEmpty()){
                    for (Attendance attendance : getAdministrative()) {   
                        filteredData.add(attendance);
                    }
                }else if(selectedDate!=null && !keyword.isEmpty()){
                    for (Attendance attendance : getAdministrative()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString()) && (attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }   
                }else if(selectedDate!=null && keyword.isEmpty()){
                    for (Attendance attendance : getAdministrative()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString())){
                            filteredData.add(attendance);
                        }
                    }
                }else if(selectedDate==null && !keyword.isEmpty()){
                    for (Attendance attendance : getAdministrative()) {
                        if ((attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }
                }
            }else if(departmentChoiceBox.getValue().toString().equals("Instruction")){
                if(selectedDate==null && keyword.isEmpty()){
                    for (Attendance attendance : getInstruction()) {   
                        filteredData.add(attendance);
                    }
                }else if(selectedDate!=null && !keyword.isEmpty()){
                    for (Attendance attendance : getInstruction()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString()) && (attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }   
                }else if(selectedDate!=null && keyword.isEmpty()){
                    for (Attendance attendance : getInstruction()) {
                        if (attendance.getDate().toString().equals(selectedDate.toString())){
                            filteredData.add(attendance);
                        }
                    }
                }else if(selectedDate==null && !keyword.isEmpty()){
                    for (Attendance attendance : getInstruction()) {
                        if ((attendance.getName().toLowerCase()).contains(keyword)){
                            filteredData.add(attendance);
                        }
                    }
                }
            }
        adminTableView.setItems(filteredData);
    }
}
