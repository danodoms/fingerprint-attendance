/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.time.LocalTime;

/**
 *
 * @author admin
 */
public class Filter {
    public class REQUIRED{
        public static String name(String name, String fieldName){
            if(name.equals("")){
                return fieldName + " can't be empty";
            }else if (name.matches("[a-zA-Z ]+")) {
                return "";
            } else {
                return "Invalid " + fieldName.toString();
            }
        }
        
        public static String password(String password, String repeatPassword){
            String prompt = "";
            
            if(password.equals("")){
                return "Password can't be empty";
            }else{
                if(password.length() < 8){
                    prompt+= "Password should at least contain 8 characters" + "\n";
                }
                
                if(!(Filter.containsUppercase(password))){
                    prompt+= "Password should contain an uppercase letter" + "\n";
                }
                
                if(!(Filter.containsNumbers(password))){
                    prompt+= "Password should contain a number" + "\n";
                }
                
                if(!(password.equals(repeatPassword))){
                    prompt+= "Passwords doesn't match" + "\n";
                }
            }
            
            return prompt;
        }
        
        public static String email(String email) {
            // Regular expression for a simple email validation
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            
            if(email.matches(emailRegex)){
                return "";
            } else {
                return "Invalid email";
            }
        }
        
        public static String privilege(String privilege){
            if(privilege.equals("Select")){
                return "Privilege can't be empty";
            }else{
                return "";
            }
        }
    }
    
    public class OPTIONAL{
        public static String name(String name, String fieldName){
            if (name.matches("[a-zA-Z]+") || name.equals("")) {
                return "";
            } else {
                return "Invalid " + fieldName.toString();
            }
        }
        
        public static String password(String password, String repeatPassword){
            String prompt = "";
            
            if(password.equals("")){
                return "";
            }else{
                if(password.length() < 8){
                    prompt+= "Password should at least contain 8 characters" + "\n";
                }
                
                if(!(Filter.containsUppercase(password))){
                    prompt+= "Password should contain an uppercase letter" + "\n";
                }
                
                if(!(Filter.containsNumbers(password))){
                    prompt+= "Password should contain a number" + "\n";
                }
                
                if(!(password.equals(repeatPassword))){
                    prompt+= "Passwords doesn't match" + "\n";
                }
            }
            
            return prompt;
        }
        
        public static String contactNum(String contactNum, String fieldName){
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


    public class TIME{
        //method that accepts two time ranges and checks if they overlap
//        public static boolean isOverlapping(String start1, String end1, String start2, String end2) {
//            //convert the time ranges to integers
//            int start1Int = Integer.parseInt(start1.replace(":", ""));
//            int end1Int = Integer.parseInt(end1.replace(":", ""));
//            int start2Int = Integer.parseInt(start2.replace(":", ""));
//            int end2Int = Integer.parseInt(end2.replace(":", ""));
//
//            //check if the first time range is within the second time range
//            if(start1Int >= start2Int && start1Int <= end2Int){
//                return true;
//            }
//
//            //check if the second time range is within the first time range
//            if(start2Int >= start1Int && start2Int <= end1Int){
//                return true;
//            }
//
//            //check if the first time range is equal to the second time range
//            if(start1Int == start2Int && end1Int == end2Int){
//                return true;
//            }
//
//            //check if the first time range is greater than the second time range
//            if(start1Int <= start2Int && end1Int >= end2Int){
//                return true;
//            }
//
//            //check if the second time range is greater than the first time range
//            if(start2Int <= start1Int && end2Int >= end1Int){
//                return true;
//            }
//
//            //return false if the time ranges don't overlap
//            return false;
//        }

        public static boolean isOverlapping(String start1, String end1, String start2, String end2) {
            LocalTime startTime1 = LocalTime.parse(start1);
            LocalTime endTime1 = LocalTime.parse(end1);
            LocalTime startTime2 = LocalTime.parse(start2);
            LocalTime endTime2 = LocalTime.parse(end2);

            //print the four variables
            System.out.println("startTime1: " + startTime1);
            System.out.println("endTime1: " + endTime1);
            System.out.println("startTime2: " + startTime2);
            System.out.println("endTime2: " + endTime2);

            return !(endTime1.isBefore(startTime2) || startTime1.isAfter(endTime2));
        }
    }
    
    private static boolean containsUppercase(String str) {
        return str.matches(".*[A-Z].*");
    }
    
    private static boolean containsNumbers(String str) {
        return str.matches(".*\\d.*");
    }
}
