/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.*;
import Utilities.Filter;
import Utilities.ImageUtil;
import Utilities.Modal;
import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_AssignmentsCTRL implements Initializable {

    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<Assignment, String> col_department;
    @FXML
    private TableColumn<Assignment, String> col_position;
    @FXML
    private TableColumn<Assignment, String> col_shift;
    @FXML
    private TableColumn<Assignment, String> col_time;
    @FXML
    private TableColumn<Assignment, String> col_dateAssigned;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label userNameLabel;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private ChoiceBox<Position> positionChoiceBox;
    @FXML
    private ChoiceBox<Shift> shiftChoiceBox;
    @FXML
    private TableView<Assignment> assignmentTable;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deactivateBtn;
    @FXML
    private HBox buttonContainerHBox;
    @FXML
    private ChoiceBox userAssignCntFilterChoiceBox;
    @FXML
    private ChoiceBox assignmentStatusFilterChoiceBox;

    User selectedUser = null;
    Assignment selectedAssignment = null;
    @FXML
    private TextField searchField;
    @FXML
    private Label manageUserLabel;
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
        //USER TABLE
        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));


        //DEPARTMENT CHOICEBOX
        //MAKE DEPARTMENT CHOICEBOX SET THE POSITION CHOICEBOX VALUE TO NULL WHEN A NEW DEPARTMENT IS SELECTED
        departmentChoiceBox.setOnAction(event -> {
            positionChoiceBox.setValue(null);
        });


        //ASSIGNMENT TABLE
        col_department.setCellValueFactory(new PropertyValueFactory<Assignment, String>("department"));
        col_position.setCellValueFactory(new PropertyValueFactory<Assignment, String>("position"));
        col_shift.setCellValueFactory(new PropertyValueFactory<Assignment, String>("shift"));
        col_time.setCellValueFactory(new PropertyValueFactory<Assignment, String>("timeRange"));
        col_dateAssigned.setCellValueFactory(new PropertyValueFactory<Assignment, String>("dateAssigned"));



        //ASSIGNMENT CHOICEBOX
        departmentChoiceBox.getItems().addAll(Department.getActiveDepartments());
        shiftChoiceBox.getItems().addAll(Shift.getActiveShifts());

        //USER ASSIGNMENT COUNT FILTER CHOICE BOX
        userAssignCntFilterChoiceBox.getItems().addAll("All", "None", "1", "2", "More than 2");
        userAssignCntFilterChoiceBox.setValue("All");

        userAssignCntFilterChoiceBox.setOnAction((event) -> {
            loadUserTable();
        });


        //ASSIGNMENT STATUS FILTER CHOICE BOX
        assignmentStatusFilterChoiceBox.getItems().addAll("Active", "Inactive");
        assignmentStatusFilterChoiceBox.setValue("Active");

        assignmentStatusFilterChoiceBox.setOnAction((event) -> {
            loadAssignmentTable(selectedUser.getId());
        });

        //add event listener to searchFilterField that calls loadUserTable() when changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadUserTable();
        });


        positionChoiceBox.setOnMouseClicked(event -> {
            // Get the corresponding department from the departmentChoiceBox
            int selectedDepartmentId = departmentChoiceBox.getValue().getId();
            System.out.println("Selected Dept ID: "+selectedDepartmentId);
            positionChoiceBox.setItems(Position.getPositionsByDepartmentId(selectedDepartmentId));
        });




        shiftChoiceBox.setOnAction(event -> {
            //System.out.println(Arrays.toString(shiftChoiceBox.getValue().getStartTime().split(":")));

            String startTime = shiftChoiceBox.getValue().getStartTime();
            String endTime = shiftChoiceBox.getValue().getEndTime();


            if(!(startTime.isEmpty())){
                //DEPRECATED
//                String startTimeHour = shiftChoiceBox.getValue().getStartTime().split(":")[0];
//                String startTimeMinute = shiftChoiceBox.getValue().getStartTime().split(":")[1];
//
//                startTimeHourField.setText(startTimeHour);
//                startTimeMinuteField.setText(startTimeMinute);

                startTimePicker.setTime(LocalTime.parse(shiftChoiceBox.getValue().getStartTime()));
            }


            if(!(endTime.isEmpty())){
                //DEPRECATED
//                String endTimeHour = shiftChoiceBox.getValue().getEndTime().split(":")[0];
//                String endTimeMinute = shiftChoiceBox.getValue().getEndTime().split(":")[1];
//
//                endTimeHourField.setText(endTimeHour);
//                endTimeMinuteField.setText(endTimeMinute);

                endTimePicker.setTime(LocalTime.parse(shiftChoiceBox.getValue().getEndTime()));
            }
        });



        //make the end time and start time pickers uneditable if the selected shift is empty or it contains a value of "flexi" or "overload"

