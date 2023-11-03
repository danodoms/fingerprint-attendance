/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

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
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

/**
 *
 * @author admin
 */
public class IdentificationThread extends Thread{
    private ImageView imageview;
    private Engine engine = UareUGlobal.GetEngine();
    private CaptureThread captureThread;
    ObservableList<Fingerprint> fingerprintList;
    ObservableList<User> userList;
    int falsePositiveRate = Engine.PROBABILITY_ONE / 100000; //sets how accurate the identification should be to return candidate
    int candidateCount = 1; //how many candidate Fmd/s to return
    
    public IdentificationThread(ImageView imageview){
        this.imageview = imageview;
    }
    
    public void startIdentification(ImageView imageview) throws InterruptedException, UareUException{
            Selection.reader.Open(Reader.Priority.COOPERATIVE);
            while(true) {
//                captureThread = new CaptureThread(imageview);
//                captureThread.start();
//
//                try {
//                    //wait till done
//                    captureThread.join(0);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//
//                //store the FMD from the latest capture event, from capture thread
//                CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
//                Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);

                Fmd fmdToIdentify = getFmdFromCaptureThread(imageview);

                Fmd[] databaseFmds = getFmdsFromDatabase();
                Candidate[] candidateFmds = engine.Identify(fmdToIdentify, 0, databaseFmds, falsePositiveRate, candidateCount );

                Fmd topCandidateFmd = null;
                if(candidateFmds.length != 0){
                    System.out.println("candidate found");
                    //topCandidateFmd = databaseFmds[candidateFmds[0].fmd_index];
                    int topCandidateIndex = candidateFmds[0].fmd_index;

                    int matchingUserId = fingerprintList.get(topCandidateIndex).getUserId();
                    displayIdentifiedUser(matchingUserId);
                    System.out.println("Your name is "+userList.get(0).getUser_lname()+"");
                }else{
                    System.out.println("no candidate/s found");
                }
            }
    }
    
    private Fmd[] getFmdsFromDatabase() throws UareUException{
        fingerprintList = Fingerprint.getFingerprints();
        byte[][] fmdBytes = extractFmdBytesFromDatabase(fingerprintList);

        Fmd[] Fmds = new Fmd[fmdBytes.length];
        
        for (int i = 0; i < fmdBytes.length; i++) {
            if (fmdBytes[i] != null) {
                Fmd convertedFmdBytes = UareUGlobal.GetImporter().ImportFmd(fmdBytes[i], Fmd.Format.ISO_19794_2_2005, Fmd.Format.ISO_19794_2_2005);
                Fmds[i] = convertedFmdBytes;
            }
        }

        return Fmds;
    }
    
    private byte[][] extractFmdBytesFromDatabase(ObservableList<Fingerprint> fingerprintList){
        byte[][] fmdBytesArray = new byte[fingerprintList.size()][];
        
        for(int i=0; i<fingerprintList.size(); i++){
            Fingerprint fingerprint = fingerprintList.get(i);
            byte[] fmdBytes = fingerprint.getFmd();
            
            if(fmdBytes != null){
                fmdBytesArray[i] = fmdBytes; 
            }
        }
        
        return fmdBytesArray;
    }
    
    private Fmd getFmdFromCaptureThread(ImageView imageview)throws UareUException, InterruptedException{
        captureThread = new CaptureThread(imageview);
        captureThread.start();
        //wait till done
        captureThread.join(0);

        //store the FMD from the latest capture event, from capture thread
        CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
        Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
        
        return fmdToIdentify;
    }
    
    private void displayIdentifiedUser(int userId){
        userList = User.getUserByUserId(userId);
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
