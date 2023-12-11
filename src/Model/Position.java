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
public class Position {
    private int id;
    private String position;
    private String description;
    private int departmentId;
    
    public Position (int id, String position){
        this.id = id;
        this.position = position;
    }
//    public Position(String position){
//        this.position = position;
//    }
    
    public Position (int id, int departmentId, String name, String description){
        this.id = id;
        this.departmentId = departmentId;
        this.position = name;
        this.description = description;
    }
    
    public Position(String position){
        this.position = position;
    }
    
    @Override
    public String toString() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    
    
    public static ObservableList<Position> getPositionsByDepartmentId(int departmentId) {
        ObservableList<Position> positions = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection();
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
    
    public static ObservableList<Position> getPositions(){
        ObservableList<Position> positions = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT * FROM position");
            
            while (rs.next()) {
                  positions.add(new Position(
                          rs.getInt("position_id"),
                          rs.getInt("department_id"),
                          rs.getString("position_name"),
                          rs.getString("position_desc")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
        
    }
    
}
