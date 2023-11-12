/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtils;
import Controller.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 *
 * @author admin
 */
public class User {
    private int id;
    private String fName;
    private String lName;
    private String mName;
    private String suffix;
    private String email;
    private String password;
    private String privilege;
    private int contactNum;
    private String sex;
    private LocalDate birthDate;
    private String address;
    private byte[] image;
    private int status;
   
    
    //Constructor
    public User(
        int id,
        String fName,
        String mName,
        String lName,
        String suffix,
        String email,
        String password,
        String privilege,
        int contactNum,
        String sex,
        LocalDate birthDate,
        String address,
        byte[] image,
        int status)
    {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.mName = mName;
        this.suffix = suffix;
        this.email = email;
        this.password = password;
        this.privilege = privilege;
        this.contactNum = contactNum;
        this.sex = sex;
        this.birthDate = birthDate;
        this.address = address;
        this.image = image;
        this.status = status;
    
    }

    
    
    public static void addUser(
        String fName,
        String mName,
        String lName,
        String suffix,
        String email,
        String password,
        String privilege,
        int contactNum,
        String sex,
        LocalDate birthDate,
        String address,
        byte[] image)
    {
        String insertQuery = "INSERT INTO `user`(`user_fname`, `user_mname`, `user_lname`, `suffix`, `email`, `password`, `privilege`, `user_cntct`, `sex`, `birth_date`, `address`, `user_img`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DatabaseUtils.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, mName);
            preparedStatement.setString(3, lName);
            preparedStatement.setString(4, suffix);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, privilege);
            preparedStatement.setInt(8, contactNum);
            preparedStatement.setString(9, sex);
            preparedStatement.setDate(10, Date.valueOf(birthDate.toString()));
            preparedStatement.setString(11, address);
            preparedStatement.setBytes(12, image);
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle any database errors here
            e.printStackTrace();
        }
    }
   
    
    public static int getNextUserId(){
        int nextUserId = 1;
        try (Connection connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT user_id from user order by user_id desc limit 1");

            if (rs.next()) {
                nextUserId = rs.getInt("user_id")+1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextUserId;  
    }
          
    public static ObservableList<User> getUserByUserId(int userId){
        ObservableList<User> users = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM user WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                  users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_fname"),
                        rs.getString("user_mname"),
                        rs.getString("user_lname"),
                        rs.getString("suffix"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("privilege"),
                        rs.getInt("user_cntct"),
                        rs.getString("sex"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("address"),
                        rs.getBytes("user_img"),
                        rs.getInt("user_status")    
                  ));
            }

             System.out.println(users);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public int getContactNum() {
        return contactNum;
    }

    public void setContactNum(int contactNum) {
        this.contactNum = contactNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return birthDate;
    }

    public void setDateOfBirth(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    
}
