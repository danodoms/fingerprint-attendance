/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.PreEnrollmentFmd;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;
import Model.*;

/**
 *
 * @author admin
 */
public class EnrollmentThread extends Thread implements Engine.EnrollmentCallback{
    private PreEnrollmentFmd preEnrollFmd;
    private Reader reader;
    private ImageView imageview;
    private CaptureThread captureThread;
    private int FmdCount = 4;
    
    
    public EnrollmentThread(Reader reader, ImageView imageview){
        this.reader = reader;
        this.imageview = imageview;
    }
    
     public void startEnrollment(Reader reader, ImageView imageview) {
        int counter = 0;
        ArrayList<Fmd> capturedFMDs = new ArrayList<>();
        try {
            reader.Open(Reader.Priority.COOPERATIVE);
            Engine engine = UareUGlobal.GetEngine();
                while (counter != FmdCount) {
                    System.out.println(counter); counter++;
                    
                    //Calls the Override GetFmd method that is implemented from Engine.CreateEnrollmentFmd Class
                    Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.ISO_19794_2_2005, this);
                    //System.out.println("FMD view count: " + fmd.getViewCnt());
                    // Send the result to the listener
                    
                    if (null != fmd) {
                        //IMPORTANT!
                        //ADD HERE THE SQL INSERT QUERY THAT INSERTS THE FMD, AND THE ID OF THE CURRENT USER
                        System.out.println("FMD returned");
                        Prompt.prompt(Prompt.ANOTHER_CAPTURE);
                        
                        capturedFMDs.add(fmd);
                        System.out.println("FMD array count: " + capturedFMDs.size());
                    } else {
                        //SendToListener(ACT_CANCELED, null, null, null, null);
                        System.out.println("No FMD returned");
                        break;
                    }

                }
                Prompt.prompt(Prompt.DONE_CAPTURE);
                reader.Close();
                
                //DOUBLE CHECKS IF THERE ARE 4 FMDS AND THEN INSERTS TO DATABASE
                if(capturedFMDs.size() == 4){
                    for (Fmd fmd : capturedFMDs) {
                        System.out.println("Inserting Fmd to database");
                        Fingerprint.insertFmd(1, fmd);
                    }
                }
            } catch (UareUException ex) {
                ex.printStackTrace();
        }
    }
    
    

    @Override
    public PreEnrollmentFmd GetFmd(Fmd.Format format) {
        Engine.PreEnrollmentFmd prefmd = null;

        while(null == prefmd){
            //start capture thread
            captureThread = new CaptureThread(reader, imageview);
            captureThread.start();

            //prompt for finger
            //System.out.println("Place finger on reader");
            
            
            
            try {
                //wait till done
                captureThread.join(0);
                Prompt.prompt(Prompt.CONTINUE_CAPTURE);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            //check result
            CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
            if(null != evt.captureResult){
                if(Reader.CaptureQuality.CANCELED == evt.captureResult.quality){
                    //capture canceled, return null
                    break;
                }
                else if(null != evt.captureResult.image && Reader.CaptureQuality.GOOD == evt.captureResult.quality){
                    //acquire engine
                    Engine engine = UareUGlobal.GetEngine();

                    try{
                        //extract features
                        Fmd fmd = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);

                        //return prefmd 
                        prefmd = new Engine.PreEnrollmentFmd();
                        prefmd.fmd = fmd;
                        prefmd.view_index = 0;

                        System.out.println("FMD Features Extracted");
                    }
                    catch(UareUException e){ 
                        System.out.println("FMD Feature Extraction failed");
                    }
                }
                else{
                    //send quality result
                    //SendToListener(ACT_CAPTURE, null, evt.capture_result, evt.reader_status, evt.exception);
                }
            }
            else{
                //send capture error
                //SendToListener(ACT_CAPTURE, null, evt.capture_result, evt.reader_status, evt.exception);
            }
        }

        return prefmd;
    }
    
    @Override
    public void run(){
        startEnrollment(reader, imageview);
    }   
}
