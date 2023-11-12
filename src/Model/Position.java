/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


import Utilities.DatabaseUtils;
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
public class Position {
    private int position_id;
    private String position_name;
    private String position_description;
    private int department_ID;
    
    public Position (int position_id, String position_name){
        this.position_id = position_id;
        this.position_name = position_name;
    }
//    public Position(String position_name){
//        this.position_name = position_name;
//    }
    
    public Position(String position_name){
        this.position_name = position_name;
    }
    
    @Override
    public String toString() {
        return position_name;
    }
    public int getId() {
        return position_id;
    }

    public void setId(int position_id) {
        this.position_id = position_id;
    }

    public String getPosition() {
        return position_name;
    }

    public void setPosition(String position_name) {
        this.position_name = position_name;
    }

    public String getPosition_description() {
        return position_description;
    }

    public void setPosition_description(String position_description) {
        this.position_description = position_description;
    }

    public int getDepartment_ID() {
        return department_ID;
    }

    public void setDepartment_ID(int department_ID) {
        this.department_ID = department_ID;
    }
    
        public static ObservableList<Position> getPositionsByDepartmentId(int departmentId) {
    ObservableList<Position> positions = FXCollections.observableArrayList();

    try (Connection connection = DatabaseUtils.getConnection();
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
    
}
