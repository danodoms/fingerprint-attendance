/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Model.Fingerprint;
import com.digitalpersona.uareu.*;
import com.digitalpersona.uareu.Engine.PreEnrollmentFmd;
import com.digitalpersona.uareu.Reader.CaptureResult;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class EnrollmentThread extends Thread implements Engine.EnrollmentCallback{
    private ImageView imageview;
    private int userIdToEnroll;
    private CaptureThread captureThread;
    private int requiredFmdToEnroll = 2; //Default is 4
    public Engine engine = UareUGlobal.GetEngine();
    IdentificationThread identificationThread = new IdentificationThread();
    ArrayList<Fmd> fmdList = new ArrayList<>();
    boolean runThisThread = true;

    public EnrollmentThread(ImageView imageview, int userIdToEnroll){
        this.imageview = imageview;
        this.userIdToEnroll = userIdToEnroll;


    }


    public void startEnrollment() throws UareUException{
        Selection.closeAndOpenReader();
        int counter = 0;
        Prompt.prompt(Prompt.START_CAPTURE);

        while(counter < requiredFmdToEnroll && runThisThread){
            System.out.println("User ID to Enroll: " + userIdToEnroll);
            System.out.println("Attempt " + counter);
            counter++;

            //Calls the Override GetFmd method that is implemented from Engine.CreateEnrollmentFmd Class
            Fmd fmdToEnroll = null;

            try{
                fmdToEnroll = engine.CreateEnrollmentFmd(Fmd.Format.ISO_19794_2_2005, this);
            }catch(UareUException ex){
                Prompt.prompt(Prompt.UNABLE_TO_ENROLL);
                stopEnrollmentThread();
                continue;
            }


            System.out.println("FMD returned");
            fmdList.add(fmdToEnroll); // Add the Fmd to the list
            Prompt.prompt(Prompt.ANOTHER_CAPTURE);
        }




        // Insert all Fmds into the database
        for (Fmd fmd : fmdList) {
            Fingerprint.insertFmd(userIdToEnroll, fmd);
            System.out.println("Added FMD to database");
        }

        //Clears the current Fmd List to ensure integrity
        fmdList.clear();


        Prompt.prompt(Prompt.DONE_CAPTURE);
        stopEnrollmentThread();

        //print ennrollment thread stopped
        System.out.println("Enrollment Thread Stopped");
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

                    if(!identificationThread.fmdIsAlreadyEnrolled(fmdToEnroll, fmdList)){
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

    public void stopEnrollmentThread(){
        if(captureThread != null){
            runThisThread = false;
            captureThread.stopThread();
            System.out.println("Enrollment Thread Stopped");
        }

    }

    @Override
    public void run(){
        try {
            if(Selection.readerIsConnected_noLogging()){
                startEnrollment();
            }else{
                Prompt.prompt(Prompt.READER_DISCONNECTED);
                stopEnrollmentThread();
            }

        } catch (UareUException ex) {
            Logger.getLogger(EnrollmentThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
