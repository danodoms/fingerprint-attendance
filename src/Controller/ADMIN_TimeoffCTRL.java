/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import Model.Timeoff;
import Model.User;
import Utilities.ImageUtil;
import Utilities.Modal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

import static Model.Attendance.getEmpName;
import static Model.Timeoff.getTimeoffByUserId;
/**
 *
 * @author User
 */
public class ADMIN_TimeoffCTRL implements Initializable{

    @FXML
    private TableColumn<Timeoff, String> calTypeCol,descCol, attachmentCol;
    @FXML
    private TableColumn<Attendance, String> empName, user_id;

    @FXML
    private TableView<Attendance> empNameTable;
    @FXML
    private DatePicker endPicker, startPicker;
    @FXML
    private TableColumn<Timeoff, Date> endDCol, startDCol;
    @FXML
    private ChoiceBox<String> typeComBox;
    @FXML
    private TextField searchBar, descField, attachmentField;
    @FXML
    private Button selectBtn, insertBtn, clearBtn, updateBtn,deactivateBtn;
    @FXML
    private TableView<Timeoff> specialCalTable;
    @FXML
    private Label empIdLabel, userOffIdLabel, timeOffIDLabel, userOffIdLabelTag;
    
    User selectedUser;
    Timeoff selectedUserTimeoff;

    @FXML
    private TableColumn<Timeoff, Integer> totalCol;
    @FXML
    private Label userNameLabel;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label manageUserLabel;


