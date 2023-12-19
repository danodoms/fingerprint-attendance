package Controller;

import Model.Attendance;
import Model.Special_Calendar;
import static Model.Attendance.getEmpName;
import static Model.Special_Calendar.getCalendarByUserId;
import Model.User;
import Utilities.Modal;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
    private TableColumn<Special_Calendar, String> calTypeCol,descCol,attachmentCol;
    @FXML
    private DatePicker endPicker, startPicker;
    @FXML
    private TableColumn<Special_Calendar, Date> endDCol, startDCol;
    @FXML
    private ChoiceBox<String> typeComBox;
    @FXML
    private TextField searchBar, descField, attachmentField;
    @FXML
    private Button selectBtn, insertBtn, clearBtn, updateBtn, deactivateBtn;
    @FXML
    private TableView<Special_Calendar> specialCalTable;


    @FXML
    private Line topLine;

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
            attachmentField.setText(selectedItem.getAttachment());;
            startPicker.setValue(sDate);
            endPicker.setValue(eDate);
            insertBtn.setDisable(true);
            updateBtn.setDisable(false);
            deactivateBtn.setDisable(false);
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
        Platform.runLater(() -> setTable());
//        ObservableList<String> monthList = FXCollections.observableArrayList();
//        monthList.addAll("January", "February", "March", 
//                "April", "May", "June", "July", 
//                "August", "September", "October", 
//                "November", "December");
//        monthChoiceBox.setItems(monthList);
        
        ObservableList<String> calendarType = FXCollections.observableArrayList();
        calendarType.addAll("Holiday");
        typeComBox.setItems(calendarType);
        typeComBox.setValue("Holiday");
    }
    public void setTable(){
        calTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        attachmentCol.setCellValueFactory(new PropertyValueFactory<>("attachment"));
        startDCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        updateBtn.setDisable(true);
        deactivateBtn.setDisable(true);
        specialCalTable.setItems(Special_Calendar.getSpecialCalendar());
    }
    
    public void clearManage(ActionEvent event){
            clear();
    }
    public void clear(){
            typeComBox.setValue("Holiday");
            descField.setText("");
            attachmentField.setText("");
            descField.setPromptText("Add description . . .");
            attachmentField.setPromptText("Add Link . . .");
            startPicker.setValue(null);
            endPicker.setValue(null);
            insertBtn.setDisable(false);
            updateBtn.setDisable(true);
    }
    
    @FXML
    private void updateSpecialCalendar(ActionEvent event) throws SQLException  {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();
        int selectedId =0;
        if (selectedItem != null) {
            selectedId = selectedItem.getId();
        }
        LocalDate localStartDate = startPicker.getValue();
        LocalDate localEndDate = endPicker.getValue();
        String type = typeComBox.getValue();
        String desription = descField.getText();
        String attachment = attachmentField.getText();
        Date startDate = Date.valueOf(localStartDate);
        Date endDate = Date.valueOf(localEndDate);

        boolean actionIsConfirmed = Modal.showConfirmationModal("Update", "Do you want to proceed?", "This will update also to all employee records");
        if (actionIsConfirmed) {
            Special_Calendar.updateSpecialCalendar(selectedId, type, desription, attachment, startDate, endDate);
            setTable();
                clear();
        }
}
    @FXML
    private void addSpecialCalendar(ActionEvent event) {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();
        String type = typeComBox.getValue();
        LocalDate localStartDate = startPicker.getValue();
        LocalDate localEndDate = endPicker.getValue();
        Date startDate = Date.valueOf(localStartDate);
        Date endDate = Date.valueOf(localEndDate);
        String description = descField.getText();
        String attachment = attachmentField.getText();
        
        try{
            Special_Calendar.addSpecialCalendar(type, description, attachment, startDate, endDate);
        } catch(SQLException ex){
            Modal.showModal("Failed", "Database Error");
        }
        Modal.showModal("Success", "Special Calendar Added");
            setTable();
            clear();
    }
    @FXML
    private void deactivateSpecialCalendar(ActionEvent event) {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();
        int selectedId =0;
        if (selectedItem != null) {
            selectedId = selectedItem.getId();
        }
        
        boolean actionIsConfirmed = Modal.showConfirmationModal("Deactivate", "Do you want to proeed?", "This will deactivate Holiday to all employees  record");
        if(actionIsConfirmed){
            Special_Calendar.deactivateSpecialCalendar(selectedId);
            setTable();
            clear();
        }
    }
    

}
