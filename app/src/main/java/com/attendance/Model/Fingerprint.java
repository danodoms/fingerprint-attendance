/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Model;

import Utilities.DatabaseUtil;
import java.util.Date;
import com.attendance.Controller.*;
import com.digitalpersona.uareu.Fmd;
import com.mysql.cj.jdbc.CallableStatement;
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
public class Fingerprint {
    private int userId;
    private byte[] fmd;
    private int width;
    private int height;
    private int resolution;
    private int fingerPosition;
    private int cbeffId;
    private Date registerDate;

    public Fingerprint(int userId, byte[] fmd, int width, int height, int resolution, int fingerPosition, int cbeffId, Date registerDate) {
        this.userId = userId;
        this.fmd = fmd;
        this.width = width;
        this.height = height;
        this.resolution = resolution;
        this.fingerPosition = fingerPosition;
        this.cbeffId = cbeffId;
        this.registerDate = registerDate;
    }
    
    

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getFmd() {
        return fmd;
    }

    public void setFmd(byte[] fmd) {
        this.fmd = fmd;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
    

    public static void insertFmd(int userId, Fmd fmd) {
    String insertQuery = "INSERT INTO fingerprint (user_id, fmd, width, height, resolution, finger_position, cbeff_id, register_date) VALUES (?, ?, ?, ?, ?, ?, ?, DATE(NOW()))";

        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, userId);

            // Convert FMD to a byte array
            byte[] fmdData = fmd.getData();
            preparedStatement.setBytes(2, fmdData);

            int width = fmd.getWidth();
            preparedStatement.setInt(3, width);
            
            int height = fmd.getHeight();
            preparedStatement.setInt(4, height);
            
            int resolution = fmd.getResolution();
            preparedStatement.setInt(5, resolution);
            
            int fingerPosition = fmd.getViews()[0].getFingerPosition();
            preparedStatement.setInt(6, fingerPosition);
            
            int cbeffId = fmd.getCbeffId();
            preparedStatement.setInt(7, cbeffId);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle any database errors here
            e.printStackTrace();
        }
    }   
    public static ObservableList<Fingerprint> getFingerprints() {
    ObservableList<Fingerprint> Fingerprints = FXCollections.observableArrayList();

    try (Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement()) {
        String query = "SELECT * FROM fingerprint";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {            
            Fingerprints.add(new Fingerprint(
                         rs.getInt("user_id"),
                    rs.getBytes("fmd"),
                    rs.getInt("width"),
                    rs.getInt("height"),
                    rs.getInt("resolution"),
                    rs.getInt("finger_position"),
                    rs.getInt("cbeff_id"),
                    rs.getDate("register_date")
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
        return Fingerprints;
    }
    
    
    public static int getFingerprintCountByUserId(int id) {
        int fingerprintCount = 0;

        // Use a standard SQL query instead of a stored procedure
        String sqlQuery = "SELECT COUNT(*) AS fingerprint_count FROM fingerprint WHERE user_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                fingerprintCount = rs.getInt("fingerprint_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fingerprintCount;
    }

    
//    public static String getLastFingerprintEnrollByUserId(int id){
//        String lastEnrollDate = "";
//        
//        try{
//            Connection connection = DatabaseUtil.getConnection();
//            CallableStatement statement = (CallableStatement) connection.prepareCall("{CALL getUserFingerprintCount(?)}");
//            statement.setInt(1,id);
//            
//            ResultSet rs = statement.executeQuery();
//
//            if(rs.next()){
//                lastEnrollDate = rs.getString("register_date");
//            }
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return lastEnrollDate;
//    }
    
    public static String getLastFingerprintEnrollByUserId(int id) {
        String lastEnrollDate = "";

        // Use a standard SQL query instead of a stored procedure
        String sqlQuery = "SELECT MAX(register_date) AS last_enroll_date FROM fingerprint WHERE user_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                lastEnrollDate = rs.getString("last_enroll_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastEnrollDate;
    }

    
    public static void destroyEnrolledFingerprintsByUserId(int userId){
        // SQL query to delete enrolled fingerprints for the given user ID
        String sqlQuery = "DELETE FROM fingerprint WHERE user_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            // Set the user ID parameter in the prepared statement
            preparedStatement.setInt(1, userId);

            // Execute the update (delete) operation
            int affectedRows = preparedStatement.executeUpdate();

            // Check if any rows were affected
            if (affectedRows > 0) {
                System.out.println("Enrolled fingerprints for user ID " + userId + " deleted successfully.");
            } else {
                System.out.println("No enrolled fingerprints found for user ID " + userId + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    

