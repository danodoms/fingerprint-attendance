/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import javafx.scene.media.AudioClip;

/**
 *
 * @author admin
 */
public class SoundUtil {
    
    //private static final String SUCCESS_SOUND_FILE = "/Fingerprint/success.mp3"; // Provide the correct path

    
     public static void playSuccessSound() {
         AudioClip buzzer = new AudioClip(SoundUtil.class.getResource("/Audio/success.wav").toExternalForm());
         buzzer.play();
     }

     public static void playFailSound() {
         AudioClip buzzer = new AudioClip(SoundUtil.class.getResource("/Audio/fail.wav").toExternalForm());
         buzzer.play();
     }

    public static void playTimeOutSound() {
        AudioClip buzzer = new AudioClip(SoundUtil.class.getResource("/Audio/timeOut.mp3").toExternalForm());
        buzzer.play();
    }

    public static void playPromptSound() {
        AudioClip buzzer = new AudioClip(SoundUtil.class.getResource("/Audio/prompt.mp3").toExternalForm());
        buzzer.play();
    }

    public static void playDenySound() {
        AudioClip buzzer = new AudioClip(SoundUtil.class.getResource("/Audio/deny.mp3").toExternalForm());
        buzzer.play();
    }

    
    
}
