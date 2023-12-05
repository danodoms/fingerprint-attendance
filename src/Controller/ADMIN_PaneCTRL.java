package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Utilities.PaneUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_PaneCTRL implements Initializable {

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
    
    private AnchorPane view;
    PaneUtil paneUtil = new PaneUtil();
    
    ArrayList<Button> buttonList = new ArrayList<>();
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DASHBOARD_PANE));
            contentPane.getChildren().setAll(view);
            view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DASHBOARD));
        } catch (IOException ex) {
            Logger.getLogger(ADMIN_PaneCTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
        //borderPaneOb.setCenter(view);
//        contentPane.getChildren().setAll(view);
        contentPane.getChildren().setAll(view);
        
        Button[] buttonArray = {dashboardBtn, attendanceBtn, employeesBtn, assignmentsBtn, fingerprintsBtn, reportsBtn};
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
    @FXML
    private void openEmpCalendar(ActionEvent event) throws IOException{
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_EMP_CALENDAR_PANE));
        contentPane.getChildren().setAll(view);
    }
    @FXML
    private void logOut(ActionEvent event) {
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
        button.setStyle("-fx-background-color: #07f9a2;"
                + "-fx-text-fill: #1a1a1a; ");
    }

    // Event handler to set default style
    private void setDefaultStyle(Button button) {
        button.setStyle("-fx-background-color: transparent;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #07f9a2"));
        //button.setOnMouseEntered(e -> button.setStyle("-fx-text-fill: #1a1a1a;"));
        button.setOnMouseExited(e -> {
        if (!button.isFocused()) {
            button.setStyle("-fx-background-color: transparent;");
            }
        });
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
}
