package Controller;

import Model.Special_Calendar;
import Utilities.DateUtil;
import Utilities.Filter;
import Utilities.Modal;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import javafx.scene.input.MouseEvent;
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

public class ADMIN_UserCalendarCTRL implements Initializable{

    @FXML
    private TableColumn<Special_Calendar, String> calTypeCol,descCol,attachmentCol;
    @FXML
    private TableColumn<Special_Calendar, Date> endDCol, startDCol;
    @FXML
    private ChoiceBox<String> typeComBox;
    @FXML
    private TextField descField, attachmentField;
    @FXML
    private Button insertBtn, clearBtn, updateBtn, deactivateBtn;
    @FXML
    private TableView<Special_Calendar> specialCalTable;
    @FXML
    private DateRangePicker dateRangePicker;

    @FXML
    private void handleSelectBtn(MouseEvent event) {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            //DEPRECATED
//            // Assuming startDate is of type java.sql.Date
//            java.sql.Date startDate = (java.sql.Date) selectedItem.getStartDate();
//            java.util.Date utilStartDate = new java.util.Date(startDate.getTime()); // Convert to java.util.Date
//            Instant instantStart = utilStartDate.toInstant();
//            LocalDate sDate = instantStart.atZone(ZoneId.systemDefault()).toLocalDate();
//
//            // Similarly, for endDate
//            java.sql.Date endDate = (java.sql.Date) selectedItem.getEndDate();
//            java.util.Date utilEndDate = new java.util.Date(endDate.getTime()); // Convert to java.util.Date
//            Instant instantEnd = utilEndDate.toInstant();
//            LocalDate eDate = instantEnd.atZone(ZoneId.systemDefault()).toLocalDate();
//
//            startPicker.setValue(sDate);
//            endPicker.setValue(eDate);

            //SET VALUES FOR DATE RANGE PICKER
            LocalDate startDate = DateUtil.sqlDateToLocalDate(selectedItem.getStartDate());
            LocalDate endDate = DateUtil.sqlDateToLocalDate(selectedItem.getEndDate());
            DateRange dateRange = new DateRange(startDate, endDate);
            dateRangePicker.setValue(dateRange);

            typeComBox.setValue(selectedItem.getType());
            descField.setText(selectedItem.getDescription());
            attachmentField.setText(selectedItem.getAttachment());;

            //insertBtn.setDisable(true);
            updateBtn.setDisable(false);
            deactivateBtn.setDisable(false);
        } else {
            typeComBox.setValue("");
            descField.setText("");
            descField.setPromptText("Add description . . .");
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
            //insertBtn.setDisable(false);
            updateBtn.setDisable(true);
    }
    
    @FXML
    private void updateSpecialCalendar(ActionEvent event) throws SQLException  {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();
        int selectedId =0;
        if (selectedItem != null) {
            selectedId = selectedItem.getId();
        }

        String type = typeComBox.getValue();
        String desription = descField.getText();
        String attachment = attachmentField.getText();

        Date startDate = Date.valueOf(dateRangePicker.getValue().getStartDate());
        Date endDate = Date.valueOf(dateRangePicker.getValue().getEndDate());


        boolean actionIsConfirmed = Modal.actionConfirmed("Update", "Do you want to proceed?", "This will update also to all employee records");
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
        String description = descField.getText();
        String attachment = attachmentField.getText();

        Date startDate = Date.valueOf(dateRangePicker.getValue().getStartDate());
        Date endDate = Date.valueOf(dateRangePicker.getValue().getEndDate());



        //check first if type is not null
        if(type == null){
            Modal.showModal("Failed", "Please select type");
            return;
        }

        //check if description is not empty
        if(description.isEmpty()){
            Modal.showModal("Failed", "Please add description");
            return;
        }

        //check if description already exists
        if(Special_Calendar.specialCalendarDescriptionExists(description)){
            Modal.showModal("Failed", "Special Calendar already exists \n   Please change description");
            return;
        }


        String specialCalendarName = "";
        boolean isOverlapping = false;

        //checks if date is overlapping with active special calendar
        ObservableList<Special_Calendar> specialCalendarList = Special_Calendar.getSpecialCalendar();
        for(Special_Calendar specialCalendar : specialCalendarList){
            if(Filter.DATE.isOverlapping(startDate+"", endDate+"", specialCalendar.getStartDate()+"", specialCalendar.getEndDate()+"")){
                specialCalendarName = specialCalendar.getDescription();
                isOverlapping = true;
            }
        }

        if(isOverlapping){
//            if(Modal.actionConfirmed("Confirm Action","Date Overlaps with: '" + specialCalendarName+"'", "Date overlaps with existing special calendar, do you want to proceed?")){
//                try{
//                    Special_Calendar.addSpecialCalendar(type, description, attachment, startDate, endDate);
//                } catch(SQLException ex){
//                    Modal.showModal("Failed", "Database Error");
//                }
//            }

            Modal.showModal("Failed", "Date Overlaps with: '" + specialCalendarName+"'");
        }else{
            //add confirmation modal first
            if(Modal.actionConfirmed("Confirm Action","Do you want to proceed?", "This will add also to all employee records")){
                try{
                    Special_Calendar.addSpecialCalendar(type, description, attachment, startDate, endDate);
                    setTable();
                    clear();
                } catch(SQLException ex){
                    Modal.showModal("Failed", "Database Error");
                }
            }
        }


    }
    @FXML
    private void deactivateSpecialCalendar(ActionEvent event) {
        Special_Calendar selectedItem = specialCalTable.getSelectionModel().getSelectedItem();
        int selectedId =0;
        if (selectedItem != null) {
            selectedId = selectedItem.getId();
        }
        
        boolean actionIsConfirmed = Modal.actionConfirmed("Deactivate", "Do you want to proeed?", "This will deactivate Holiday to all employees  record");
        if(actionIsConfirmed){
            Special_Calendar.deactivateSpecialCalendar(selectedId);
            setTable();
            clear();
        }
    }



}
