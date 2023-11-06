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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author admin
 */
public class IdentificationThread extends Thread{
    private ImageView imageview; //sets the imageview to use as fingerprint display, likely from fpIdentification.fxml
    private Engine engine = UareUGlobal.GetEngine(); //creates engine to be used by whole class
    private CaptureThread captureThread;
    ObservableList<Fingerprint> fingerprintList;
    ObservableList<User> userList;
    int falsePositiveRate = Engine.PROBABILITY_ONE / 100000; //sets how accurate the identification should be to return candidate
    int candidateCount = 1; //how many candidate Fmd/s to return
    
    
    //constructor with fingerprint display
    public IdentificationThread(ImageView imageview){
        this.imageview = imageview;
    }
    
    
    
    //default constructor
    public IdentificationThread(){
    }
    
     
    
    //called by the run method for starting the identification process
    public void startIdentification(ImageView imageview) throws InterruptedException, UareUException{
//        Selection.reader.Close();
//        Selection.reader.Open(Reader.Priority.COOPERATIVE);
        Selection.closeAndOpenReader();
        while(true) {
            Fmd fmdToIdentify = getFmdFromCaptureThread(imageview);
            Fmd[] databaseFmds = getFmdsFromDatabase();
            compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
        }
        //Selection.reader.Close();
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
            System.out.println("candidate found");
            //topCandidateFmd = databaseFmds[candidateFmds[0].fmd_index];
            int topCandidateFmdIndex = candidateFmds[0].fmd_index;
            int matchingUserId = fingerprintList.get(topCandidateFmdIndex).getUserId();
            displayIdentifiedUser(matchingUserId);
            System.out.println("Your name is "+userList.get(0).getlName()+"");
            return true;
        }else{
            System.out.println("no candidate/s found");
            return false;
        }
    }
    
    
    
    //this method is used by "compareFmdToDatabaseFmds" for display purposes
    private void displayIdentifiedUser(int userId){
        userList = User.getUserByUserId(userId);
        String fname = userList.get(0).getfName();
        String mname = userList.get(0).getmName();
        String lname = userList.get(0).getlName();
        String suffix = userList.get(0).getSuffix();
    }
    
    
    
    //This method is used by enrollmentThread to check if the current FMD captured is already enrolled
    public boolean fmdIsAlreadyEnrolled(Fmd fmdToIdentify) throws UareUException{
        Fmd[] databaseFmds = getFmdsFromDatabase();
        return compareFmdToDatabaseFmds(fmdToIdentify, databaseFmds);
    }
    
    

    
    
    public void stopIdentificationThread() throws UareUException{
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
