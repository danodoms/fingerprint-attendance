/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getAttendancebyLate;
import static Model.Attendance.getEmpName;
import static Model.Attendance.getYearforLabel;
import Model.User;
import java.time.YearMonth;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author User
 */
public class ADMIN_AttReportsCTRL implements Initializable{
    @FXML
    private TableView<Attendance> empNameTable, tardinessTable;
    @FXML
    private TableColumn<Attendance, String> empName, tiAM, toAM, tiPM, toPM, remCol, user_id;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private Label monthYearLabel, nameLabel, dateTimeLabel, totalLogin, totalAbsent, byPercent, totalLate;
    @FXML
    private ChoiceBox <String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetBtn, selectBtn;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    Month currentMonth = currentDate.getMonth();
    int currentDay = currentDate.getDayOfMonth();
    String capitalizedMonth = currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1).toLowerCase();
    
    @Override
    public void initialize(URL location, ResourceBundle rb) {
         try {
        setTable();
        //Tardiness Table
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tiAM.setCellValueFactory(new PropertyValueFactory<>("timeInAm"));
        toAM.setCellValueFactory(new PropertyValueFactory<>("timeOutAm"));
        tiPM.setCellValueFactory(new PropertyValueFactory<>("timeInPm"));
        toPM.setCellValueFactory(new PropertyValueFactory<>("timeOutPm"));
        remCol.setCellValueFactory(new PropertyValueFactory<>("attendance_status"));
        //Selection Name table
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        empName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        dateTimeLabel.setText(String.valueOf(currentMonth)+" "+currentDay+", "+ currentYear);
        ObservableList<String> monthList = FXCollections.observableArrayList();
        monthList.addAll("January", "February", "March", 
                "April", "May", "June", "July", 
                "August", "September", "October", 
                "November", "December");
        monthChoiceBox.setItems(monthList);
        monthChoiceBox.setValue(capitalizedMonth);
        monthChoiceBox.setOnAction(event -> selectMonth());
        yearChoiceBox.setOnAction(event -> selectYear());
        
        ObservableList<String> monthYear = FXCollections.observableArrayList();
                monthYear.addAll("2021","2022");
                for (Attendance attendance : getYearforLabel()){
                        String[] splitItems = attendance.getDate().toString().split("-");
                        monthYear.add(splitItems[0]);
                }
                yearChoiceBox.setItems(monthYear);
                yearChoiceBox.setValue(currentYear+"");
                
          } catch (Exception e) {
        e.printStackTrace();
    }     
    }
    public void selectYear(){
        String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        monthYearLabel.setText(selectedMonth+ " , "+selectedYear);
    }
    public void selectMonth(){
        String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        monthYearLabel.setText(selectedMonth+ " , "+selectedYear);
    }
    
    public void setTable(){
        
        empNameTable.setItems(Attendance.getEmpName());
        monthChoiceBox.setValue(capitalizedMonth);
        yearChoiceBox.setValue(currentYear+"");
        searchBar.setText("");
        nameLabel.setText("");
        totalLogin.setText("");
        byPercent.setText("0%");
        totalLate.setText("");
        totalAbsent.setText("");
        searchBar.setPromptText("Search name...");
        tardinessTable.setItems(null);
        monthChoiceBox.setOnAction(event -> selectMonth());
        yearChoiceBox.setOnAction(event -> selectYear());
    }
    @FXML
    private void filterBySearchBar(KeyEvent event) {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        
            for (Attendance attendance : getEmpName()) {   
                    if ((attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
        empNameTable.setItems(filteredData);
    }
    @FXML
    private void selectEMployee(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showAttTable(selectedItem.getId());
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        }else{
            totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalAbsent.setText("");
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
            tardinessTable.setItems(null);
        }
    }
    
     public void showAttTable(int user_id){
         totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalAbsent.setText("");
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
                List<String> dayHolder = new LinkedList<>();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        String[] dateOnCTRL = monthYearLabel.getText().split(" ");
        String selectedName = nameLabel.getText();
        String monthToNum = "",converted ="";
        double tardiness;
        int daysInMonth=0, lateCount = 0, absentCount = 0;
                if(dateOnCTRL[0].equals("January")){monthToNum="1";}if(dateOnCTRL[0].equals("February")){monthToNum="2";}
                if(dateOnCTRL[0].equals("March")){monthToNum="3";}if(dateOnCTRL[0].equals("April")){monthToNum="4";}
                if(dateOnCTRL[0].equals("May")){monthToNum="5";}if(dateOnCTRL[0].equals("June")){monthToNum="6";}
                if(dateOnCTRL[0].equals("July")){monthToNum="7";}if(dateOnCTRL[0].equals("August")){monthToNum="8";}
                if(dateOnCTRL[0].equals("September")){monthToNum="9";}if(dateOnCTRL[0].equals("October")){monthToNum="10";}
                if(dateOnCTRL[0].equals("November")){monthToNum="11";}if(dateOnCTRL[0].equals("December")){monthToNum="12";}
                 
        int year= Integer.parseInt(dateOnCTRL[2]);
                int month = Integer.parseInt(monthToNum);
                YearMonth yearMonth = YearMonth.of(year, month);
                daysInMonth = yearMonth.lengthOfMonth();
                
                for (Attendance attendance : getAttendancebyLate(user_id)){
                    System.out.println(attendance.getDate()+", "+attendance.getTimeInAm()+", "+attendance.getTimeOutAm()+", "+attendance.getTimeInPm()+", "+attendance.getTimeOutPm());
                    String[] dateOnModel = attendance.getDate().toString().split("-");
                    if(dateOnModel[1].equals(monthToNum)){
                        dayHolder.add(dateOnModel[2]);
                    }
                }
                System.out.println(dayHolder);
                
                for(int i =1; i<=daysInMonth; i++){
                    LocalDate localDate = LocalDate.of(year, month, i);
                    boolean dayChecker = false;
                    for(int j =0; j<dayHolder.size(); j++){
                        int amDay = Integer.parseInt(dayHolder.get(j));
                            if(i==amDay){
                            dayChecker=true;
                        }
                    }
                    if(dayChecker==true){
                        for (Attendance attendance : getAttendancebyLate(user_id)){
                             
                            if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                    && attendance.getAttendance_status().equals("Late")){
                                filteredData.add(attendance);
                                 lateCount++;
                                break;
                            }
//                            if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
//                                    && attendance.getAttendance_status().equals("No Out")
//                                    && attendance.getNotation().equals("AM")){
//                                filteredData.add(attendance);
//                                lateCount++;
//                                break;
//                            }
                        }
                    }
                    if(dayChecker==false){
                        for (Attendance attendance : getAttendancebyLate(user_id)){
                            attendance.setDate(java.sql.Date.valueOf(localDate));
                            attendance.setTimeInAm("    --");
                            attendance.setTimeOutAm("    --");
                            attendance.setTimeInPm("    --");
                            attendance.setTimeOutPm("    --");
                            attendance.setAttendance_status("Absent");
                            filteredData.add(attendance);
                            absentCount++;
                            break;
                        }
                    }
                }
                
            if(lateCount!=0 || absentCount!=0){ 
                tardiness = ((double) (lateCount + absentCount) / (double) (daysInMonth)) * 100;
                converted = String.format("%.1f", tardiness);
                totalLogin.setText(daysInMonth+"");
                totalLate.setText(lateCount+"");
                totalAbsent.setText(absentCount+"");
            }else{converted = "0";}
                tardinessTable.setItems(filteredData);
                byPercent.setText(converted+"%");
                
     }   
    
//   
    
    @FXML
    private void resetSelectedItems(ActionEvent event){
        setTable();
    }
    
//    @FXML
//     public void showOnLabels() {
//        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            nameLabel.setText(selectedItem.getName());
//        }
//    }
}

