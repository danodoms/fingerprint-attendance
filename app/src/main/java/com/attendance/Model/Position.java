/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Model;


import Utilities.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author admin
 */
public class Position {
    private int id;
    private String position;
    private String description;
    private int departmentId;
    private int status;

    
    public Position (int id, String position){
        this.id = id;
        this.position = position;
    }
    
    public Position (int id, int departmentId, String name, String description, int status){
        this.id = id;
        this.departmentId = departmentId;
        this.position = name;
        this.description = description;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
                          rs.getString("position_desc"),
                          rs.getInt("status")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
        
    }

    //add position object method to database
    public static void addPosition(String position, String description, int departmentId) throws SQLException{
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){

            String query = "INSERT INTO position (position_name, position_desc, department_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, position.trim());
            preparedStatement.setString(2, description.trim());
            preparedStatement.setInt(3, departmentId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePosition(int id, String position, String description, int departmentId) throws SQLException{
        String query = "UPDATE position SET position_name=?, position_desc=?, department_id=? WHERE position_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, position);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, departmentId);
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

            System.out.println("Executed updatePosition");
        }
    }

    //deactivate position object method to database
    public static void deactivatePosition(int positionId) throws SQLException{
        String query = "UPDATE position SET status=0 WHERE position_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, positionId);
            preparedStatement.executeUpdate();

            System.out.println("Executed deactivatePosition");
        }
    }

    //invert position status
    public static void invertPositionStatus(int positionId) throws SQLException{
        String query = "UPDATE position SET status=1-status WHERE position_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, positionId);
            preparedStatement.executeUpdate();

            System.out.println("Executed invertPositionStatus");
        }
    }

    //if position already exists based on department id
    public static boolean positionAlreadyExists(String position, int departmentId) throws SQLException{
        String query = "SELECT * FROM position WHERE position_name=? AND department_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, position);
            preparedStatement.setInt(2, departmentId);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        }
    }
}
