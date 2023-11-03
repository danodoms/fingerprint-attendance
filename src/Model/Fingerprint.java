/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;
import Controller.*;
import com.digitalpersona.uareu.Fmd;
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

        try (PreparedStatement preparedStatement = dbMethods.getConnection().prepareStatement(insertQuery)) {
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

    try (Connection connection = dbMethods.getConnection();
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
         //System.out.println("SHIFTS: " + shifts);

    } catch (SQLException e) {
        e.printStackTrace();
    }
        return Fingerprints;
    }        
}
    

