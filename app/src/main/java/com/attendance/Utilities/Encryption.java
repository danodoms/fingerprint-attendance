/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author admin
 */
public class Encryption {
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes(StandardCharsets.UTF_8));

            // Get the hashed password
            byte[] hashedPassword = md.digest();

            // Convert byte array to a string representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedPassword) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the exception or rethrow
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Hash the plaintext password
        String hashedInputPassword = hashPassword(plainPassword);

        // Compare the hashed passwords
        return hashedInputPassword.equals(hashedPassword);
    }
    
}