    @FXML
    public void filterBySearchBar(KeyEvent event) {
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
    public void selectEmployeeTimeOff(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showTimeoffTable(selectedItem.getId());
        selectedUser = User.getUserByUserId(selectedItem.getId());
        clear();


        empIdLabel.setText(selectedItem.getId()+"");
        deactivateBtn.setDisable(true);
        updateBtn.setDisable(true);
        userOffIdLabelTag.setDisable(true);
        typeComBox.setValue("On Leave");
//<<<<<<< HEAD
        searchBar.setText("");
        searchBar.setPromptText("Search name...");
//=======


        userNameLabel.setText(selectedUser.getFullNameWithInitial());
        userImageView.setImage(ImageUtil.byteArrayToImage(selectedUser.getImage()));

        manageUserLabel.setVisible(true);
        userImageView.setVisible(true);
        userNameLabel.setVisible(true);
//>>>>>>> dano
        
    }
    public void showTimeoffTable(int user_id){
        ObservableList<Timeoff> filteredData = FXCollections.observableArrayList();
        for (Timeoff timeoff : getTimeoffByUserId(user_id)){
            filteredData.add(timeoff);
        }
        specialCalTable.setItems(filteredData);
    }
     @FXML
    private void handleSelectBtn(ActionEvent event) {
        selectedUserTimeoff = specialCalTable.getSelectionModel().getSelectedItem();
//        userOffIdLabel.setText(selectedItem.getOffId()+"");
//        timeOffIDLabel.setText(selectedItem.getUserOffId()+"");
            updateBtn.setDisable(false);
            deactivateBtn.setDisable(false);
            userOffIdLabelTag.setDisable(false);

        if (selectedUserTimeoff != null) {

            // Assuming startDate is of type java.sql.Date
            java.sql.Date startDate = (java.sql.Date) selectedUserTimeoff.getStartDate();
            java.util.Date utilStartDate = new java.util.Date(startDate.getTime()); // Convert to java.util.Date
            Instant instantStart = utilStartDate.toInstant();
            LocalDate sDate = instantStart.atZone(ZoneId.systemDefault()).toLocalDate();

            // Similarly, for endDate
            java.sql.Date endDate = (java.sql.Date) selectedUserTimeoff.getEndDate();
            java.util.Date utilEndDate = new java.util.Date(endDate.getTime()); // Convert to java.util.Date
            Instant instantEnd = utilEndDate.toInstant();
            LocalDate eDate = instantEnd.atZone(ZoneId.systemDefault()).toLocalDate();

            typeComBox.setValue(selectedUserTimeoff.getType());
            descField.setText(selectedUserTimeoff.getDescription());
            attachmentField.setText(selectedUserTimeoff.getAttachment());
            startPicker.setValue(sDate);
            endPicker.setValue(eDate);
            insertBtn.setDisable(true);
            typeComBox.setDisable(true);
        } else {
            typeComBox.setValue("");
            descField.setText("");
            descField.setPromptText("Add description . . .");
            startPicker.setValue(null);
            endPicker.setValue(null);
        }
    }
    
    @FXML
private void updateTimeoff(ActionEvent event) throws SQLException {
    LocalDate localStartDate = startPicker.getValue();
    LocalDate localEndDate = endPicker.getValue();
    int userTimeoffId = Integer.parseInt(timeOffIDLabel.getText());
    String desription = descField.getText();
    String attachment = attachmentField.getText();
    int userId = Integer.parseInt(empIdLabel.getText());
    int offID = selectedUserTimeoff.getOffId();
    Date startDate = Date.valueOf(localStartDate);
    Date endDate = Date.valueOf(localEndDate);

    boolean actionIsConfirmed = Modal.actionConfirmed("Update", "Do you want to proceed?", "This will update the selected timeoff record");
    if (actionIsConfirmed) {
        Timeoff.updateTimeoff(userTimeoffId, userId, offID, desription, attachment, startDate, endDate);
        showTimeoffTable(userId);
            clear();
    }
}
@FXML
    private void addTimeoff(ActionEvent event) {
        LocalDate localStartDate = startPicker.getValue();
        LocalDate localEndDate = endPicker.getValue();
        Date startDate = Date.valueOf(localStartDate);
        Date endDate = Date.valueOf(localEndDate);
        String desription = descField.getText();
        String attachment = attachmentField.getText();
        int userId = Integer.parseInt(empIdLabel.getText());
        int offID = Integer.parseInt(userOffIdLabel.getText());
        
        try{
            Timeoff.addTimeoff( userId, offID, desription, attachment, startDate, endDate);
        } catch(SQLException ex){
            Modal.showModal("Failed", "Database Error");
        }
        
        Modal.showModal("Success", "Timeoff Added");
        showTimeoffTable(userId);
        clear();
    }
    
    @FXML
    private void deactivateTimeoff(ActionEvent event) {
        int userId = Integer.parseInt(empIdLabel.getText());
        int offID = Integer.parseInt(timeOffIDLabel.getText());
        
        boolean actionIsConfirmed = Modal.actionConfirmed("Deactivate", "Do you want to proeed?", "This will deactivate the selected assignment record");
        if(actionIsConfirmed){
            Timeoff.deactivateTimeoff(offID);
            showTimeoffTable(userId);
            clear();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userImageView.setVisible(false);
        userNameLabel.setVisible(false);
        manageUserLabel.setVisible(false);
        setTable();

//        ObservableList<String> monthList = FXCollections.observableArrayList();
//        monthList.addAll("January", "February", "March", 
//                "April", "May", "June", "July", 
//                "August", "September", "October", 
//                "November", "December");
//        monthChoiceBox.setItems(monthList);
        
        ObservableList<String> timeoffList = FXCollections.observableArrayList();
        timeoffList.addAll("On Leave", "On Travel", "No Office");
        typeComBox.setItems(timeoffList);
        
      //Selection Name table
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        empName.setCellValueFactory(new PropertyValueFactory<>("name"));
      //Timeoff Column assignments
        calTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        attachmentCol.setCellValueFactory(new PropertyValueFactory<>("attachment"));
        startDCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        specialCalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        String attachmentValue = newSelection.getAttachment();
        if (attachmentValue == null) {
            attachmentField.setPromptText("Add Link . . .");
        }
    }
});
       typeComBox.setOnAction(event -> {
    if (typeComBox.getValue() != null) {
        if (typeComBox.getValue().equals("On Travel")) {
            userOffIdLabel.setText("2");
        } else if (typeComBox.getValue().equals("On Leave")){
            userOffIdLabel.setText("1");
        } else if (typeComBox.getValue().equals("No Office")){
            userOffIdLabel.setText("3");
        }else{ userOffIdLabel.setText("");}
    }
});
        
    }
    public void setTable(){
        empNameTable.setItems(Attendance.getEmpName());
        updateBtn.setDisable(true);
        deactivateBtn.setDisable(true);
        userOffIdLabelTag.setDisable(true);
    }

    @FXML
    public void clearManage(ActionEvent event){
            clear();
    }

    public void clear(){
            typeComBox.setValue("");
            attachmentField.setText("");
            descField.setText("");
            userOffIdLabel.setText("");
            timeOffIDLabel.setText("");
            descField.setPromptText("Add description . . .");
            attachmentField.setPromptText("Add Link . . .");
            startPicker.setValue(null);
            endPicker.setValue(null);
            insertBtn.setDisable(false);
            typeComBox.setDisable(false);
            updateBtn.setDisable(false);
            deactivateBtn.setDisable(false);
            userOffIdLabelTag.setDisable(false);
    }


}
