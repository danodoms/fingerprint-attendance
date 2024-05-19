/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;

import com.digitalpersona.uareu.Fid.Fiv;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

/**
 *
 * @author admin
 */
public class Display {
    
    public static void displayFingerprint(Fiv view, ImageView imageview){

        //null checking for both parameters
        if (view == null || imageview == null) {
            System.out.println("Display.displayFingerprint(): Invalid/null input parameters. Cannot display fingerprint.");
            return;
        }


        BufferedImage Buffimage = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Buffimage.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
                        
        Image image = SwingFXUtils.toFXImage(Buffimage, null);

        imageview.setImage(image);
    }
    
}
