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
    public class REQUIRED{
        public static String name(String name, String fieldName){
            if(name.equals("")){
                return fieldName + " can't be empty";
            }else if (name.matches("[a-zA-Z]+")) {
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
    
    private static boolean containsUppercase(String str) {
        return str.matches(".*[A-Z].*");
    }
    
    private static boolean containsNumbers(String str) {
        return str.matches(".*\\d.*");
    }
}
