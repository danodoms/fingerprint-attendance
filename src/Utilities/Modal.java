/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import com.dlsc.gemsfx.DialogPane;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

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


    public static boolean actionConfirmed(String title, String header, String content) {
        final boolean[] userResponse = {false};

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Get the result of the prompt
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                System.out.println("User clicked Yes");
                // Perform actions for Yes
                userResponse[0] = true;
            } else if (response == noButton) {
                System.out.println("User clicked No");
                // Perform actions for No
            }
            // Handle other button types if necessary
        });

        return userResponse[0];
    }

    public static boolean actionConfirmed(DialogPane dialogPane, String title, String header, String content) {
        dialogPane.showConfirmation("Confirmation Title", "A confirmation requires the user to decide.");
        return false;
    }
}
