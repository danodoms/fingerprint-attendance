/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Controller.FingerprintController;
import com.digitalpersona.uareu.*;
import com.digitalpersona.uareu.Reader.*;
import com.digitalpersona.uareu.UareUException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javafx.scene.canvas.*;


/**
 *
 * @author admin
 */
public class CaptureThread2 extends Thread {
    public Reader reader;
    public ImageView imageview;
    public Canvas canvas;
    
    public CaptureThread2(Reader reader, ImageView imageview, Canvas canvas){
        this.imageview = imageview;
        this.reader = reader;
        this.canvas = canvas;
    }
 
    public void startStream(Reader reader, ImageView imageview, Canvas canvas) {
            int counter = 0;
        try {
            reader.Open(Reader.Priority.COOPERATIVE);
            //reader.StartStreaming();
            while (true) {
                System.out.println(counter); counter++;
                
//                    Reader.CaptureResult captureResult = reader.GetStreamImage(
//                        Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500);
                    
System.out.println(reader.GetStatus());
                     Reader.CaptureResult captureResult = reader.Capture(
                        Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);
                    
                    
                    //System.out.println(captureResult.quality);
                       
                            System.out.println("Capture quality: " + captureResult.quality);
                   

                    if (captureResult != null) {
                        Fid fid = captureResult.image;
                        
                        Fid.Fiv view = fid.getViews()[0];
                        int width = view.getWidth();
                        int height = view.getHeight();
                        byte[] imageData = view.getData();
                        
                        
                        BufferedImage Buffimage = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                         Buffimage.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
                        
                        Image image = SwingFXUtils.toFXImage(Buffimage, null);
                        
                        //Image img = new Image(new ByteArrayInputStream(imageData));
                        imageview.setFitWidth(width);
                        imageview.setFitHeight(height);
                        imageview.setImage(image);
                        
                        
//                        System.out.println(imageData);
//                        
//                        String base64String = byteArrayToBase64(imageData);
//                        System.out.println(base64String);
//                        
//                    try {
//                        Thread.sleep(20000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(CaptureThread2.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                        
                        
//                        GraphicsContext gc = canvas.getGraphicsContext2D();
//                        Image img = new Image(new ByteArrayInputStream(imageData));
//                        gc.drawImage(img, canvas.getWidth(), canvas.getHeight());
                        
                        
                        
                        //DEBUGGER FOR SETTING IMAGE IN IMAGEVIEW
                      String imagePath = getClass().getResource("sample.jpg").toExternalForm();
                        String imagePath2 = getClass().getResource("sample2.jpg").toExternalForm();
                        //Image image = new Image(imagePath);
                        //Image image2 = new Image(imagePath2);
                        
//                        imageview.setImage(image);
//                        //Platform.runLater(() -> imageview.setImage(image));
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(CaptureThread2.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        //imageview.setImage(image2);
                       //Platform.runLater(() -> imageview.setImage(image2));
                    }
                    //reader.Close();
                }
                
            } catch (UareUException ex) {
            Logger.getLogger(FingerprintController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        startStream(reader,imageview,canvas);
    }
    
    public String byteArrayToBase64(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }
    
}
