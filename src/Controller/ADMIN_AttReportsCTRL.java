/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getEmpName;
import static Model.Attendance.getYearforLabel;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
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
    private TableView<Attendance> empNameTable;
    @FXML
    private TableColumn<Attendance, String> empName;
    @FXML
    private Label monthYearLabel, nameLabel, dateTimeLabel;
    @FXML
    private ChoiceBox <String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetBtn;
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    Month currentMonth = currentDate.getMonth();
    String capitalizedMonth = currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1).toLowerCase();
    
    @Override
    public void initialize(URL location, ResourceBundle rb) {
         try {
        setTable();
        dateTimeLabel.setText(String.valueOf(currentDate));
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
        monthYearLabel.setText(selectedMonth+ ", "+selectedYear);
    }
    public void selectMonth(){
        String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        monthYearLabel.setText(selectedMonth+ ", "+selectedYear);
    }
    
    public void setTable(){
        empNameTable.setItems(Attendance.getEmpName());
        monthChoiceBox.setValue(capitalizedMonth);
        yearChoiceBox.setValue(currentYear+"");
        searchBar.setText("");
        monthYearLabel.setText("");
        nameLabel.setText("");
        searchBar.setPromptText("Search name...");
        empName.setCellValueFactory(new PropertyValueFactory<>("name"));
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

