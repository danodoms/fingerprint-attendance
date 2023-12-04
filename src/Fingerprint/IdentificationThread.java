/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Controller.LoginPaneCTRL;
import Model.Fingerprint;
import Model.User;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Utilities.*;
import java.util.ArrayList;
import javafx.scene.media.AudioClip;

/**
 *
 * @author admin
 */
public class IdentificationThread extends Thread{
    IdentificationModal identificationModal = new IdentificationModal();
    PaneUtil controllerUtils = new PaneUtil();
    private ImageView imageview; //sets the imageview to use as fingerprint display, likely from fpIdentification.fxml
    private Engine engine = UareUGlobal.GetEngine(); //creates engine to be used by whole class
    private CaptureThread captureThread;
    ObservableList<Fingerprint> fingerprintList;
    ObservableList<User> userList;
    int falsePositiveRate = Engine.PROBABILITY_ONE / 100000; //sets how accurate the identification should be to return candidate
    int candidateCount = 1; //how many candidate Fmd/s to return
    
    private boolean headlessMode = false;
    
    
    //constructor with fingerprint display
    public IdentificationThread(ImageView imageview){
        this.imageview = imageview;
    }
    
    
    
    //default constructor
    public IdentificationThread(){
        headlessMode = true;
    }
    
     
    
    //called by the run method for starting the identification process
    public void startIdentification(ImageView imageview) throws InterruptedException, UareUException{
        Selection.closeAndOpenReader();
        while(ThreadFlags.running) {
            Fmd fmdToIdentify = getFmdFromCaptureThread(imageview);
            Fmd[] databaseFmds = getFmdsFromDatabase();
            compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
        }
    }
    
      
    
    //first method used in "startIdentification"
    private Fmd getFmdFromCaptureThread(ImageView imageview)throws UareUException, InterruptedException{
        captureThread = new CaptureThread(imageview);
        captureThread.start();
        captureThread.join(0); //wait till done

        //store the FMD from the latest capture event, from captureThread
        CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
        Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
        
        return fmdToIdentify;
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
        String fname = user.getFname();
        String mname = user.getMname();
        String lname = user.getLname();
        String suffix = user.getSuffix();
        byte[] userImage = user.getImage();
        
        String fullName = fname + " " + mname + " " + lname + " " + suffix;
        System.out.println("You are " + fullName);
        //SoundUtil.playSuccessSound();
        
        //ADD A METHOD THAT VERIFIES FIRST IF USER HAS ALREADY TIMED IN/OUT, DO IT BY QUERYING THE ATTENDANCE RECORD
        //if(hasTimedInOrOut(userId))
        
        
        AudioClip buzzer = new AudioClip(getClass().getResource("/Audio/success.wav").toExternalForm());
        buzzer.play();
      
        Platform.runLater(() -> {
            //controllerUtils.openAndClosePane(controllerUtils.FP_IDENTIFICATION_SUCCESS, 2250);
            identificationModal.displayIdentificationSuccess(2750, fullName, userImage);
        });  
    }
    
    
    
    private void userIdentificationFailed(){
        AudioClip buzzer = new AudioClip(getClass().getResource("/Audio/fail.wav").toExternalForm());
        buzzer.play();
        
        
        Platform.runLater(() -> {
            identificationModal.displayIdentificationFail(1500);
        });  
    }
    
    
    
    private void hasTimedInOrOut(){
        
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
    

    
    
    public void stopIdentificationThread() throws UareUException{
        ThreadFlags.running = false;
        System.out.println("Identification Thread Stopped");
        Selection.reader.Close();
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
