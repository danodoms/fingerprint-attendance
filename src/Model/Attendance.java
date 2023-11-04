/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.dbMethods;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author User
 */
public class Attendance {
    private int id;
    private int userId;
    private Date date;
    private int dateStatus;
    private String timeIn;
    private String timeOut;
    
     public Attendance (int id, String timeIn){
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.dateStatus = dateStatus;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
     public int getId() {
        return dateStatus;
    }
     public int getUserID() {
        return userId;
    }
     public Date getDate() {
        return date;
    }
     public int getDateStatus() {
        return dateStatus;
    }
     public String getTimeIn() {
        return timeIn;
    }
     public String getTimeOut() {
        return timeOut;
    }
     
     public void setId() {
         this.id = id;
    }
     public void setUserId() {
        this.userId = userId;
    }
     public void setDate() {
       this.date = date;
    }
     public void setDateStatus() {
       this.dateStatus = dateStatus;
    }
     public void setTimeIn() {
        this.timeIn = timeIn;
    }
     public void setTimeOut() {
        this.timeOut = timeOut;
    }
     
        public static ObservableList<Attendance> getAttendance(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = dbMethods.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT attendance_id, user_id FROM attendance");
            
            while (rs.next()) {
                  attendance.add(new Attendance(
                          rs.getInt("timeIn"),
                 rs.getString("timeOut")
                  ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
        
    }
}
