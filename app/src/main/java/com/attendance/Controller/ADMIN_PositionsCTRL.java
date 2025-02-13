/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Department;
import Model.Position;
import Utilities.Modal;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_PositionsCTRL implements Initializable {

    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private TableColumn<Position, Integer> col_id;
    @FXML
    private TableColumn<Position, String> col_position;
    @FXML
    private TableColumn<Position, String> col_description;
    @FXML
    private ChoiceBox<Department> departmentFilterChoiceBox;
    @FXML
    private ChoiceBox<String> statusFilterChoiceBox;
    @FXML
    private TextField positionTitleField;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private TextArea positionDescTextArea;
    @FXML
    private TableView<Position> positionTable;
    
    Position selectedPosition = null;
    @FXML
    private TextField searchField;
//    int positionId = selectedPosition.getId();
//    String position = selectedPosition.getPosition();
//    String positionDescription = selectedPosition.getDescription();
//    int departmentId = selectedPosition.getDepartmentId();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<Position, Integer>("id"));
        col_position.setCellValueFactory(new PropertyValueFactory<Position, String>("position"));
        col_description.setCellValueFactory(new PropertyValueFactory<Position, String>("description"));

        //init departmentFilterChoiceBox and all option
        departmentFilterChoiceBox.getItems().add(new Department(0, "All"));
        departmentFilterChoiceBox.getItems().addAll(Department.getActiveDepartments());
        departmentFilterChoiceBox.setValue(new Department(0, "All"));

        //add lambda event trigger for deptchoicebox thatloads position table
        departmentFilterChoiceBox.setOnAction((event) -> {
            loadPositionTable();
        });

        //init status choicebox with active inactive option
        statusFilterChoiceBox.getItems().addAll("Active", "Inactive");
        statusFilterChoiceBox.setValue("Active");

        //add lambda event trigger for statuschoicebox that loads position table
        statusFilterChoiceBox.setOnAction((event) -> {
            loadPositionTable();
        });

        loadPositionTable();
        
        departmentChoiceBox.setOnMouseClicked(event -> {
            // Get the corresponding department from the departmentChoiceBox
            departmentChoiceBox.setItems(Department.getActiveDepartments());
        });

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadPositionTable();
        });

    }

    private void loadPositionTable(){
        ObservableList<Position> positions = Position.getPositions();

        //first, filter the positions by department, store in a new list
        ObservableList<Position> filteredPositions = positions.filtered(position -> {
            if(departmentFilterChoiceBox.getValue().getId() == 0){
                return true;
            }else{
                return position.getDepartmentId() == departmentFilterChoiceBox.getValue().getId();
            }
        });

        //filter the new list by status, store in a new list
        filteredPositions = filteredPositions.filtered(position -> {
            if(statusFilterChoiceBox.getValue().equals("Active")){
                return position.getStatus() == 1;
            }else if(statusFilterChoiceBox.getValue().equals("Inactive")){
                return position.getStatus() == 0;
            }else{
                return true;
            }
        });

        //then, filter based on searchFilterField, store in new list
        filteredPositions = filteredPositions.filtered(position -> {
            String searchFilter = searchField.getText().toLowerCase();
            if(searchFilter.isEmpty()){
                return true;
            }else{
                return position.getPosition().toLowerCase().contains(searchFilter);
            }
        });

        positionTable.setItems(filteredPositions);
    }    

    @FXML
    private void positionSelected(MouseEvent event) {
        selectedPosition = positionTable.getSelectionModel().getSelectedItem();
        
        positionTitleField.setText(selectedPosition.getPosition());
        departmentChoiceBox.setValue(Department.getDepartmentById(selectedPosition.getDepartmentId()));
        positionDescTextArea.setText(selectedPosition.getDescription());

        if(selectedPosition.getStatus() == 1){
            deactivateBtn.setText("Deactivate");
        }else{
            deactivateBtn.setText("Activate");
        }
    }

    @FXML
    public void addPosition(ActionEvent actionEvent) throws SQLException {
        //check if position title is empty and department choice box
        if(positionTitleField.getText().isEmpty() || departmentChoiceBox.getValue() == null){
            Modal.showModal("Add Position", "Please fill out all fields");
            return;
        }

        //check if position already exists
        if(Position.positionAlreadyExists(positionTitleField.getText(), departmentChoiceBox.getValue().getId())){
            Modal.showModal("Add Position", "Position already exists");
        }else{
            if(Modal.actionConfirmed("Add Position", "Add Position?", "This action will add the position")){
                Position.addPosition(positionTitleField.getText(), positionDescTextArea.getText(), departmentChoiceBox.getValue().getId());
                loadPositionTable();
                clearFields();
            }
        }
    }

    @FXML
    public void updatePosition(ActionEvent actionEvent) throws SQLException {
        boolean actionIsConfirmed = Modal.actionConfirmed("Update Position", "Are you sure you want to update this position?", "This action cannot be undone.");
        if (actionIsConfirmed) {
            Position.updatePosition(selectedPosition.getId(), positionTitleField.getText(), positionDescTextArea.getText(), departmentChoiceBox.getValue().getId());
            loadPositionTable();
            clearFields();
        }
    }

    @FXML
    public void invertPositionStatus(ActionEvent actionEvent) throws SQLException {
        String actionType = selectedPosition.getStatus() == 1 ? "Deactivate" : "Activate";
        String confirmationMessage = actionType + " this position?";
        String actionDescription = "This action will " + actionType.toLowerCase() + " the currently selected position";

        if (Modal.actionConfirmed(actionType, confirmationMessage, actionDescription)) {
            Position.invertPositionStatus(selectedPosition.getId());
            loadPositionTable();
            clearFields();
        }
    }

    public void clearFields(){
        positionTitleField.setText("");
        departmentChoiceBox.setValue(null);
        positionDescTextArea.setText("");
    }
}
