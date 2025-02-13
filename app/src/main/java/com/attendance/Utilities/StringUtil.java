/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Utilities;

/**
 *
 * @author admin
 */
public class StringUtil {
    public static String createFullName(String fName, String mName, String lName, String suffix){
        fName = convertNullToEmpty(fName);
        mName = convertToInitial(convertNullToEmpty(mName));
        lName = convertNullToEmpty(lName);
        suffix = convertNullToEmpty(suffix);
        
        String fullName = fName + " " + mName + " " + lName + " " + suffix;
        fullName = removeExtraSpaces(fullName);
        
        return fullName;
    }

    //create a method that will format the fullname like this: "Smith, John A. Jr." "Surname, Firstname MiddleInitial Suffix", make the return type array
    public static String[] formatFullName(String fName, String mName, String lName, String suffix){
        fName = convertNullToEmpty(fName);
        mName = convertToInitial(convertNullToEmpty(mName));
        lName = convertNullToEmpty(lName);
        suffix = convertNullToEmpty(suffix);

        String[] nameParts = new String[2];
        nameParts[0] = removeExtraSpaces(lName + ", ");
        nameParts[1] = removeExtraSpaces(fName + " " + mName + " " + suffix);

        return nameParts;
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
    
    public static String formatFullNameWithInitial(String fullName, String delimiter){
        String[] nameParts = fullName.split("\\$");
        
        String fName = nameParts.length > 0 ? nameParts[0] : "";
        String mName = nameParts.length > 1 ? nameParts[1] : "";
        String lName = nameParts.length > 2 ? nameParts[2] : "";
        String suffix = nameParts.length > 3 ? nameParts[3] : "";
        
        return createFullNameWithInitial(fName, mName, lName, suffix);
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

    //craete a method that returns a greetings based on the time of day, use buuilt in methods
    public static String getGreeting(){
        String greeting = "";
        int hour = java.time.LocalTime.now().getHour();
        if(hour >= 0 && hour < 12){
            greeting = "Good Morning";
        }else if(hour >= 12 && hour < 18){
            greeting = "Good Afternoon";
        }else if(hour >= 18 && hour < 24){
            greeting = "Good Evening";
        }
        return greeting;
    }


}
