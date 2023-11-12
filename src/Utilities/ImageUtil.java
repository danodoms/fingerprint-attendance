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
     public static Image byteArrayToImage(byte[] byteArray) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        return new Image(inputStream);
    }
}
