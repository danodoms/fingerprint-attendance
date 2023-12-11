/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Department;
import Model.Position;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<Position, Integer>("id"));
        col_position.setCellValueFactory(new PropertyValueFactory<Position, String>("position"));
        col_description.setCellValueFactory(new PropertyValueFactory<Position, String>("description"));
        
        loadPositionTable();
        
        departmentChoiceBox.setOnMouseClicked(event -> {
            // Get the corresponding department from the departmentChoiceBox
            int selectedDepartmentId = departmentChoiceBox.getValue().getId();
            departmentChoiceBox.setItems(Department.getDepartments());
        });
    }

    private void loadPositionTable(){
        ObservableList<Position> positions = Position.getPositions();
        positionTable.setItems(positions);
    }    

    @FXML
    private void positionSelected(MouseEvent event) {
        selectedPosition = positionTable.getSelectionModel().getSelectedItem();
        
        positionTitleField.setText(selectedPosition.getPosition());
        departmentChoiceBox.setValue(Department.getDepartmentById(selectedPosition.getDepartmentId()));
        positionDescTextArea.setText(selectedPosition.getDescription());
    }
    
}
