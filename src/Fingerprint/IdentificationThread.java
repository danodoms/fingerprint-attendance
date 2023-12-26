/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Model.Fingerprint;
import Model.User;
import Utilities.PaneUtil;
import Utilities.SoundUtil;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class IdentificationThread extends Thread{
    IdentificationModal identificationModal = new IdentificationModal();
    private ImageView imageview; //sets the imageview to use as fingerprint display, likely from fpIdentification.fxml
    private Engine engine = UareUGlobal.GetEngine(); //creates engine to be used by whole class
    public CaptureThread captureThread;
    ObservableList<Fingerprint> fingerprintList;
    ObservableList<User> userList;
    int falsePositiveRate = Engine.PROBABILITY_ONE / 100000; //sets how accurate the identification should be to return candidate
    int candidateCount = 1; //how many candidate Fmd/s to return
    int delayTimeInMs = 4000;
    
    private boolean headlessMode = false;

    public boolean runThisThread = true;
    
    //constructor with fingerprint display
    public IdentificationThread(ImageView imageview){
        this.imageview = imageview;
    }
    

    //default constructor
    public IdentificationThread(){
        headlessMode = true;
    }



    public void startIdentification(ImageView imageview) throws InterruptedException, UareUException{
        Selection.closeAndOpenReader();

        System.out.println("Identification Thread Started");
        while(runThisThread) {
            Fmd fmdToIdentify = getFmdFromCaptureThread(imageview);
            Fmd[] databaseFmds = getFmdsFromDatabase();
            compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
        }
        System.out.println("Identification Thread Stopped");
    }


      
    
    //first method used in "startIdentification"
    private Fmd getFmdFromCaptureThread(ImageView imageview)throws UareUException, InterruptedException{
//        captureThread = new CaptureThread(imageview);
        captureThread = new CaptureThread("Identification Thread", imageview, this);
        captureThread.start();
        captureThread.join(0); //wait till done


        //store the FMD from the latest capture event, from captureThread
//        CaptureThread.CaptureEvent evt = null;
//
//        if(captureThread.getLastCapture() != null){
//            evt = captureThread.getLastCapture();
//        }
//
//        if(evt.captureResult.image != null){
//            Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
//            return fmdToIdentify;
//        }else{
//            return null;
//        }


        //CHATGPT REVISION
        CaptureThread.CaptureEvent evt = captureThread.getLastCapture();

        if (evt != null && evt.captureResult.image != null) {
            Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
            return fmdToIdentify;
        } else {
            return null;
        }

    }



    //second method used in "startIdentification"
    private Fmd[] getFmdsFromDatabase() throws UareUException{
        fingerprintList = Fingerprint.getFingerprints();
        Fmd[] Fmds = new Fmd[fingerprintList.size()];
        
        for (int i = 0; i < fingerprintList.size(); i++) {
            if (fingerprintList.get(i).getFmd() != null) {
                Fmd convertedFmdBytes = UareUGlobal.GetImporter().ImportFmd(fingerprintList.get(i).getFmd(), Fmd.Format.ISO_19794_2_2005, Fmd.Format.ISO_19794_2_2005);
                Fmds[i] = convertedFmdBytes;
            }
        }
        return Fmds;
    }
    
    
    
    //third method used in "startIdentification"
    private boolean compareFmdToDatabaseFmds(Fmd fmdToIdentify, Fmd[] databaseFmds) throws UareUException{

        if(fmdToIdentify == null){
            System.out.println("fmdToIdentify is null");
            return false;
        }
        Candidate[] candidateFmds = engine.Identify(fmdToIdentify, 0, databaseFmds, falsePositiveRate, candidateCount );

        if(candidateFmds.length != 0){
            System.out.println("Candidate found");
            //topCandidateFmd = databaseFmds[candidateFmds[0].fmd_index];
            int topCandidateFmdIndex = candidateFmds[0].fmd_index;
            int matchingUserId = fingerprintList.get(topCandidateFmdIndex).getUserId();
            if(!headlessMode){
                userIdentificationSuccess(matchingUserId);
            }
            return true;
        }else{
            if(!headlessMode){
                userIdentificationFailed();
            }
            System.out.println("No candidate/s found");
            return false;
        }
    }
    
    //duplicate method, accepts additional Fmd ArrayList in addition to database Fmds for comparison purposes mainly on Enrollment Thread
    private boolean compareFmdToDatabaseFmds(Fmd fmdToIdentify, Fmd[] databaseFmds, ArrayList<Fmd> fmdList) throws UareUException {
        // Combine the input ArrayList with the databaseFmds
        Fmd[] combinedFmds = new Fmd[databaseFmds.length + fmdList.size()];

        // Copy databaseFmds to the beginning of the combinedFmds array
        System.arraycopy(databaseFmds, 0, combinedFmds, 0, databaseFmds.length);

        // Copy fmdList to the end of the combinedFmds array
        for (int i = 0; i < fmdList.size(); i++) {
            combinedFmds[databaseFmds.length + i] = fmdList.get(i);
        }

        Candidate[] candidateFmds = engine.Identify(fmdToIdentify, 0, combinedFmds, falsePositiveRate, candidateCount );

        if(candidateFmds.length != 0){
            System.out.println("Candidate found");
            return true;
        }else{
            System.out.println("No candidate/s found");
            return false;
        }
    }

    
    
    
    //this method is used by "compareFmdToDatabaseFmds" for display purposes
    private void userIdentificationSuccess(int userId){
        
        User user = User.getUserByUserId(userId);

        //SoundUtil.playSuccessSound();
        Platform.runLater(() -> {
            //controllerUtils.openAndClosePane(controllerUtils.FP_IDENTIFICATION_SUCCESS, 2250);
            identificationModal.displayIdentificationSuccess(delayTimeInMs, user);
        });

        //make the thread sleep based on delayTimeInMs
        try {
            if(ThreadFlags.runVerificationThread == false){
                Thread.sleep(delayTimeInMs);
            }else{
                while(ThreadFlags.runVerificationThread){
                    System.out.println("Waiting for verification thread to end capture");
                    Thread.sleep(1000);
                }
            }



        } catch (InterruptedException ex) {
            Logger.getLogger(IdentificationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    private void userIdentificationFailed(){
        SoundUtil.playFailSound();
        Platform.runLater(() -> {
            identificationModal.displayIdentificationFail(1500);
        });  
    }
    
    
    //This method is used by enrollmentThread to check if the current FMD captured is already enrolled
    public boolean fmdIsAlreadyEnrolled(Fmd fmdToIdentify) throws UareUException{
        Fmd[] databaseFmds = getFmdsFromDatabase();
        return compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
    }
    
    //duplicate method that accepts additional fmdList, mainly used on Erollment Thread
    public boolean fmdIsAlreadyEnrolled(Fmd fmdToIdentify, ArrayList<Fmd> fmdList) throws UareUException{
        Fmd[] databaseFmds = getFmdsFromDatabase();
        return compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds, fmdList);
    }


    //create method that stops thread
    public void stopThread(){
        runThisThread = false;
        captureThread.stopThread();
    }
   
    
    @Override
    public void run(){ 
        try {
            startIdentification(imageview);
        } catch (InterruptedException ex) {
            Logger.getLogger(IdentificationThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UareUException ex) {
            Logger.getLogger(IdentificationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
