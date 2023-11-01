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
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 *
 * @author admin
 */
public class CaptureThread extends Thread {
    public Reader reader;
    public ImageView imageview;
    
    public CaptureThread(Reader reader, ImageView imageview){
        this.imageview = imageview;
        this.reader = reader;
    }
    
    public void startCapture(Reader reader, ImageView imageview) {
        int counter = 0;
        try {
            reader.Open(Reader.Priority.COOPERATIVE);
            while (true) {
                System.out.println(counter); counter++;
                
                System.out.println("Reader Status: " + reader.GetStatus());
                Reader.CaptureResult captureResult = reader.Capture(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);
                System.out.println("Capture quality: " + captureResult.quality);
                   
                Fid fid = captureResult.image;       
                Fid.Fiv view = fid.getViews()[0];
                Display.displayFingerprint(view, imageview);
            }
                
                
            } catch (UareUException ex) {
                ex.printStackTrace();
        }
    }
    
    public void startStream(Reader reader, ImageView imageview) {
        int counter = 0;
        try {
            reader.Open(Reader.Priority.COOPERATIVE);
            reader.StartStreaming();
            while (true) {
                System.out.println(counter); counter++;
                
                System.out.println("Reader Status: " + reader.GetStatus());
                Reader.CaptureResult captureResult = reader.GetStreamImage(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500);
                System.out.println("Capture quality: " + captureResult.quality);
                   
                Fid fid = captureResult.image;       
                Fid.Fiv view = fid.getViews()[0];
                Display.displayFingerprint(view, imageview);
            }
                
                
            } catch (UareUException ex) {
                ex.printStackTrace();
        }
    }    

    
    @Override
    public void run(){
        startCapture(reader,imageview);
    }
}