//        startTimePicker.setEditable(false);
//        endTimePicker.setEditable(false);
//
//        shiftChoiceBox.setOnAction(event -> {
//
//            String shiftName = shiftChoiceBox.getValue().getShiftName().toString().toLowerCase();
//
//
//            if(shiftChoiceBox.getValue() == null){
//                startTimePicker.setEditable(false);
//                endTimePicker.setEditable(false);
//            }else{
//                if(shiftName.contains("flexi") || shiftName.contains("overload")){
//                    startTimePicker.setEditable(false);
//                    endTimePicker.setEditable(false);
//                }else{
//                    startTimePicker.setEditable(true);
//                    endTimePicker.setEditable(true);
//                }
//            }
//        });

        userImageView.setVisible(false);
        userNameLabel.setVisible(false);
        manageUserLabel.setVisible(false);

        startTimePicker.setTime(null);
        endTimePicker.setTime(null);


        loadUserTable();


        //print if starttimepicker and endtimepicker is editable
        System.out.println("startTimePicker.isEditable(): " + startTimePicker.isEditable());
    }    
    
    public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();

        //filter the users list based on their assignment count and store them in a new list
        ObservableList<User> filteredUsers = users.filtered(user -> {
            int assignmentCount = Assignment.getActiveAssignmentCountByUserId(user.getId());
            String userAssignCntFilter = userAssignCntFilterChoiceBox.getValue().toString();

            if(userAssignCntFilter.equals("All")){
                return true;
            }else if(userAssignCntFilter.equals("None")){
                return assignmentCount == 0;
            }else if(userAssignCntFilter.equals("1")){
                return assignmentCount == 1;
            }else if(userAssignCntFilter.equals("2")){
                return assignmentCount == 2;
            }else{
                return assignmentCount > 2;
            }
        });

        //then, filter based on searchFilterField, store in new list
        filteredUsers = filteredUsers.filtered(user -> {
            String searchFilter = searchField.getText().toLowerCase();
            if(searchFilter.isEmpty()){
                return true;
            }else{
                return user.getFullName().toLowerCase().contains(searchFilter);
            }
        });

        userTable.setItems(filteredUsers);
    }
    
    public void loadAssignmentTable(int user_id){
        String statusFilter = (String) assignmentStatusFilterChoiceBox.getValue();
        ObservableList<Assignment> assignments = Assignment.getAssignmentsByUserId(user_id);

        //create a new observable list to store filtered assignments
        ObservableList<Assignment> filteredAssignments = assignments.filtered(assignment -> {
            if(statusFilter.equals("Active")){
                return assignment.getStatus() == 1;
            }else if(statusFilter.equals("Inactive")){
                return assignment.getStatus() == 0;
            }else{
                return true;
            }
        });



        assignmentTable.setItems(filteredAssignments);
    }    

    @FXML
    private void userSelected(MouseEvent event) {
//        showAddBtnOnly();
        selectedUser = userTable.getSelectionModel().getSelectedItem();
        loadAssignmentTable(selectedUser.getId());
        
        userNameLabel.setText(selectedUser.getFullName());
        userImageView.setImage(ImageUtil.byteArrayToImage(User.getUserImageByUserId(selectedUser.getId())));
        userImageView.setVisible(true);
        userNameLabel.setVisible(true);
        manageUserLabel.setVisible(true);
        
        clearFields();
        
    }

    @FXML
    private void assignmentSelected(MouseEvent event) {
//        showUpdateDeactivateBtnOnly();
        selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
        String department = selectedAssignment.getDepartment();
        int departmentId = selectedAssignment.getDepartmentId();
        String position = selectedAssignment.getPosition();
        int positionId = selectedAssignment.getPositionId();
        String shift = selectedAssignment.getShift();
        int shiftId = selectedAssignment.getShiftId();
        String timeRange = selectedAssignment.getTimeRange();
        
        String startTimeHour = "";
        String startTimeMinute = "";
        String endTimeHour = "";
        String endTimeMinute = "";
        
        String[] times = timeRange.split(" - ");

        if (times.length == 2) {
            // Parse start time components
            String startTime = times[0];
            String[] startComponents = startTime.split(":");
            startTimeHour = startComponents[0];
            startTimeMinute = startComponents[1];

            // Parse end time components
            String endTime = times[1];
            String[] endComponents = endTime.split(":");
            endTimeHour = endComponents[0];
            endTimeMinute = endComponents[1];
        } else {
            System.out.println("Invalid time range format");
        }

        //DEPRECATED
//        //TIME FIELDS
//        startTimeHourField.setText(startTimeHour);
//        startTimeMinuteField.setText(startTimeMinute);
//        endTimeHourField.setText(endTimeHour);
//        endTimeMinuteField.setText(endTimeMinute);

        //TIME PICKERS
        startTimePicker.setTime(selectedAssignment.getStartTime());
        endTimePicker.setTime(selectedAssignment.getEndTime());
        
        departmentChoiceBox.setValue(new Department(departmentId,department));
        positionChoiceBox.setValue(new Position(positionId, position));
        shiftChoiceBox.setValue(new Shift(shiftId, shift));

        if(selectedAssignment.getStatus() == 1){
            deactivateBtn.setText("Deactivate");
        }else{
            deactivateBtn.setText("Activate");
        }

        
    }
    
    public void showAddBtnOnly(){
        // Clear existing children in the HBox
        buttonContainerHBox.getChildren().clear();

        // Add the addBtn with Hgrow set to ALWAYS
        HBox.setHgrow(addBtn, javafx.scene.layout.Priority.ALWAYS);
        buttonContainerHBox.getChildren().add(addBtn);

        // Set visibility for other buttons
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        deactivateBtn.setVisible(false);
    }
    
    public void showUpdateDeactivateBtnOnly(){
        // Clear existing children in the HBox
        buttonContainerHBox.getChildren().clear();

        // Add the addBtn with Hgrow set to ALWAYS
        HBox.setHgrow(updateBtn, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(deactivateBtn, javafx.scene.layout.Priority.ALWAYS);
        buttonContainerHBox.getChildren().add(updateBtn);
        buttonContainerHBox.getChildren().add(deactivateBtn);

        // Set visibility for other buttons
        addBtn.setVisible(false);
        updateBtn.setVisible(true);
        deactivateBtn.setVisible(true);
    }
    
    public void clearFields(){
        departmentChoiceBox.setValue(null);
        positionChoiceBox.setValue(null);
        shiftChoiceBox.setValue(new Shift(""));

        //set timepicker to null
        startTimePicker.setTime(null);
        endTimePicker.setTime(null);

        //DEPRECATED
//        //TIME FIELDS
//        startTimeHourField.clear();
//        startTimeMinuteField.clear();
//        endTimeHourField.clear();
//        endTimeMinuteField.clear();
    }

    @FXML
    private void addAssignment(ActionEvent event) throws SQLException {
        //check first if values are null before storing them

        //check if a user is selected first
        if(selectedUser == null){
            Modal.showModal("Failed", "Please select a user first");
            return;
        }

        //check if no fields are empty, if there is then showModal and return
        if (departmentChoiceBox.getValue() == null || positionChoiceBox.getValue() == null || shiftChoiceBox.getValue() == null || startTimePicker.getTime() == null || endTimePicker.getTime() == null) {
            Modal.showModal("Failed", "Please fill out all fields");
            return;
        }

        //get the values from the fields
        int userId = selectedUser.getId();
        int positionId = positionChoiceBox.getValue().getId();
        int shiftId = shiftChoiceBox.getValue().getId();

        String startTime = startTimePicker.getTime().toString();
        String endTime = endTimePicker.getTime().toString();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateAssigned = currentDate.format(formatter);


        //check if position is already assigned to user but allow if the shift contains the word "overload"
        if (Assignment.positionAlreadyExists(userId, positionId) && !shiftChoiceBox.getValue().getShiftName().toLowerCase().contains("overload")) {
            Modal.showModal("Failed", "This position is already assigned to the selected user");
            return;
        }

        //for each active assignment in the assignment table, check if the time range overlaps with the new assignment
        boolean isOverlapping = false;

        ObservableList<Assignment> assignments = Assignment.getActiveAssignmentsByUserId(userId);
        for (Assignment assignment : assignments) {
            if (Filter.TIME.isOverlapping(assignment.getStartTime() + "", assignment.getEndTime() + "", startTime, endTime)) {
                isOverlapping = true;
                break;
            }
        }

        System.out.println("isOverlapping: " + isOverlapping);

        if (isOverlapping) {
            if (Modal.actionConfirmed("Overlapping Assignment", "Overlapping assignment, Continue?", "This will add an overlapping assignment")) {
                Assignment.addAssignment(userId, positionId, shiftId, startTime, endTime, dateAssigned);
            }
        } else {
            //prompt user to confirm action
            if (Modal.actionConfirmed("Add Assignment", "Do you want to proceed?", "This will add a new assignment")) {
                Assignment.addAssignment(userId, positionId, shiftId, startTime, endTime, dateAssigned);
            }
        }

        loadAssignmentTable(userId);
    }

    @FXML
    private void updateAssignment(ActionEvent event){
        int assignmentId = selectedAssignment.getId();
        int positionId = positionChoiceBox.getValue().getId();
        int shiftId = shiftChoiceBox.getValue().getId();

        //DEPRECATED
//        String startTime = startTimeHourField.getText() + ":" + startTimeMinuteField.getText();
//        String endTime = endTimeHourField.getText() + ":" + endTimeMinuteField.getText();

        String startTime = startTimePicker.getTime().toString();
        String endTime = endTimePicker.getTime().toString();
        
        
        boolean actionIsConfirmed = Modal.actionConfirmed("Update", "Do you want to proeed?", "This will update the selected assignment record");
        if(actionIsConfirmed){
            try {
                    Assignment.updateAssignment(assignmentId, positionId, shiftId, startTime, endTime);
                } catch (SQLException ex) {
                    Logger.getLogger(ADMIN_AssignmentsCTRL.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadAssignmentTable(selectedUser.getId());
        }      
    }

    @FXML
    private void invertAssignmentStatus(ActionEvent event) {
        String actionType = selectedAssignment.getStatus() == 1 ? "Deactivate" : "Activate";
        String confirmationMessage = actionType + " this assignment?";
        String actionDescription = "This action will " + actionType.toLowerCase() + " the currently selected assignment";

        if (Modal.actionConfirmed(actionType, confirmationMessage, actionDescription)) {
            Assignment.invertAssignmentStatus(selectedAssignment.getId());
            loadAssignmentTable(selectedUser.getId());
            clearFields();
        }
        
    }
    
    
    private String filterHour(String hour){
        String[] hourArray = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        
        for (String hourArr : hourArray) {
            if (hour.equals(hourArr)) {
                return hour;
            }
        }
        
        return "";
    }
    
    private String filterMinute(String minute) {
        // Define the range for minutes (00 to 59)
        int minMinute = 0;
        int maxMinute = 59;

        // Create an ArrayList to store the minute values
        ArrayList<String> minuteList = new ArrayList<>();

        // Populate the minuteList with values from minMinute to maxMinute
        for (int i = minMinute; i <= maxMinute; i++) {
            minuteList.add(String.format("%02d", i));
        }

        // Check if the provided minute is in the minuteList
        if (minuteList.contains(minute)) {
            return minute;
        } else {
            return "";
        }
    }

//DEPRECATED
//    private void filterTimeFields(KeyEvent event){
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//
//        executor.execute(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Platform.runLater(() -> {
//                startTimeHourField.setText(filterHour(startTimeHourField.getText()));
//                startTimeMinuteField.setText(filterMinute(startTimeMinuteField.getText()));
//                endTimeHourField.setText(filterHour(endTimeHourField.getText()));
//                endTimeMinuteField.setText(filterMinute(endTimeMinuteField.getText()));
//            });
//        });
//        executor.shutdown();
//
//
//    }

}
