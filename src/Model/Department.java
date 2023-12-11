/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import Utilities.DatabaseUtil;
import Controller.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    public static Department getDepartmentById(int departmentId){
        Department department = null;
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            String query = "SELECT department_id, department_name FROM department WHERE department_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, departmentId);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                  department = new Department(
                          rs.getInt("department_id"),
                 rs.getString("department_name")
                  );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }
    
    public static void addDepartment(String departmentName) throws SQLException{
        String insertQuery = "INSERT INTO department (department_name) VALUES (?)";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, departmentName);
            
            preparedStatement.executeUpdate();
    }
    
    public static void updateDepartment(int departmentId, String departmentName) throws SQLException{
        String updateQuery = "UPDATE department SET department_name=? WHERE department_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, departmentName);
            preparedStatement.setInt(2, departmentId);
            
            preparedStatement.executeUpdate();
            
            System.out.println("Executed updateDepartment");
        }
    }
    
    public static void deactivateDepartment(int departmentId) throws SQLException{
        String query = "UPDATE department SET status=0 WHERE department_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, departmentId);
            preparedStatement.executeUpdate();
            
            System.out.println("Executed deactivateDepartment");
        }
    }
}
