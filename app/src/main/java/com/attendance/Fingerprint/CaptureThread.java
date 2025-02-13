/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Fingerprint;

import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import javafx.scene.image.ImageView;
import com.attendance.Fingerprint.IdentificationThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author admin
 */
public class CaptureThread extends Thread{
    private ImageView imageview;
    private CaptureEvent lastCapture;
    private String threadName;
    private int delayTimeInMs;
    public boolean isCaptureCanceled; //used by other threads to check if capture is canceled
    IdentificationThread identificationThread;
    public boolean runCapture = true;
    
    public CaptureThread(ImageView imageview){
        this.imageview = imageview;

    }

    public CaptureThread(String threadName){
        this.threadName = threadName;

    }

    public CaptureThread(String threadName, int delayTimeInMs){
        this.threadName = threadName;
        this.delayTimeInMs = delayTimeInMs;

    }

    public CaptureThread(String threadName, ImageView imageview, IdentificationThread identificationThread){
        this.threadName = threadName;
        this.imageview = imageview;
        this.identificationThread = identificationThread;
    }

    public void startCapture() {
        System.out.println(threadName + ": Capture Thread Started");


        while(runCapture && Selection.readerIsConnected_noLogging()) {
            try {
                System.out.println(threadName + "Reader Status: " + Selection.reader.GetStatus());
                cancelCaptureBasedOnDelayTime(delayTimeInMs); //only cancels capture if delayTimeInMs is not 0
                Reader.CaptureResult captureResult = Selection.reader.Capture(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);

                //if(captureResult.image != null){
                    lastCapture = new CaptureEvent(captureResult, Selection.reader.GetStatus());
//                }

                System.out.println(threadName + "Capture quality: " + captureResult.quality);
                runCapture = false;

                //Store single fingerprint view
                Fid.Fiv view = (captureResult.image != null) ? captureResult.image.getViews()[0] : null;

                //Display fingerprint image on imageview
                Display.displayFingerprint(view, imageview);

            } catch (UareUException ex) {
                ex.printStackTrace();
            }
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

        public CaptureEvent(Reader.CaptureResult captureResult, Reader.Status readerStatus){
                this.captureResult = captureResult;
                this.readerStatus = readerStatus;
        }
    }
    
    public CaptureEvent getLastCapture(){
        return lastCapture;
    }

    public void cancelCaptureBasedOnDelayTime(int delayTimeInMs){
        if(delayTimeInMs != 0){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    System.out.println("Delaying for " + delayTimeInMs + "ms");
                    Thread.sleep(delayTimeInMs);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try {
                    Selection.reader.CancelCapture();
                    isCaptureCanceled = true;
                } catch (UareUException e) {
                    throw new RuntimeException(e);
                }
            });
            executor.shutdown();
        }
    }

    public void stopThread (){
        try {
            Selection.reader.CancelCapture();
        } catch (UareUException e) {
            throw new RuntimeException(e);
        }
        isCaptureCanceled = true;
    }

    @Override
    public void run(){
        //check first if reader is connected
        if(Selection.readerIsConnected_noLogging()){
            startCapture();
        }else{
            System.out.println(threadName + ": Capture Thread needs reader to be connected, closing thread...");
            runCapture = false;
            //sleep thread for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //stopThread();
        }

    }
}
