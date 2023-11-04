/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fingerprint;
import com.digitalpersona.uareu.Fid.Fiv;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author admin
 */
public class Display {
    
    public static void displayFingerprint(Fiv view, ImageView imageview){
        BufferedImage Buffimage = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Buffimage.getRaster().setDataElements(0, 0, view.getWidth(), view.getHeight(), view.getImageData());
                        
        Image image = SwingFXUtils.toFXImage(Buffimage, null);
                        
//          imageview.setFitWidth(width);
//          imageview.setFitHeight(height);
        imageview.setImage(image);
    }
    
}
