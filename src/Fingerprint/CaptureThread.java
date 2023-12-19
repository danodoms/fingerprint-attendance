/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import javafx.application.Platform;
import javafx.scene.image.ImageView;


/**
 *
 * @author admin
 */
public class CaptureThread extends Thread{
    private ImageView imageview;
    private CaptureEvent lastCapture;
    private String threadName;
    
    public CaptureThread(ImageView imageview){
        this.imageview = imageview;

    }

    public CaptureThread(String threadName){
        this.threadName = threadName;

    }
    
    public void startCapture() {
        System.out.println(threadName + ": Capture Thread Started");
        try {
            //null checking for reader before executing the lines below
            if(Selection.reader == null){
                Platform.runLater(() -> {
                    Selection.reader = Selection.getReader();
                });
            }else{
                System.out.println("Reader Status: " + Selection.reader.GetStatus());
                Reader.CaptureResult captureResult = Selection.reader.Capture(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);

                lastCapture = new CaptureEvent(captureResult, Selection.reader.GetStatus());
                System.out.println("Capture quality: " + captureResult.quality);

                //Store sigle fingerprint view
                Fid fid = captureResult.image;
                Fid.Fiv view = fid.getViews()[0];

                //Display fingerprint image on imageview
                if(imageview != null){
                    Display.displayFingerprint(view, imageview);
                }
            }


                
            } catch (UareUException ex) {
                ex.printStackTrace();
            }
        System.out.println(threadName + ": Capture Thread Stopped");
    }
    
    public void startStream(ImageView imageview) {
        int counter = 0;
        try {
            Selection.reader.Open(Reader.Priority.COOPERATIVE);
            Selection.reader.StartStreaming();
            
                while (true) {
                    System.out.println(counter); counter++;

                    System.out.println("Reader Status: " + Selection.reader.GetStatus());
                    Reader.CaptureResult captureResult = Selection.reader.GetStreamImage(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500);
                    System.out.println("Capture quality: " + captureResult.quality);
                        
                    //Store sigle fingerprint view
                    Fid fid = captureResult.image;       
                    Fid.Fiv view = fid.getViews()[0];
                    
                    //Display fingerprint image on imageview
                    Display.displayFingerprint(view, imageview);
                }
            } catch (UareUException ex) {
                ex.printStackTrace();
        }
    }

    public class CaptureEvent{
        private static final long serialVersionUID = 101;

        public Reader.CaptureResult captureResult;
        public Reader.Status        readerStatus;
        public UareUException       exception;

        public CaptureEvent(Reader.CaptureResult captureResult, Reader.Status raderStatus){
                this.captureResult = captureResult;
                this.readerStatus = readerStatus;
        }
        
        
    }
    
    public CaptureEvent getLastCapture(){
        return lastCapture;
    }

    @Override
    public void run(){
        startCapture();
    }
}
