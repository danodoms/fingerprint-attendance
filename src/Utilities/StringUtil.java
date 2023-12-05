/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author admin
 */
public class StringUtil {
    public static String createFullName(String fName, String mName, String lName, String suffix){
        fName = convertNullToEmpty(fName);
        mName = convertNullToEmpty(mName);
        lName = convertNullToEmpty(lName);
        suffix = convertNullToEmpty(suffix);
        
        String fullName = fName + " " + mName + " " + lName + " " + suffix;
        fullName = removeExtraSpaces(fullName);
        
        return fullName;
    }
    
    public static String createFullNameWithInitial(String fName, String mName, String lName, String suffix){
        fName = convertNullToEmpty(fName);
        mName = convertToInitial(convertNullToEmpty(mName));
        lName = convertNullToEmpty(lName);
        suffix = convertNullToEmpty(suffix);
        
        String fullName = fName + " " + mName + " " + lName + " " + suffix;
        fullName = removeExtraSpaces(fullName);
        
        return fullName;
    }
    
    private static String removeExtraSpaces(String text) {
        // Replace multiple spaces with a single space
        text = text.replaceAll("\\s+", " ");
        // Trim leading and trailing spaces
        text = text.trim();
        return text;
    }
    
    private static String convertNullToEmpty(String input) {
        return input == null ? "" : input;
    }
    
    public static String convertToInitial(String middleName) {
        if (middleName == null || middleName.isEmpty()) {
            return ""; // Return an empty string for null or empty input
        } else {
            // Take the first character of the middle name and append a period
            return middleName.substring(0, 1) + ".";
        }
    }

}
