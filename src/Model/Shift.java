/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author admin
 */
public class Shift {
    private int id;
    private String shiftName;
    private String startTime;
    private String endTime;
    private int status;

    //CONSTRUCTOR
    public Shift(int id, String shiftName, String startTime, String endTime, int status) {
        this.id = id;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
                          rs.getString("end_time"),
                        rs.getInt("status")
                ));
            }
             System.out.println("SHIFTS: " + shifts);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    public static ObservableList<Shift> getActiveShifts() {
        ObservableList<Shift> shifts = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM `shift` WHERE status = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                shifts.add(new Shift(
                        rs.getInt("shift_id"),
                        rs.getString("shift_name"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("status")
                ));
            }
            System.out.println("SHIFTS: " + shifts);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    //ADD SHIFT TO DATABASE
    public static void addShift(String shiftName, String startTime, String endTime){
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "INSERT INTO shift (shift_name, start_time, end_time) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shiftName);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, endTime);

            preparedStatement.executeUpdate();

            System.out.println("Shift added to database");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //UPDATE SHIFT
    public static void updateShift(int id, String shiftName, String startTime, String endTime){
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "UPDATE shift SET shift_name = ?, start_time = ?, end_time = ? WHERE shift_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shiftName);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, endTime);
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

            System.out.println("Shift updated in database");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DEACTIVATE SHIFT
    public static void deactivateShift(int id){
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "UPDATE shift SET status = 0 WHERE shift_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Shift deactivated in database");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //invert shift status
    public static void invertShiftStatus(int shiftId) throws SQLException{
        String query = "UPDATE shift SET status=1-status WHERE shift_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, shiftId);
            preparedStatement.executeUpdate();

            System.out.println("Executed invertShiftStatus");
        }
    }

    //create method shift already exists
    public static boolean shiftAlreadyExists(String shiftName) throws SQLException{
        String query = "SELECT * FROM shift WHERE shift_name = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, shiftName);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        }
    }


}
