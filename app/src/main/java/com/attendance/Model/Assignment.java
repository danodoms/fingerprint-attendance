/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Model;

import Utilities.DatabaseUtil;
import com.mysql.cj.jdbc.CallableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalTime;

/**
 *
 * @author admin
 */
public class Assignment {
    private int id;
    private int positionId;
    private String position;
    private int departmentId;
    private String department;
    private int shiftId;
    private String shift;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dateAssigned;
    private int status;
    
    //CALCULATED VARIABLE, NOT IN ACTUAL DATABASE TABLE
    private String timeRange;

    public Assignment(int assignmentId, String departmentName, String positionName, String shiftName, String startTime, String endTime, String dateAssigned) {
        this.id = assignmentId;
        this.department = departmentName;
        this.position = positionName;
        this.shift = shiftName;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.dateAssigned = dateAssigned;
    }

    public Assignment(int assignmentId, int departmentId, String departmentName, int positionId, String positionName, int shiftId, String shiftName, String timeRange, String startTime, String endTime, String dateAssigned, int status) {
        this.id = assignmentId;
        this.departmentId = departmentId;
        this.department = departmentName;
        this.positionId = positionId;
        this.position = positionName;
        this.shiftId = shiftId;
        this.shift = shiftName;
        this.timeRange = timeRange;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.dateAssigned = dateAssigned;
        this.status = status;
    }

    public Assignment(int assignmentId, int departmentId, String departmentName, int positionId, String positionName, int shiftId, String shiftName, String timeRange, String startTime, String endTime, String dateAssigned) {
        this.id = assignmentId;
        this.departmentId = departmentId;
        this.department = departmentName;
        this.positionId = positionId;
        this.position = positionName;
        this.shiftId = shiftId;
        this.shift = shiftName;
        this.timeRange = timeRange;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.dateAssigned = dateAssigned;
    }

    public String getTimeRange() {
        if (startTime != null && endTime != null) {
            timeRange = startTime.toString() + " - " + endTime.toString();
        }


        return timeRange;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(String dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Assignment(int id, String department, String position, String shift) {
        this.id = id;
        this.position = position;
        this.department = department;
        this.shift = shift;
    }
    
    public Assignment(int id, int departmentId, String department, int positionId, String position, int shiftId, String shift, String timeRange, String dateAssigned) {
        this.id = id;
        this.positionId = positionId;
        this.position = position;
        this.departmentId = departmentId;
        this.department = department;
        this.shiftId = shiftId;
        this.shift = shift;
        this.timeRange = timeRange;
        this.dateAssigned = dateAssigned;
    }

    public Assignment(int id, int departmentId, String department, int positionId, String position, int shiftId, String shift, String timeRange, String dateAssigned, int status) {
        this.id = id;
        this.positionId = positionId;
        this.position = position;
        this.departmentId = departmentId;
        this.department = department;
        this.shiftId = shiftId;
        this.shift = shift;
        this.timeRange = timeRange;
        this.dateAssigned = dateAssigned;
        this.status = status;
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
    
    public static ObservableList<Assignment> getActiveAssignmentsByUserId(int user_id) {
        ObservableList<Assignment> assignments = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection();
             CallableStatement callableStatement = (CallableStatement) connection.prepareCall("{call get_user_active_assignments(?)}")) {

            callableStatement.setInt(1, user_id);
            ResultSet rs = callableStatement.executeQuery();

            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getInt("assignment_id"),
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        rs.getInt("position_id"),
                        rs.getString("position_name"),
                        rs.getInt("shift_id"),
                        rs.getString("shift_name"),
                        rs.getString("time_range"), // Assuming the time_range column is returned by the stored procedure
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("date_assigned")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    public static ObservableList<Assignment> getAssignmentsByUserId(int user_id) {
        ObservableList<Assignment> assignments = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection();
             CallableStatement callableStatement = (CallableStatement) connection.prepareCall("{call get_user_assignments(?)}")) {

            callableStatement.setInt(1, user_id);
            ResultSet rs = callableStatement.executeQuery();

            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getInt("assignment_id"),
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        rs.getInt("position_id"),
                        rs.getString("position_name"),
                        rs.getInt("shift_id"),
                        rs.getString("shift_name"),
                        rs.getString("time_range"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("date_assigned"),
                        rs.getInt("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }
    
    //create method named getAssignmentCountByUserId with this query SELECT COUNT(*) FROM user u, assignment a WHERE u.user_id = a.user_id AND u.user_id = ?, dont use stored procedure
    public static int getActiveAssignmentCountByUserId(int user_id) {
        int assignmentCount = 0;
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "SELECT COUNT(*) FROM user u, assignment a WHERE u.user_id = a.user_id AND u.user_id = ? AND status = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                assignmentCount = rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentCount;
    }

    //create method getAssignmentByAssigmentId
    public static Assignment getAssignmentByAssignmentId(int assignmentId) {
        Assignment assignment = null;
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "SELECT a.assignment_id, p.position_name, d.department_name, s.shift_name, a.start_time, a.end_time, a.date_assigned from assignment a, shift s, position p, department d where s.shift_id = a.shift_id AND a.position_id = p.position_id AND p.department_id = d.department_id AND a.assignment_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, assignmentId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                assignment = new Assignment(
                        rs.getInt("assignment_id"),
                        rs.getString("department_name"),
                        rs.getString("position_name"),
                        rs.getString("shift_name"),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getString("date_assigned")
                );
                assignment.setStartTime(rs.getTime("start_time").toLocalTime());
                assignment.setEndTime(rs.getTime("end_time").toLocalTime());
                assignment.setDateAssigned(rs.getString("date_assigned"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignment;
    }
    
    
    
    
    public static void addAssignment(int userId, int positionId, int shiftId, String startTime, String endTime, String dateAssigned) throws SQLException{
        String insertQuery = "INSERT INTO `assignment`(user_id, position_id, shift_id, start_time, end_time, date_assigned) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, positionId);
            preparedStatement.setInt(3, shiftId);
            preparedStatement.setString(4, startTime);
            preparedStatement.setString(5, endTime);
            preparedStatement.setString(6, dateAssigned);
            
            preparedStatement.executeUpdate();
    }
    
    public static void updateAssignment(int assignmentId, int positionId, int shiftId, String startTime, String endTime) throws SQLException{
        String updateQuery = "UPDATE assignment SET position_id=?, shift_id=?, start_time=?, end_time=? WHERE assignment_id=?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, positionId);
            preparedStatement.setInt(2, shiftId);
            preparedStatement.setString(3, startTime);
            preparedStatement.setString(4, endTime);
            preparedStatement.setInt(5, assignmentId);
            
            preparedStatement.executeUpdate();
            
            System.out.println("Executed updateAssignment");
        }
    }
    
    public static void deactivateAssignment(int assignmentId) {
        String query = "UPDATE assignment SET status = 0 WHERE assignment_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, assignmentId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it as needed
        }
    }

    //method that inverts the status of an assignment
    public static void invertAssignmentStatus(int assignmentId) {
        String query = "UPDATE assignment SET status = 1 - status WHERE assignment_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, assignmentId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it as needed
        }
    }

    public static boolean positionAlreadyExists(int userId, int positionId) throws SQLException{
        String query = "SELECT * FROM assignment WHERE user_id = ? AND position_id = ? AND status = 1";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, positionId);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        }
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
