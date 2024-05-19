package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Session.Session;
import Utilities.PaneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ContainerPaneCTRL implements Initializable {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button logOutAdminBtn;
    @FXML
    private Pane contentPane;
    @FXML
    private Button fingerprintsBtn;
    @FXML
    private Button employeesBtn;
    @FXML
    private Button assignmentsBtn;
    @FXML
    private Button attendanceBtn;
    @FXML
    private Button reportsBtn;
    private Button calendarBtn;
    @FXML
    private Button holidaysBtn;
    @FXML
    private Button timeOffBtn;
    @FXML
    private Button departmentsBtn;
    @FXML
    private Button positionsBtn;
    @FXML
    private Button shiftsBtn;
    
    private AnchorPane view;
    PaneUtil paneUtil = new PaneUtil();
    
    ArrayList<Button> buttonList = new ArrayList<>();
    @FXML
    private HBox reportsButtonContainer;
    @FXML
    private VBox navButtonsContainer;
    @FXML
    private TitledPane manageButtonsContainer;
    @FXML
    private HBox departmentsButtonContainer;
    @FXML
    private HBox attendanceButtonContainer;
    @FXML
    private HBox holidaysBtnContainer;
    @FXML
    private HBox assignmentsButtonContainer;
    @FXML
    private HBox positionButtonContainer;
    @FXML
    private HBox shiftButtonContainer;
    @FXML
    private HBox fingerprintsButtonContainer;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String dashboardPath = paneUtil.ADMIN_DASHBOARD;

        //if user is records officer, do this
        if(Session.getInstance().getLoggedInUser().getPrivilege().equalsIgnoreCase("records officer")){
            hideContainer(reportsButtonContainer);
            hideContainer(departmentsButtonContainer);
            hideContainer(attendanceButtonContainer);
            hideContainer(positionButtonContainer);
            hideContainer(shiftButtonContainer);
            hideContainer(fingerprintsButtonContainer);


            dashboardPath = paneUtil.RO_DASHBOARD;
        }

        //load dashboard pane
        try {
            view = FXMLLoader.load(getClass().getResource(dashboardPath));
            contentPane.getChildren().setAll(view);
            highlightButton(dashboardBtn);
        } catch (IOException ex) {
            Logger.getLogger(ContainerPaneCTRL.class.getName()).log(Level.SEVERE, null, ex);
        }

        contentPane.getChildren().setAll(view);


        //list navigation buttons
        Button[] buttonArray = {dashboardBtn, attendanceBtn, employeesBtn, assignmentsBtn, fingerprintsBtn, reportsBtn, departmentsBtn, positionsBtn, shiftsBtn, holidaysBtn, timeOffBtn};
        buttonList = new ArrayList<>(Arrays.asList(buttonArray));
    } 
    
    private void openEmployeeMgmtPane(ActionEvent event) {
        paneUtil.openPane(paneUtil.ADMIN_EMP_MGMT);
    }

    @FXML
    private void openDashboardPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DASHBOARD));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
        highlightButton(dashboardBtn);
    }
    @FXML
    private void openAttendancePane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_ATTENDANCE));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
        highlightButton(attendanceBtn);
    }
    @FXML
    private void openEmpPane(ActionEvent event) throws IOException {

        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_EMP_MGMT));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
        highlightButton(employeesBtn);
    }
     @FXML
    private void openAttRepPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_ATT_REPORTS));
        contentPane.getChildren().setAll(view);
        highlightButton(reportsBtn);
    }
    @Deprecated
    private void openHoCalendar(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_EMP_CALENDAR_PANE));
        contentPane.getChildren().setAll(view);
        highlightButton(holidaysBtn);
    }
    @FXML
    private void openTOCalendar(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_EMP_TIMEOFF_PANE));
        contentPane.getChildren().setAll(view);
        highlightButton(timeOffBtn);
    }
    @FXML
    private void logOut(ActionEvent event) {
        Session.getInstance().clearSession();
        paneUtil.exitAndOpenNewPane(logOutAdminBtn, paneUtil.LOGIN_PANE);
    }
    

    @FXML
    private void openFingerprintsPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_FINGERPRINTS));
        contentPane.getChildren().setAll(view);
        highlightButton(fingerprintsBtn);
    }

    @FXML
    private void openAssignmentsPane(ActionEvent event) throws IOException {
         view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_ASSIGNMENTS));
        contentPane.getChildren().setAll(view);
        highlightButton(assignmentsBtn);
    }
    
    
    // Event handler to set pressed style
    private void setPressedStyle(Button button) {
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #a3f8c2, #81ecae, #5ddc99);"
                + "-fx-text-fill: #3b3b3b; " + " -fx-transition: all 0.5s;");
    }

    // Event handler to set default style
    private void setDefaultStyle(Button button) {
        button.setStyle("-fx-background-color: transparent;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, #a3f8c2, #81ecae, #5ddc99"));
//        button.setOnMouseExited(e -> {
//        if (!button.isFocused()) {
//            button.setStyle("-fx-background-color: linear-gradient(to bottom, #a3f8c2, #81ecae, #5ddc99;");
//            }
//        });
    }
    
    private void highlightButton(Button selectedButton){
        setPressedStyle(selectedButton);
        System.out.println("Selected button: " + selectedButton.getText());
        System.out.println("buttonList size: " + buttonList.size()+"");
        
         
        
        for (Button btn : buttonList) {
            //System.out.println(" button: " + selectedButton+"");
            if (!(btn.getText().equals(selectedButton.getText()))) {
                setDefaultStyle(btn);
                System.out.println("Set default style for button: " + btn.getText());
            }
        }
    }

    @FXML
    private void openDepartmentsPane(ActionEvent event)throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DEPARTMENTS));
        contentPane.getChildren().setAll(view);
        highlightButton(departmentsBtn);
    }

    @FXML
    private void openPositionsPane(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_POSITIONS));
        contentPane.getChildren().setAll(view);
        highlightButton(positionsBtn);
    }

    @FXML
    private void openShiftsPane(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_SHIFTS));
        contentPane.getChildren().setAll(view);
        highlightButton(shiftsBtn);
    }

    @FXML
    private void openHolidaysPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_EMP_CALENDAR_PANE));
        contentPane.getChildren().setAll(view);
        highlightButton(holidaysBtn);
    }

    private void hideContainer(HBox container) {
        container.setVisible(false);
        container.setDisable(true);
        navButtonsContainer.setMargin(container, new Insets(-33,0,0,0) );
    }

//    private void hideContainer(HBox container) {
//        container.setVisible(false);
//        container.setDisable(true);
//        navButtonsContainer.setMargin(container, new Insets(-33,0,0,0) );
//    }
}
