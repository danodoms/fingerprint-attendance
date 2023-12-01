package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Utilities.PaneUtil;
import java.io.IOException;
import java.net.URL;
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
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    private Button addEmployeeBtn;

    @FXML
    private Button dashBoardBtn, attendanceViewer, logOutAdminBtn, attendanceReportBtn;
    /**
     * Initializes the controller class.
     */
    // BorderPane borderPaneOb;
    
    private AnchorPane view;
    PaneUtil paneUtil = new PaneUtil();
//    @FXML
//    private Button attendanceReportBtn;
    
    @FXML
    private Pane contentPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DASHBOARD_PANE));
        } catch (IOException ex) {
            Logger.getLogger(ADMIN_PaneCTRL.class.getName()).log(Level.SEVERE, null, ex);
        }
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
    } 
    private void openEmployeeMgmtPane(ActionEvent event) {
        paneUtil.openPane(paneUtil.EMPLOYEE_MGMT_PANE);
    }

    @FXML
    private void openDashboardPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_DASHBOARD_PANE));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
    }
    @FXML
    private void openAttendancePane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_ATTENDANCE_PANE));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
    }
    @FXML
    private void openEmpPane(ActionEvent event) throws IOException {

        view = FXMLLoader.load(getClass().getResource(paneUtil.EMPLOYEE_MGMT_PANE));
        //borderPaneOb.setCenter(view);
        contentPane.getChildren().setAll(view);
    }
     @FXML
    private void openAttRepPane(ActionEvent event) throws IOException {
        view = FXMLLoader.load(getClass().getResource(paneUtil.ADMIN_ATT_REP_PANE));
        contentPane.getChildren().setAll(view);
    }
    @FXML
    private void logOut(ActionEvent event) {
        paneUtil.exitAndOpenNewPane(logOutAdminBtn, paneUtil.LOGIN_PANE);
    }
}
