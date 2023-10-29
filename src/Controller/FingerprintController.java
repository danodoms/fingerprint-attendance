/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.digitalpersona.uareu.*;
import Fingerprint.*;
import javafx.beans.binding.*;
import javafx.beans.value.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FingerprintController implements Initializable {
    
    private ReaderCollection m_collection;
    private Reader           m_reader;
    private ObservableObjectValue<Reader> ObservableReader;

    @FXML
    private Label readerStatusLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setReaderStatusLabel();
        
        m_reader = Selection.waitAndGetReader();
//        BooleanBinding readerNullBinding = Bindings.isNull((ObservableObjectValue<Reader>) reader);
//        readerStatusLabel.textProperty().bind(Bindings.when(readerNullBinding)
//        .then("Disconnected")
//        .otherwise("Connected"));

//
//        this.ObservableReader = new SimpleObjectProperty<>(ObservableReader);
//        readerStatusLabel.textProperty().bind(Bindings.when(ObservableReader.isNull())
//                                         .then("Disconnected")
//                                         .otherwise("Connected"));

        
        
        //reader = Selection.waitAndGetReader();

    }   
    
    private void setReaderStatusLabel(){
        String newText = readerStatusLabel.getText();
        if(Selection.readerIsConnected()){
            newText += " Connected";
            
        }else{
            newText += " Disconnected";
        }
            
        readerStatusLabel.setText(newText);
    }
}
