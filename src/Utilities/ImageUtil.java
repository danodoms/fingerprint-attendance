/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import com.digitalpersona.uareu.Fid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author admin
 */
public class ImageUtil {
    private static Image defaultImage = new Image("/Images/default_user_img.jpg");
    
     public static Image byteArrayToImage(byte[] byteArray) {
        if(byteArray == null){
            System.out.println("ImageUtil: byteArray from database is NULL, replacing with default image");
            return defaultImage;
        }
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return new Image(inputStream);
        
    }
}
