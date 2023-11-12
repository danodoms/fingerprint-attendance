/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.PreEnrollmentFmd;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;
import Model.*;
import com.digitalpersona.uareu.Reader.CaptureResult;

/**
 *
 * @author admin
 */
public class EnrollmentThread extends Thread implements Engine.EnrollmentCallback{
    private ImageView imageview;
    private CaptureThread captureThread;
    private int requiredFmdToEnroll = 4;
    public Engine engine = UareUGlobal.GetEngine();
    IdentificationThread identificationThread = new IdentificationThread();
    
    public EnrollmentThread(ImageView imageview){
        this.imageview = imageview;
    }
    
    
    
    public void startEnrollment(ImageView imageview) throws UareUException{
//        Selection.reader.Close();
//        Selection.reader.Open(Reader.Priority.COOPERATIVE);
        Selection.closeAndOpenReader();

        for (int attemptCounter = 0; attemptCounter < requiredFmdToEnroll; attemptCounter++) {
            System.out.println("Attempt " + attemptCounter);

            //Calls the Override GetFmd method that is implemented from Engine.CreateEnrollmentFmd Class
            Fmd fmdToEnroll = engine.CreateEnrollmentFmd(Fmd.Format.ISO_19794_2_2005, this);
            System.out.println("FMD returned");

            Fingerprint.insertFmd(5, fmdToEnroll);
            System.out.println("Added FMD to database");
            Prompt.prompt(Prompt.ANOTHER_CAPTURE);
        }
        
        Selection.reader.Close();
        Prompt.prompt(Prompt.DONE_CAPTURE);
    }
    
        

    @Override
    public PreEnrollmentFmd GetFmd(Fmd.Format format){
        Engine.PreEnrollmentFmd prefmd = null;

        while(null == prefmd){
            //Get captureResult from instance of captureThread
            CaptureResult captureResult = getCaptureResultFromCaptureThread(imageview);
            
            if(captureResult == null){
                continue;
            }
            
            if(Reader.CaptureQuality.CANCELED == captureResult.quality){
                break;
            }
            
            if(Reader.CaptureQuality.GOOD == captureResult.quality){
                try{
                    Fmd fmdToEnroll = engine.CreateFmd(captureResult.image, Fmd.Format.ISO_19794_2_2005); //createFmd from captureResult image
                    
                    if(!identificationThread.fmdIsAlreadyEnrolled(fmdToEnroll)){
                        prefmd = new Engine.PreEnrollmentFmd();
                        prefmd.fmd = fmdToEnroll;
                        prefmd.view_index = 0;
                        System.out.println("FMD Features Extracted");
                    }else{
                        Prompt.prompt(Prompt.ALREADY_ENROLLED);
                    }
                }
                catch(UareUException e){ 
                    System.out.println("FMD Feature Extraction failed");
                }
            }
        }
        return prefmd;
    }
    
    private CaptureResult getCaptureResultFromCaptureThread(ImageView imageview){
        captureThread = new CaptureThread(imageview);
        captureThread.start();
        try {
            captureThread.join(0); //wait till done
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        Prompt.prompt(Prompt.CONTINUE_CAPTURE);

        //store the CaptureResult from the latest capture event, from captureThread
        CaptureThread.CaptureEvent captureEvent = captureThread.getLastCapture();
        CaptureResult captureResult = captureEvent.captureResult;

        return captureResult;
    }    
    
    @Override
    public void run(){
        try {
            startEnrollment(imageview);
        } catch (UareUException ex) {
            Logger.getLogger(EnrollmentThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
