/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getAttendancebyLate;
import static Model.Attendance.getEmpName;
import static Model.Attendance.getYearforLabel;
import java.time.YearMonth;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
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

/**
 *
 * @author User
 */
public class ADMIN_AttReportsCTRL implements Initializable{
    @FXML
    private TableView<Attendance> empNameTable, tardinessTable;
    @FXML
    private TableColumn<Attendance, String> empName, notCol, arCol;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private Label monthYearLabel, nameLabel, dateTimeLabel, totalLogin, totalTardiness, byPercent, totalLate;
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
        arCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        notCol.setCellValueFactory(new PropertyValueFactory<>("notation"));
        //Selection Name table
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
        searchBar.setPromptText("Search name...");
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
    private void setTardinessTable(ActionEvent event) {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        String[] dateOnCTRL = monthYearLabel.getText().split(" ");
        String selectedName = nameLabel.getText();
        String monthToNum = "",converted ="";
        int count=0, daysInMonth=0;
        double tardiness;
            for (Attendance attendance : getAttendancebyLate()){
                String[] dateOnModel = attendance.getDate().toString().split("-");
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
                System.out.println("Number of days in " + yearMonth + ": " + daysInMonth);
                     
                for(int i =1; i<=daysInMonth; i++){
                    if (attendance.getName().equals(selectedName) && dateOnModel[0].equals(dateOnCTRL[2])
                        && dateOnModel[1].equals(monthToNum) && dateOnModel[2].equals(i+"")){
                         count++;
                        filteredData.add(attendance);
                        i=32;
                    }
                } 
            }
            daysInMonth = (daysInMonth*2);
            if(count!=0){ 
                tardiness = ((double) count / (double) (daysInMonth)) * 100;
                converted = String.format("%.1f", tardiness);
            }else{converted = "0";}
            if(!nameLabel.getText().isEmpty()){totalLogin.setText(daysInMonth+"");totalLate.setText(count+"");}
        tardinessTable.setItems(filteredData);
        byPercent.setText(converted+"%");
    } 
    
    @FXML
    private void resetSelectedItems(ActionEvent event){
        setTable();
    }
    
    @FXML
     public void showOnLabels() {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
        }
    }
}

