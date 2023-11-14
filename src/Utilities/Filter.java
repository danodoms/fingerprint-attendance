/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author admin
 */
public class Filter {
    public static String filterName(String name, String fieldName){
        if(name.equals("")){
            return fieldName + " can't be blank";
        }else if (name.matches("[a-zA-Z]+")) {
            return "";
        } else {
            return "Invalid " + fieldName.toString();
        }
    }
    
    public static String filterContactNum(String contactNum, String fieldName){
        try {
            // Attempt to parse the contact number as an integer
            int parsedNumber = Integer.parseInt(contactNum);

            // Check if the parsed number is exactly 11 digits
            if (String.valueOf(parsedNumber).length() == 11) {
                return "";
            } else {
                return "Invalid contact number";
            }
        } catch (NumberFormatException e) {
            // Handle the case where the contact number is not a valid integer
            return "Invalid contact number";
        }
    }
}
