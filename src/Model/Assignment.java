/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtil;
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
public class Assignment {
    private int id;
    private String position;
    private String department;
    private String shift;

    public Assignment(int id, String department, String position, String shift) {
        this.id = id;
        this.position = position;
        this.department = department;
        this.shift = shift;
    }
    
    
    public static ObservableList<Assignment> getAssignmentByUserId(int user_id){
         ObservableList<Assignment> assignments = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            String query = "SELECT a.assignment_id, p.position_name, d.department_name, s.shift_name from user u, assignment a, shift s, position p, department d where u.user_id = a.user_id AND s.shift_id = a.shift_id AND a.position_id = p.position_id AND p.department_id = d.department_id AND u.user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                  assignments.add(new Assignment(
                                    rs.getInt("assignment_id"),
                           rs.getString("department_name"),
                          rs.getString("position_name"),
                          rs.getString("shift_name")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
    
    
}
