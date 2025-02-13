
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Fingerprint;

import com.attendance.Model.Fingerprint;
import com.attendance.Model.User;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import javafx.collections.ObservableList;

/**
 *
 * @author admin
 */
public class VerificationThread extends Thread{
    private Engine engine = UareUGlobal.GetEngine(); //creates engine to be used by whole class
    private CaptureThread captureThread;
    ObservableList<Fingerprint> fingerprintList;
    ObservableList<User> userList;
    int falsePositiveRate = Engine.PROBABILITY_ONE / 100000; //sets how accurate the identification should be to return candidate
    int candidateCount = 1; //how many candidate Fmd/s to return
    //private boolean isFingerprintMatched;
    private int userIdToMatch;
    private int delayTimeInMs;

    public boolean userIsVerified;

    boolean runThisThread = true;

    public boolean isCaptureCanceled;



    //constructor used for user fingerprint verification
    public VerificationThread(int userIdToMatch, int delayTimeInMs){
        this.userIdToMatch = userIdToMatch;
        this.delayTimeInMs = delayTimeInMs;
    }

    public VerificationThread(int userIdToMatch){
        this.userIdToMatch = userIdToMatch;
    }



    public void startVerification() throws InterruptedException, UareUException{
        Selection.closeAndOpenReader();

        //print identification thread started
        ThreadFlags.runVerificationThread = true;
        System.out.println("Verification Thread Started");

        while(runThisThread) {
            Fmd fmdToIdentify = getFmdFromCaptureThread();
            Fmd[] databaseFmds = getFmdsFromDatabase();
            //compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
            isFingerprintMatchWithUserId(fmdToIdentify, databaseFmds);
        }
        ThreadFlags.runVerificationThread = false;
        System.out.println("Verification Thread Stopped");

    }



    //first method used in "startIdentification"
    private Fmd getFmdFromCaptureThread()throws UareUException, InterruptedException{
//        while(ThreadFlags.runIdentificationThread){
//            System.out.println("Waiting for identification thread to end capture");
//            Thread.sleep(1000);
//        }

        captureThread = new CaptureThread("Verification Thread", delayTimeInMs);
        captureThread.start();
        captureThread.join(0); //wait till done

        isCaptureCanceled = captureThread.isCaptureCanceled;

        //store the FMD from the latest capture event, from captureThread
        CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
        if(evt == null ){
            System.out.println("evt is null");

            return null;
        }else{
            if(evt.captureResult.image == null){
                System.out.println("evt.captureResult.image is null");
                return null;
            }
            Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
            return fmdToIdentify;
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


    //used for verification
    private void isFingerprintMatchWithUserId(Fmd fmdToIdentify, Fmd[] databaseFmds) throws UareUException{

        if(fmdToIdentify == null){
            System.out.println("fmdToIdentify is null");
            //ThreadFlags.isFingerprintMatched = false;
            userIsVerified = false;
            runThisThread = false;
            return;
        }

        Candidate[] candidateFmds = engine.Identify(fmdToIdentify, 0, databaseFmds, falsePositiveRate, candidateCount );

        if(candidateFmds.length != 0){
            //topCandidateFmd = databaseFmds[candidateFmds[0].fmd_index];
            int topCandidateFmdIndex = candidateFmds[0].fmd_index;
            int matchingUserId = fingerprintList.get(topCandidateFmdIndex).getUserId();

            if(matchingUserId == userIdToMatch) {
                System.out.println("VERFICATION: Fingerprint matched with User ID");
                userIsVerified = true;
                //ThreadFlags.isFingerprintMatched = true;
            }else{
                System.out.println("VERFICATION: Fingerprint doesn't match with User ID");
                //ThreadFlags.isFingerprintMatched = false;
                userIsVerified = false;
            }

        }else{
            //print no candidates found for verification
            System.out.println("VERFICATION: No candidate/s found");
        }

        runThisThread = false;
    }


    @Override
    public void run(){
        try {
            startVerification();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (UareUException ex) {
            ex.printStackTrace();
        }
    }
}
