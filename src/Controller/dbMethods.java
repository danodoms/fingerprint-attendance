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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/v10_attendance_system";
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
    public ObservableList<Departments> getDepartments(){
        ObservableList<Departments> departments = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT departmentID, departmentName FROM department");
            
            while (rs.next()) {
                  departments.add(new Departments(
                                    rs.getInt("departmentID"),
                           rs.getString("departmentName")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
        
    }
    
    //queries positions based on selected department on employee mgmt ui, to display on choicebox
    public ObservableList<Positions> getPositionsByDepartmentId(int departmentId) {
    ObservableList<Positions> positions = FXCollections.observableArrayList();

    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {

        String query = "SELECT positionID, position_name FROM position p JOIN department d ON p.departmentID = d.departmentID WHERE p.departmentID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, departmentId);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
              positions.add(new Positions(
                            rs.getInt("positionID"),
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
    public ObservableList<Shifts> getShifts() {
    ObservableList<Shifts> shifts = FXCollections.observableArrayList();

    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {
        String query = "SELECT * FROM `shift`";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {            
            shifts.add(new Shifts(
                         rs.getInt("shiftID"),
                    rs.getString("shiftName"),
                    rs.getString("startTime"),
                      rs.getString("endTime")
            ));
        }
         System.out.println("SHIFTS: " + shifts);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return shifts;
}    



}
