/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import Model.Fingerprint;
import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Engine.Candidate;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

/**
 *
 * @author admin
 */
public class IdentificationThread extends Thread{
    private Reader reader;
    private ImageView imageview;
    private CaptureThread captureThread;
    
    
    public IdentificationThread(Reader reader, ImageView imageview){
        this.reader = reader;
        this.imageview = imageview;
    }
    
    public void startIdentification(Reader reader, ImageView imageview){
        //sets how accurate the identification should be to return candidate
        int falsePositiveRate = Engine.PROBABILITY_ONE / 100000;
        
        //how many candidate Fmd/s to return
        int candidateCount = 1;
        
        
        try {
            reader.Open(Reader.Priority.COOPERATIVE);
            Engine engine = UareUGlobal.GetEngine();
            while (true) {
                //ISO_19794_2_2005
                    captureThread = new CaptureThread(reader, imageview);
                    captureThread.start();

                    //prompt for finger
                    //System.out.println("Place finger on reader");


                    try {
                        //wait till done
                        captureThread.join(0);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    //store the FMD from the latest capture event, from capture thread
                    CaptureThread.CaptureEvent evt = captureThread.getLastCapture();
                    Fmd fmdToIdentify = engine.CreateFmd(evt.captureResult.image, Fmd.Format.ISO_19794_2_2005);
                    
                    Fmd[] databaseFmds = getFmdsFromDatabase();

                    Candidate[] candidateFmd = engine.Identify(fmdToIdentify, 0, databaseFmds, falsePositiveRate, candidateCount );
                     
                    if(candidateFmd != null){
                        System.out.println("candidate found");
                        
                    }else{
                        System.out.println("no candidate/s found");
                    }
                            
                }
            
        } catch(UareUException ex){
            ex.printStackTrace();
        }
    }
    
    private Fmd[] getFmdsFromDatabase(){
        Engine engine = UareUGlobal.GetEngine();

        ObservableList<Fingerprint> fingerprintList = Fingerprint.getFingerprints();
        byte[][] fmdBytes = extractFmdBytesFromDatabase(fingerprintList);

        Fmd[] Fmds = new Fmd[fmdBytes.length];
        
        try{
            for (int i = 0; i < fmdBytes.length; i++) {
                if (fmdBytes[i] != null) {
                    
                    Fmd convertedFmdBytes = UareUGlobal.GetImporter().ImportFmd(fmdBytes[i], Fmd.Format.ISO_19794_2_2005, Fmd.Format.ISO_19794_2_2005);
//                    Fmds[i] = engine.CreateFmd(fmdBytes[], 357, 392, 197, 0, 0, Fmd.Format.ISO_19794_2_2005);
                    Fmds[i] = convertedFmdBytes;
                }
            }
        } catch(UareUException ex){
            ex.printStackTrace();
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
    

    @Override
    public void run(){
        startIdentification(reader, imageview);
    }

   
}
