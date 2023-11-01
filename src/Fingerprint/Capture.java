/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;
import com.digitalpersona.uareu.*;
import com.digitalpersona.uareu.Reader.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author admin
 */
public class Capture {
    
    
    public static CaptureResult captureFingerprintImage(Reader reader) {
        try {
            // Open the reader
            reader.Open(Reader.Priority.COOPERATIVE);

            // Capture a single fingerprint image
            CaptureResult captureResult = reader.Capture(Fid.Format.ISO_19794_4_2005, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);

            return captureResult;
        } catch (UareUException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                // Close the reader
                reader.Close();
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static BufferedImage convertBytesToImage(byte[] imageBytes) {
    try {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();
        return image;
    } catch (IOException e) {
        e.printStackTrace();
        return null; // Handle the exception as needed
    }
}

    
    
    
    
//    public static Fmd captureFingerprintImage(Reader reader) {
//        try {
//            // Open the reader
//            reader.Open(Reader.Priority.COOPERATIVE);
//
//            // Capture a single fingerprint image
//            CaptureResult captureResult = reader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1);
//
//            if (captureResult.image != null && captureResult.quality == CaptureQuality.GOOD) {
//                // Successfully captured a fingerprint image
//                return (Fmd) captureResult.image;
//            } else {
//                // Handle capture failure or low-quality image
//                System.out.println("Fingerprint capture failed or quality is not good.");
//                return null;
//            }
//        } catch (UareUException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                // Close the reader
//                reader.Close();
//            } catch (UareUException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    
}
