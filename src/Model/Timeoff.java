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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author User
 */
public class Timeoff {

    private int userOffId;
    private int id;
    private int offId;
    private String type;
    private String description;
    private String attachment;
    private Date startDate;
    private Date endDate;
    private int total;

    public int getUserOffId() {
        return userOffId;
    }

    public void setUserOffId(int userOffId) {
        this.userOffId = userOffId;
    }
    
    public int getOffId() {
        return offId;
    }

    public void setOffId(int offId) {
        this.offId = offId;
    }
    
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getTotal() {
        return total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Timeoff(int id, int userOffID, int offID, String type, String description, String attachment, Date startDate, Date endDate, int total) {
        this.id = id;
        this.userOffId = userOffID;
        this.offId = offID;
        this.type = type;
        this.description = description;
        this.attachment = attachment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }
    public Timeoff(int userOffID, int id, int offID, String attachment, Date startDate, Date endDate) {
        this.id = id;
        this.userOffId = userOffID;
        this.offId = offID;
        this.description = description;
        this.attachment = attachment;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Timeoff( int id, int offID,String description, String attachment, Date startDate, Date endDate) {
        this.id = id;
        this.offId = offID;
        this.attachment = attachment;
        this.startDate = startDate;
        this.endDate = endDate;
    }
     public Timeoff(Date startDate,int total){
         this.startDate = startDate;
         this.total = total;
     }

     public static ObservableList<Timeoff> getTimeoffByUserId(int user_id) {
    ObservableList<Timeoff> timeoff = FXCollections.observableArrayList();
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement statement = connection.prepareStatement("select distinct `u`.`user_id` AS `id`,`ut`.`user_timeoff_id` AS `userOffID`,`to`.`timeoff_id` AS `offID`,`to`.`timeoff_type` AS `type`,`ut`.`attachment` AS `attachment`,`ut`.`description` AS `description`,`ut`.`start_date` AS `startDate`,`ut`.`end_date` AS `endDate`,to_days(`ut`.`end_date`) - to_days(`ut`.`start_date`) + 1 AS `total` from ((`v11_attendance_system`.`user` `u` join `v11_attendance_system`.`user_timeoff` `ut` on(`u`.`user_id` = `ut`.`user_id`)) join `v11_attendance_system`.`timeoff` `to` on(`ut`.`timeoff_id` = `to`.`timeoff_id`)) where `ut`.`status` = 1 AND `u`.`user_id`= ? order by `ut`.`start_date`;")) {

        statement.setInt(1, user_id); // Set the user_id in the PreparedStatement.

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
    timeoff.add(new Timeoff(
            rs.getInt("id"),
            rs.getInt("userOffid"),
             rs.getInt("offID"),
            rs.getString("type"),
            rs.getString("description"),
            rs.getString("attachment"),
            rs.getDate("startDate"),
            rs.getDate("endDate"),
            rs.getInt("total")
    ));
}
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return timeoff;
}
     
    public static void updateTimeoff(int userOffId, int userId, int offID, String description, String attachment, Date startDate, Date endDate) throws SQLException {
    String insertQuery = "UPDATE user_timeoff SET user_id=?, timeoff_id=?, description=?, attachment=?, start_date=?, end_date=? WHERE user_timeoff_id=?";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, offID);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, attachment);
            preparedStatement.setDate(5, (java.sql.Date) startDate);
            preparedStatement.setDate(6, (java.sql.Date) endDate);
            preparedStatement.setInt(7, userOffId);

            preparedStatement.executeUpdate();

            System.out.println("Executed addTimeoff");
    }
     
    public static void addTimeoff( int userId, int offID, String description, String attachment, Date startDate, Date endDate) throws SQLException {
        String updateQuery = "INSERT INTO `user_timeoff` (user_id, timeoff_id, description, attachment, start_date, end_date) VALUES (?,?,?,?,?,?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, offID);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, attachment);
            preparedStatement.setDate(5, (java.sql.Date) startDate);
            preparedStatement.setDate(6, (java.sql.Date) endDate);

            preparedStatement.executeUpdate();

            System.out.println("Executed updateTimeoff");
        }
    }
    public static void deactivateTimeoff(int timeoff_id) {
        String query = "UPDATE user_timeoff SET status = 0 WHERE user_timeoff_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, timeoff_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it as needed
        }
    }
}
