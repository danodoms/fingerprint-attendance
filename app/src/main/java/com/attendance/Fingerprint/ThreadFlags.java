/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Fingerprint;

/**
 *
 * @author admin
 */
public class ThreadFlags {
    public static volatile boolean runIdentificationThread = false;
    public static volatile boolean runVerificationThread = false;
    public static volatile boolean programIsRunning = true;

    //flag used by VerificationThread to check if fingerprint is matched
    //public static volatile boolean isFingerprintMatched = false;

}
