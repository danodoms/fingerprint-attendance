package Controller;

import Model.Attendance;
import Model.Special_Calendar;
import static Model.Attendance.getEmpName;
import static Model.Special_Calendar.getCalendarByUserId;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class ADMIN_UserCalendarController implements Initializable{

    @FXML
    private TableColumn<Special_Calendar, String> calTypeCol,descCol;
    @FXML
    private TableColumn<Attendance, String> empName, user_id;

    @FXML
    private TableView<Attendance> empNameTable;
    @FXML
    private DatePicker endPicker, startPicker;
    @FXML
    private TableColumn<Special_Calendar, Date> endDCol, startDCol;
    @FXML
    private ChoiceBox<String> typeComBox, monthChoiceBox;
    @FXML
    private TextField searchBar, descField;
    @FXML
    private Button selectBtn, insertBtn, clearBtn;
    @FXML
    private TableView<Special_Calendar> specialCalTable;


    @FXML
    private Line topLine;

    @FXML
    private TableColumn<Special_Calendar, Integer> totalCol;


    @FXML
    void filterBySearchBar(KeyEvent event) {
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
    void selectEMployee(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showSpecialCalTable(selectedItem.getId());
    }
    public void showSpecialCalTable(int user_id){
        ObservableList<Special_Calendar> filteredData = FXCollections.observableArrayList();
        for (Special_Calendar calendar : getCalendarByUserId(user_id)){
            filteredData.add(calendar);
        }
        specialCalTable.setItems(filteredData);
    }
     @FXML
    private void handleSelectBtn(ActionEvent event) {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {

            // Assuming startDate is of type java.sql.Date
            java.sql.Date startDate = (java.sql.Date) selectedItem.getStartDate();
            java.util.Date utilStartDate = new java.util.Date(startDate.getTime()); // Convert to java.util.Date
            Instant instantStart = utilStartDate.toInstant();
            LocalDate sDate = instantStart.atZone(ZoneId.systemDefault()).toLocalDate();

            // Similarly, for endDate
            java.sql.Date endDate = (java.sql.Date) selectedItem.getEndDate();
            java.util.Date utilEndDate = new java.util.Date(endDate.getTime()); // Convert to java.util.Date
            Instant instantEnd = utilEndDate.toInstant();
            LocalDate eDate = instantEnd.atZone(ZoneId.systemDefault()).toLocalDate();

            typeComBox.setValue(selectedItem.getType());
            descField.setText(selectedItem.getDescription());
            startPicker.setValue(sDate);
            endPicker.setValue(eDate);
            insertBtn.setDisable(true);
        } else {
            typeComBox.setValue("");
            descField.setText("");
            descField.setPromptText("Add description . . .");
            startPicker.setValue(null);
            endPicker.setValue(null);
        }
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
      //Selection Name table
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        empName.setCellValueFactory(new PropertyValueFactory<>("name"));
      //Special Calendar Column assignments
        calTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    public void setTable(){
        empNameTable.setItems(Attendance.getEmpName());
    }
    public void clearManage(ActionEvent event){
            typeComBox.setValue("");
            descField.setText("");
            descField.setPromptText("Add description . . .");
            startPicker.setValue(null);
            endPicker.setValue(null);
            insertBtn.setDisable(false);
    }
    

}
