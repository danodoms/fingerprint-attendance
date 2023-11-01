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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import com.digitalpersona.uareu.*;
import com.digitalpersona.uareu.Reader.*;
import com.digitalpersona.uareu.Fid.*;
import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
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
    @FXML
    private ImageView fingerprintImage;
    @FXML
    private Canvas fingerprintCanvas;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        m_reader = Selection.getReader();
        
        setReaderStatusLabel();
        CaptureThread2 capT = new CaptureThread2(m_reader, fingerprintImage, fingerprintCanvas);
        capT.start();
        
//        Capabilities rc = m_reader.GetCapabilities();
//			if(!rc.can_stream){
//				System.out.println("This reader does not support streaming");
//                                
//			}else{
//                            System.out.println("Streaming Supported");
//                        }
        
//        try {
//            m_reader.Open(Priority.COOPERATIVE);
//            m_reader.StartStreaming();
//            while (true) {
//                    CaptureResult captureResult = m_reader.GetStreamImage(
//                        Fid.Format.ISO_19794_4_2005, ImageProcessing.IMG_PROC_DEFAULT, 500);
//
//                    if (captureResult != null) {
//                        Fid fid = captureResult.image;
//                        Fiv fiv = fid.getViews()[0];
//                        byte[] imageData = fiv.getImageData();
//
//                        // Create a JavaFX Image from the captured bytes
//                        Image fxImage = new Image(new ByteArrayInputStream(imageData));
//
//                        // Update the ImageView with the new frame
//                        //fingerprintImage.setImage(fxImage);
//                        Platform.runLater(() -> fingerprintImage.setImage(fxImage));
//                    }
//                }
//            } catch (UareUException ex) {
//            Logger.getLogger(FingerprintController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            
//        if(m_reader != null){
//            CaptureResult captureResult = Capture.captureFingerprintImage(m_reader);
//
//            if (captureResult != null) {
//               
//                    System.out.println("Fingerprint image captured successfully.");
//                    // You can use the capturedImage for further processing or display.
//                    
//                    Fid fid = captureResult.image;
//                    System.out.println("fid: "+fid+"");
//                     
//                    Fiv fiv = fid.getViews()[0];
//                    System.out.println("fiv: "+fiv+"");
//                    
//                    System.out.println("fiv view count: "+fiv.getViewCnt()+"");
//                    System.out.println("fiv view number: "+fiv.getViewNumber()+"");
//                    System.out.println("fiv getImageData: "+fiv.getImageData()+"");
//
//                    
//                    BufferedImage img = Capture.convertBytesToImage(fiv.getImageData());
//                    
//                    System.out.println("img: "+img+"");
//                    
//                    //WritableImage writableImage = new WritableImage(img.getWidth(), img.getHeight());
//                    Image fxImage = SwingFXUtils.toFXImage(img, null);
//                    fingerprintImage.setImage(fxImage);
//                    
//                } else {
//                    System.out.println("Fingerprint image capture failed.");
//                } 
//        }
        
            
            
        
          
            
        }
        
        
        
//        m_reader = Selection.waitAndGetReader();
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
