/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.User;
import Utilities.ImageUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ADMIN_FingerprintsCTRL implements Initializable {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_name;
    @FXML
    private Button enrollBtn;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label fingerprintCountLabel;
    @FXML
    private Label lastEnrollDateLabel;
    @FXML
    private Label nameLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        
        loadUserTable();
    }    
    
     public void loadUserTable(){
        ObservableList<User> users = User.getActiveEmployees();
        userTable.setItems(users);
    }

    @FXML
    private void loadUserDetails(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        int fingerprintCount = User.getFingerprintCountByUserId(selectedUser.getId());
        String lastFingerprintEnroll = User.getLastFingerprintEnrollByUserId(selectedUser.getId());
        byte[] userImage = User.getUserByUserId(selectedUser.getId()).getImage();
        
        
        
        fingerprintCountLabel.setText(fingerprintCount+"");
        nameLabel.setText(selectedUser.getFullName());
        lastEnrollDateLabel.setText(lastFingerprintEnroll);
        userImageView.setImage(ImageUtil.byteArrayToImage(userImage));
        
        
        
        
        System.out.println("Fingerprint cOUNT: "+ fingerprintCount+"");
        
    }
     
    
}
