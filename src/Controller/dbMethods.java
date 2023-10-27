/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import Model.*;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;

/**
 *
 * @author admin
 */
public class dbMethods {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/v11_attendance_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public Connection getConnection(){
        Connection connection;
        try{
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    //queries departments to display on employee mgmt choicebox
    public ObservableList<Department> getDepartments(){
        ObservableList<Department> departments = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
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
    
    //queries positions based on selected department on employee mgmt ui, to display on choicebox
    public ObservableList<Position> getPositionsByDepartmentId(int departmentId) {
    ObservableList<Position> positions = FXCollections.observableArrayList();

    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {

        String query = "SELECT position_id, position_name FROM position p JOIN department d ON p.department_id = d.department_id WHERE p.department_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, departmentId);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
              positions.add(new Position(
                            rs.getInt("position_id"),
                          rs.getString("position_name")
              ));
        }
        
         System.out.println(positions);

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return positions;
}
    
    //queries columns from shift table, to display on employee mgmt shift choicebox
    public ObservableList<Shift> getShifts() {
    ObservableList<Shift> shifts = FXCollections.observableArrayList();

    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
        String query = "SELECT * FROM `shift`";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {            
            shifts.add(new Shift(
                         rs.getInt("shift_id"),
                    rs.getString("shift_name"),
                    rs.getString("start_time"),
                      rs.getString("end_time")
            ));
        }
         System.out.println("SHIFTS: " + shifts);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return shifts;
}    

    
    
    
      public int getNextUserId(){
        int nextUserId = 1;
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT user_id from user order by user_id desc limit 1");
            
            if (rs.next()) {
                nextUserId = rs.getInt("user_id")+1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextUserId;
        
    }


}
