/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Shift;
import Utilities.Modal;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
    private TextField startTimeHourField;
    @FXML
    private TextField startTimeMinuteField;
    @FXML
    private TextField endTimeHourField;
    @FXML
    private TextField endTimeMinuteField;
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

        shiftTable.setItems(filteredShifts);
    }

    //ADD SHIFT
    @FXML
    public void addShift(ActionEvent actionEvent) {
        String shiftName = shiftNameField.getText();
        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();

        Shift.addShift(shiftName, startTime, endTime);
        loadShiftTable();
        clearFields();
    }

    //UPDATE SHIFT
    @FXML
    public void updateShift(ActionEvent actionEvent) {
        int id = selectedShift.getId();
        String shiftName = shiftNameField.getText();
        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();

        boolean actionIsConfirmed = Modal.showConfirmationModal("Update Shift", "Are you sure you want to update this shift?", "This action cannot be undone.");

        if(actionIsConfirmed){
            Shift.updateShift(id, shiftName, startTime, endTime);
            loadShiftTable();
        }

    }

    @FXML
    public void deactivateShift(ActionEvent actionEvent) {
        int id = selectedShift.getId();

        boolean actionIsConfirmed = Modal.showConfirmationModal("Deactivate Shift", "Are you sure you want to deactivate this shift?", "This action cannot be undone.");

        if(actionIsConfirmed){
            Shift.deactivateShift(id);
            loadShiftTable();
            clearFields();
        }

    }


    @FXML
    public void shiftSelected(Event event) {
        selectedShift = (Shift) shiftTable.getSelectionModel().getSelectedItem();

        shiftNameField.setText(selectedShift.getShiftName());
        startTimeHourField.setText(selectedShift.getStartTime().split(":")[0]);
        startTimeMinuteField.setText(selectedShift.getStartTime().split(":")[1]);
        endTimeHourField.setText(selectedShift.getEndTime().split(":")[0]);
        endTimeMinuteField.setText(selectedShift.getEndTime().split(":")[1]);
    }

    //CLEAR FIELDS
    public void clearFields() {
        shiftNameField.setText("");
        startTimeHourField.setText("");
        startTimeMinuteField.setText("");
        endTimeHourField.setText("");
        endTimeMinuteField.setText("");
    }

}
