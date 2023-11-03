/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;
import Controller.*;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author admin
 */
public class Prompt {
    //Predefined JavaFX Label for Prompting
    public static Label promptLabel;
    
    public static final String START_CAPTURE = "Touch Sensor to begin";
    public static final String CONTINUE_CAPTURE = "Touch Sensor again";
    public static final String ANOTHER_CAPTURE = "Place another finger";
    public static final String DONE_CAPTURE = "Enrollment Complete";
    
    public static void prompt(String promptType){
        Platform.runLater(() -> {
        promptLabel.setText(promptType);
    }); 
    }
}
