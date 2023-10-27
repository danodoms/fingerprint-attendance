/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.digitalpersona.uareu.*;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FingerprintController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private Reader fingerprintReader;
    private Fmd enrollmentTemplate;
    
    public FingerprintController(Reader reader) {
//        this.fingerprintReader = reader;
//    }
//
//    public void startFingerprintEnrollment() {
//        try {
//            // Initialize enrollment
//            Engine engine = UareUGlobal.GetEngine();
//            Fmd.Format format = Fmd.Format.ISO_19794_2_2005; // Choose a suitable format
//            int sampleCount = 4; // Number of fingerprint samples for enrollment
//            Fmd[] samples = new Fmd[sampleCount];
//
//            for (int i = 0; i < sampleCount; i++) {
//                // Capture a fingerprint image
//                Reader.CaptureResult captureResult = fingerprintReader.Capture(Reader.Priority.COOPERATIVE);
//
//                // Check if the capture was successful
//                if (captureResult.image != null) {
//                    // Process the captured image to create an Fmd
//                    Fmd sampleFmd = engine.CreateFmd(captureResult.image, format);
//
//                    // Store the sample Fmd
//                    samples[i] = sampleFmd;
//                }
//            }
//
//            // Create an enrollment template from the captured samples
//            enrollmentTemplate = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378, samples);
//
//            // Store the enrollment template for later use (e.g., database storage)
//        } catch (UareUException e) {
//            e.printStackTrace();
//            // Handle the exception (e.g., display an error message)
//        }
//    }
//
//    public Fmd getEnrollmentTemplate() {
//        return enrollmentTemplate;
//    }
    }  
}
