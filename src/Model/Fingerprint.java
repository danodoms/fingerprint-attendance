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
    private int id;
    private int userId;
    private byte[] fmd;
    private Date registerDate;

    public Fingerprint(int id, int userId, byte[] fmd, Date registerDate) {
        this.id = id;
        this.userId = userId;
        this.fmd = fmd;
        this.registerDate = registerDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    String insertQuery = "INSERT INTO fingerprint (user_id, fmd, register_date) VALUES (?, ?, DATE(NOW()))";

        try (PreparedStatement preparedStatement = dbMethods.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, userId);

            // Convert FMD to a byte array
            byte[] fmdData = fmd.getData();
            preparedStatement.setBytes(2, fmdData);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle any database errors here
            e.printStackTrace();
        }
}
    
}
