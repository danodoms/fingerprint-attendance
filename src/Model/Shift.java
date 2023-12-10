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
public class Shift {
    private int id;
    private String shiftName;
    private String startTime;
    private String endTime;

    //CONSTRUCTOR
    public Shift(int id, String shiftName, String startTime, String endTime) {
        this.id = id;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    //CONSTRUCTOR
    public Shift(String shiftName) {
        this.shiftName = shiftName;
    }
   
    //CONSTRUCTOR
    public Shift(int id, String shiftName) {
        this.id = id;
        this.shiftName = shiftName;
    }
    
    //CONSTRUCTOR
    public Shift(int id){
        this.id = id;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getStartTime() {
        return (startTime != null) ? startTime : "";
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return (endTime != null) ? endTime : "";
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return shiftName;
    }
    
    public static ObservableList<Shift> getShifts() {
    ObservableList<Shift> shifts = FXCollections.observableArrayList();

    try (Connection connection = DatabaseUtil.getConnection();
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

}
