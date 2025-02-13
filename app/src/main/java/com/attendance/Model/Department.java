/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.attendance.Model;

import Utilities.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author admin
 */
public class Department {
    private int id;
    private String departmentName;
    private int status;
    private String description;
   public static String defaultValue = "Select Department";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department(int id, String departmentName){
        this.id = id;
        this.departmentName = departmentName;
    }
    
    public Department(String departmentName){
        this.departmentName = departmentName;
    }

    public Department(int id, String departmentName, String description, int status){
        this.id = id;
        this.departmentName = departmentName;
        this.description = description;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
            ResultSet rs = statement.executeQuery("SELECT department_id, department_name, description, status FROM department");
            
            while (rs.next()) {
                  departments.add(new Department(
                          rs.getInt("department_id"),
                        rs.getString("department_name"),
                    rs.getString("description"),
                    rs.getInt("status")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;  
    }

    public static ObservableList<Department> getActiveDepartments(){
        ObservableList<Department> departments = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT department_id, department_name, description, status FROM department WHERE status=1");

            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        rs.getString("description"),
                        rs.getInt("status")
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
            
            String query = "SELECT * FROM department WHERE department_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, departmentId);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                  department = new Department(
                          rs.getInt("department_id"),
                 rs.getString("department_name"),
                          rs.getString("description"),
                    rs.getInt("status")
                  );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }
    
    public static void addDepartment(String departmentName, String description) throws SQLException{
        String insertQuery = "INSERT INTO department (department_name, description) VALUES (?, ?)";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, departmentName.trim());
            preparedStatement.setString(2, description.trim());

            preparedStatement.executeUpdate();
    }
    
    public static void updateDepartment(int departmentId, String departmentName, String description) throws SQLException{
        String updateQuery = "UPDATE department SET department_name=?, description=? WHERE department_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, departmentName);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, departmentId);

            
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

    public static void invertDepartmentStatus(int departmentId) throws SQLException{
        Department department = getDepartmentById(departmentId);
        int status = department.getStatus() == 1 ? 0 : 1;

        String query = "UPDATE department SET status=? WHERE department_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, departmentId);
            preparedStatement.executeUpdate();

            System.out.println("Executed invertDepartmentStatus");
        }
    }

    public static boolean departmentNameExists(String departmentName) throws SQLException{
        String query = "SELECT * FROM department WHERE department_name=?";
        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(query);
        preparedStatement.setString(1, departmentName);

        ResultSet rs = preparedStatement.executeQuery();

        return rs.next();
    }
}
