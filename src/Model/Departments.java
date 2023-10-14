/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author admin
 */
public class Departments {
    private int id;
    private String departmentName;
   public static String defaultValue = "Select Department";
    
    public Departments(int id, String departmentName){
        this.id = id;
        this.departmentName = departmentName;
    }
    
//    public Departments(String departmentName){
//        this.departmentName = departmentName;
//    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getDefaultValue(){
        return defaultValue;
    }
    
    @Override
    public String toString() {
        return departmentName;
    }
    
}
