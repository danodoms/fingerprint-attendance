/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getEmpName;
import static Model.Attendance.getYearforLabel;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

/**
 *
 * @author User
 */
public class Admin_attReportsController implements Initializable {
    @FXML
    private AnchorPane openAttRepPane;
    @FXML
    private TableView<Attendance> adminTableView, empNameTable;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private TableColumn<Attendance, String> amCol, pmCol, tiAMCol, toAMCol, tiPMCol, toPMCol, remarksCol, empName;
    @FXML
    private TextField searchBar;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox monthChoiceBox, yearChoiceBox;
    @FXML
    private Button viewDTRBtn, resetBtn, generateDocBtn;
    @FXML
    private Label nameLabel, monthYearLabel;
    @FXML
    private static Pane fpIdentificationPane;
    
    dbMethods dbMethods = new dbMethods();
    controllerMethods method = new controllerMethods();
    
    @FXML
   private void openDTR(ActionEvent event) {
        method.openPane(method.ADMIN_ATTENDANCE_WORD);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();
        ObservableList<String> monthList = FXCollections.observableArrayList();
        monthList.addAll("January", "February", "March", 
                "April", "May", "June", "July", 
                "August", "September", "October", 
                "November", "December");
        monthChoiceBox.setItems(monthList);
        monthChoiceBox.setValue("Select Month");
        monthChoiceBox.setOnAction(event -> selectMonth());
        yearChoiceBox.setOnAction(event -> selectYear());
        
        ObservableList<String> monthYear = FXCollections.observableArrayList();
                for (Attendance attendance : getYearforLabel()){
                        String[] splitItems = attendance.getDate().toString().split("-");
                        monthYear.add(splitItems[0]);
                }
                System.out.println(monthYear);
                yearChoiceBox.setItems(monthYear);
                yearChoiceBox.setValue("Select Year");
    }
    public void selectYear(){
        String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
           if(!(selectedMonth.equals("Select Month"))){
               monthYearLabel.setText(selectedMonth+ ", "+selectedYear);
           }
    }
    public void selectMonth(){
        String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
           if(!(selectedYear.equals("Select Year"))){
               monthYearLabel.setText(selectedMonth+ ", "+selectedYear);
           }
    }
    
    public void setTable(){
        empNameTable.setItems(Attendance.getEmpName());
        monthChoiceBox.setValue("Select Month");
        yearChoiceBox.setValue("Select Year");
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
        // Get the selected item from the table.
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        // If an item is selected, populate the text fields with the item's data.
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
        }
    }
}

