/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import Model.*;
import javafx.collections.FXCollections;

/**
 *
 * @author admin
 */
public class dbMethods {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_system";
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
    
    public ObservableList<Departments> getDepartments(){
        ObservableList<Departments> departments = FXCollections.observableArrayList();
        try {
            Connection connection = getConnection();
            Statement statement = (Statement) connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT department_ID, department_name FROM department");
            
            while (resultSet.next()) {
                int id = resultSet.getInt("department_ID");
                String departmentName = resultSet.getString("department_name");
                Departments departmentObj = new Departments(id, departmentName);
                departments.add(departmentObj);
                System.out.println(departments);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
        
    }
    
//    public ObservableList<Positions> getPositionsByDepartment(String department){
//              ObservableList<Positions> positions = FXCollections.observableArrayList();
//        try {
//            Connection connection = getConnection();
//            Statement statement = (Statement) connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT position_name FROM position p JOIN department d ON p.department_ID = d.department_ID WHERE department_name = '" + department + "'");
//            
//            while (resultSet.next()) {
//                String positionName = resultSet.getString("position_name");
//                Positions positionObj = new Positions(positionName);
//                positions.add(positionObj);
//                System.out.println(positions);
//            }
//
//            resultSet.close();
//            statement.close();
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return positions;
//    }
    
    public ObservableList<Positions> getPositionsByDepartment(String department) {
    ObservableList<Positions> positions = FXCollections.observableArrayList();

    try (Connection connection = getConnection();
        Statement statement = connection.createStatement()) {

        String query = "SELECT position_name FROM position p JOIN department d ON p.department_ID = d.department_ID WHERE department_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, department);
        
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String positionName = resultSet.getString("position_name");
            positions.add(new Positions(positionName));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

        return positions;
    }

}
