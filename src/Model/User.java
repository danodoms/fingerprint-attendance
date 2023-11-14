/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtil;
import Controller.*;
import Utilities.EncryptionUtil;
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
    private String fname;
    private String lname;
    private String mname;
    private String suffix;
    private String email;
    private String password;
    private String privilege;
    private String contactNum;
    private String sex;
    private LocalDate birthDate;
    private String address;
    private byte[] image;
    private int status;
   
    
    //Full Constructor
    public User(
        int id,
        String fname,
        String mname,
        String lname,
        String suffix,
        String email,
        String password,
        String privilege,
        String contactNum,
        String sex,
        LocalDate birthDate,
        String address,
        byte[] image,
        int status)
    {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.mname = mname;
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
    
    public User(String email, String password, String privilege){
        this.email = email;
        this.password = password;
        this.privilege = privilege;
    }

    
    
    public static void addUser(
        String fname,
        String mname,
        String lname,
        String suffix,
        String email,
        String password,
        String privilege,
        String contactNum,
        String sex,
        LocalDate birthDate,
        String address,
        byte[] image) throws SQLException
    {
        String hashedPassword = EncryptionUtil.hashPassword(password);
        String insertQuery = "INSERT INTO `user`(`user_fname`, `user_mname`, `user_lname`, `suffix`, `email`, `password`, `privilege`, `user_cntct`, `sex`, `birth_date`, `address`, `user_img`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, mname);
            preparedStatement.setString(3, lname);
            preparedStatement.setString(4, suffix);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setString(7, privilege);
            preparedStatement.setString(8, contactNum);
            preparedStatement.setString(9, sex);
            
            if (birthDate != null) {
                preparedStatement.setDate(10, Date.valueOf(birthDate.toString()));
            } else {
                preparedStatement.setNull(10, java.sql.Types.DATE);
            }

            preparedStatement.setString(11, address);
            preparedStatement.setBytes(12, image);
            
            preparedStatement.executeUpdate();
    }
   
    
    public static int getNextUserId(){
        int nextUserId = 1;
        try (Connection connection = DatabaseUtil.getConnection();
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
    
    
    public static ObservableList<User> getUsers(){
        ObservableList<User> users = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery("select * from user where user_status = 1;");
            
            while (rs.next()) {
                LocalDate birthDate = null;
                java.sql.Date sqlBirthDate = rs.getDate("birth_date");
                if (sqlBirthDate != null) {
                    birthDate = sqlBirthDate.toLocalDate();
                }

                  users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_fname"),
                        rs.getString("user_mname"),
                        rs.getString("user_lname"),
                        rs.getString("suffix"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("privilege"),
                        rs.getString("user_cntct"),
                        rs.getString("sex"),
                        birthDate,
                        rs.getString("address"),
                        rs.getBytes("user_img"),
                        rs.getInt("user_status")  
                  ));
            }
            
            System.out.println("Users: " + users);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users; 
    }
    
          
    public static ObservableList<User> getUserByUserId(int userId){
        ObservableList<User> users = FXCollections.observableArrayList();

        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM user WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                LocalDate birthDate = null;
                java.sql.Date sqlBirthDate = rs.getDate("birth_date");
                if (sqlBirthDate != null) {
                    birthDate = sqlBirthDate.toLocalDate();
                }
                
                  users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_fname"),
                        rs.getString("user_mname"),
                        rs.getString("user_lname"),
                        rs.getString("suffix"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("privilege"),
                        rs.getString("user_cntct"),
                        rs.getString("sex"),
                        birthDate,
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
    
    
    public static User getUserByEmail(String email){
        User user = null;
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "SELECT email, password, privilege FROM user WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                String userEmail = rs.getString("email");
                String password = rs.getString("password");
                String privilege = rs.getString("privilege");
                user = new User(userEmail, password, privilege);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
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

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
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
