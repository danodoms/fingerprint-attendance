/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import Utilities.DatabaseUtil;
import Controller.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author admin
 */
public class Department {
    private int id;
    private String departmentName;
   public static String defaultValue = "Select Department";
    
    public Department(int id, String departmentName){
        this.id = id;
        this.departmentName = departmentName;
    }
    
    public Department(String departmentName){
        this.departmentName = departmentName;
    }
    
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
    
    public static ObservableList<Department> getDepartments(){
        ObservableList<Department> departments = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT department_id, department_name FROM department");
            
            while (rs.next()) {
                  departments.add(new Department(
                          rs.getInt("department_id"),
                 rs.getString("department_name")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
        
    }
}
