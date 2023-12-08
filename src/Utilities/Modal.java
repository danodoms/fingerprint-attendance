/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class Modal {
    public static void showModal(String title, String message) {
        Stage successStage = new Stage();
        successStage.initModality(Modality.APPLICATION_MODAL);
        successStage.setTitle(title);

        Label successLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> successStage.close());

        VBox modalContent = new VBox(successLabel, closeButton);
        modalContent.setSpacing(10);

        // Center the content
        modalContent.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(modalContent, 350, 250);
        successStage.setScene(successScene);
        successStage.show();
    }
}
