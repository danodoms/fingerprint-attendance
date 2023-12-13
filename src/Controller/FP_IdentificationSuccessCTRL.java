/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Utilities.ImageUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FP_IdentificationSuccessCTRL implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private Label nameLabel1;
    @FXML
    private ImageView userImageView;
    @FXML
    private ProgressBar progressBar;


    int delayTimeInMs = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
     public void setUserData(int delayTimeInMs, String userName, byte[] imageData) {
        // Set the actual image and name in the placeholder
        userImageView.setImage(ImageUtil.byteArrayToImage(imageData));
        nameLabel.setText(userName);
        this.delayTimeInMs = delayTimeInMs;
        loadProgressbar();
    }

    //make the progress bar decrease automatically in 3 seconds from 100 to 0 percent
    public void loadProgressbar(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 100; i >= 0; i--) {
                    updateProgress(i, 100);
                    Thread.sleep(delayTimeInMs/100); // Simulate some work being done
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


}
