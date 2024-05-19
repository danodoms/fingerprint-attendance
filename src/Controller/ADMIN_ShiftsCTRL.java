/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Position;
import Model.Shift;
import Utilities.Modal;
import com.dlsc.gemsfx.TimePicker;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_ShiftsCTRL implements Initializable {

    @FXML
    private TableColumn<Shift, Integer> col_id;
    @FXML
    private TableColumn<Shift, String> col_shiftName;
    @FXML
    private TextField shiftNameField;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private ChoiceBox<String> statusFilterChoiceBox;
    @FXML
    private TableView shiftTable;
    @FXML
    private TableColumn col_startTime;
    @FXML
    private TableColumn col_endTime;

    Shift selectedShift = null;
    @FXML
    private TextField searchField;
    @FXML
    private TimePicker startTimePicker;
    @FXML
    private TimePicker endTimePicker;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //INIT SHIFT TABLE COLUMNS
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_shiftName.setCellValueFactory(new PropertyValueFactory<>("shiftName"));
        col_startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        col_endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        //INIT STATUS FILTER CHOICEBOX
        statusFilterChoiceBox.getItems().addAll("Active", "Inactive", "All");
        statusFilterChoiceBox.setValue("Active");

        //add lambda event listener to status filter choicebox that triggers loadShiftTable() when clicked
        statusFilterChoiceBox.setOnAction((event) -> {
            loadShiftTable();
        });

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadShiftTable();
        });

        startTimePicker.setTime(null);
        endTimePicker.setTime(null);

        loadShiftTable();
    }

    //LOAD SHIFT TABLE BASED ON FILTER
    private void loadShiftTable(){
        String statusFilter = statusFilterChoiceBox.getValue();
        ObservableList<Shift> shifts = Shift.getShifts();

        //create a new observable list to store filtered shifts
        ObservableList<Shift> filteredShifts = shifts.filtered(shift -> {
            if(statusFilter.equals("Active")){
                return shift.getStatus() == 1;
            }else if(statusFilter.equals("Inactive")){
                return shift.getStatus() == 0;
            }else{
                return true;
            }
        });

        //filter by searchField
        filteredShifts = filteredShifts.filtered(shift -> {
            String searchFilter = searchField.getText().toLowerCase();
            if(searchFilter.isEmpty()){
                return true;
            }else{
                return shift.getShiftName().toLowerCase().contains(searchFilter);
            }
        });

        shiftTable.setItems(filteredShifts);
    }

    //ADD SHIFT
    @FXML
    public void addShift(ActionEvent actionEvent) throws SQLException {
        String shiftName = shiftNameField.getText();
        String startTime = startTimePicker.getTime().toString();
        String endTime = endTimePicker.getTime().toString();

        //DEPRECATED
//        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
//        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();

        //check if shift name is empty
        if(shiftName.isEmpty()){
            Modal.showModal("Shift Name Required", "Please enter a shift name.");
            return;
        }

        //check if shift already exists
        if(Shift.shiftAlreadyExists(shiftName)){
            Modal.showModal("Shift Already Exists", "A shift with this name already exists.");
            return;
        }

        //add shift
        if(Modal.actionConfirmed("Add Shift", "Add Shift?", "This action will add the shift")){
            Shift.addShift(shiftName, startTime, endTime);
            loadShiftTable();
            clearFields();
        }



    }

    //UPDATE SHIFT
    @FXML
    public void updateShift(ActionEvent actionEvent) {
        int id = selectedShift.getId();
        String shiftName = shiftNameField.getText();
        String startTime = startTimePicker.getTime().toString();
        String endTime = endTimePicker.getTime().toString();

        //DEPRECATED
//        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
//        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();

        boolean actionIsConfirmed = Modal.actionConfirmed("Update Shift", "Are you sure you want to update this shift?", "This action cannot be undone.");

        if(actionIsConfirmed){
            Shift.updateShift(id, shiftName, startTime, endTime);
            loadShiftTable();
        }

    }

    @FXML
    public void invertShiftStatus(ActionEvent actionEvent) throws SQLException {
        String actionType = selectedShift.getStatus() == 1 ? "Deactivate" : "Activate";
        String confirmationMessage = actionType + " this position?";
        String actionDescription = "This action will " + actionType.toLowerCase() + " the currently selected position";

        if (Modal.actionConfirmed(actionType, confirmationMessage, actionDescription)) {
            Shift.invertShiftStatus(selectedShift.getId());
            loadShiftTable();
            clearFields();
        }

    }


    @FXML
    public void shiftSelected(Event event) {
        selectedShift = (Shift) shiftTable.getSelectionModel().getSelectedItem();
        shiftNameField.setText(selectedShift.getShiftName());

        //DEPRECATED
//        startTimeHourField.setText(selectedShift.getStartTime().split(":")[0]);
//        startTimeMinuteField.setText(selectedShift.getStartTime().split(":")[1]);
//        endTimeHourField.setText(selectedShift.getEndTime().split(":")[0]);
//        endTimeMinuteField.setText(selectedShift.getEndTime().split(":")[1]);

        //set values for timepickers
        startTimePicker.setTime(LocalTime.parse(selectedShift.getStartTime()));
        endTimePicker.setTime(LocalTime.parse(selectedShift.getEndTime()));

        if(selectedShift.getStatus() == 1){
            deactivateBtn.setText("Deactivate");
        }else{
            deactivateBtn.setText("Activate");
        }
    }

    //CLEAR FIELDS
    public void clearFields() {
        shiftNameField.setText("");
        startTimePicker.setValue(null);
        endTimePicker.setValue(null);
    }

}
