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
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author User
 */
public class Special_Calendar {
    private int id;
    private String type;
    private String description;
    private String attachment;
    private Date startDate;
    private Date endDate;
    private int total;
    private String name;
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public Special_Calendar(int id, String type, String description, Date startDate, Date endDate, int total) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }
    public Special_Calendar(int id, String type, String description, String attachment, Date startDate, Date endDate) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.attachment = attachment;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Special_Calendar( String type, String description, String attachment, Date startDate, Date endDate) {
        this.type = type;
        this.description = description;
        this.attachment = attachment;
        this.startDate = startDate;
        this.endDate = endDate;
    }
     public Special_Calendar(Date startDate,int total){
         this.startDate = startDate;
         this.total = total;
     }
     
     public Special_Calendar(String name, String type,int year, int month, int day){
        this.name = name;
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
     }
     
    public static ObservableList<Special_Calendar> getHolidayDtr(){
        ObservableList<Special_Calendar> dtrHolidayDocx = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT * FROM `user_calendar_schedule`");
            
            while (rs.next()) {
                dtrHolidayDocx.add(new Special_Calendar(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("year"),
                        rs.getInt("month"),
                        rs.getInt("day")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dtrHolidayDocx;
    }
     
     public static ObservableList<Special_Calendar> getCalendarByUserId(int user_id) {
    ObservableList<Special_Calendar> calendar = FXCollections.observableArrayList();
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM `special_calendar_view` WHERE id = ?")) {

        statement.setInt(1, user_id); // Set the user_id in the PreparedStatement.

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
    calendar.add(new Special_Calendar(
            rs.getInt("id"),
            rs.getString("type"),
            rs.getString("description"),
            rs.getDate("startDate"),
            rs.getDate("endDate"),
            rs.getInt("total")
    ));
}
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return calendar;
}
    public static ObservableList<Special_Calendar> getSpecialCalendar(){
        ObservableList<Special_Calendar> calendar = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT special_calendar_id as id,\n" +
                                                    "       sc_type as type,\n" +
                                                    "       sc_desc as description,\n" +
                                                    "       attachment,\n" +
                                                    "       start_date as startDate,\n" +
                                                    "       end_date as endDate\n" +
                                                    "FROM special_calendar\n" +
                                                    "WHERE status = 1;");
            
            while (rs.next()) {
                calendar.add(new Special_Calendar(
              rs.getInt("id"),
            rs.getString("type"),
            rs.getString("description"),
             rs.getString("attachment"),
             rs.getDate("startDate"),
              rs.getDate("endDate")
    ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }
    
    public static void updateSpecialCalendar(int id, String type, String description, String attachment, Date startDate, Date endDate) throws SQLException {
    String insertQuery = "UPDATE special_calendar SET sc_type=?, sc_desc=?, attachment=?, start_date=?, end_date=? WHERE special_calendar_id=?";

        PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(insertQuery);

            preparedStatement.setString(1, type);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, attachment);
            preparedStatement.setDate(4, (java.sql.Date) startDate);
            preparedStatement.setDate(5, (java.sql.Date) endDate);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();

            System.out.println("Executed Update Special Calendar");
    }
    
    public static void addSpecialCalendar( String type, String description, String attachment, Date startDate, Date endDate) throws SQLException {
        String addQuery = "INSERT INTO `special_calendar` (sc_type, sc_desc, attachment, start_date, end_date) VALUES (?,?,?,?,?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addQuery)) {

           preparedStatement.setString(1, type);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, attachment);
            preparedStatement.setDate(4, (java.sql.Date) startDate);
            preparedStatement.setDate(5, (java.sql.Date) endDate);

            preparedStatement.executeUpdate();

            System.out.println("Executed Add Special Calendar");
        }
    }
    public static void deactivateSpecialCalendar(int special_calendar_id) {
        String query = "UPDATE special_calendar SET status = 0 WHERE special_calendar_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, special_calendar_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it as needed
        }
    }

    //check if special calendar description already exists
    public static boolean specialCalendarDescriptionExists(String description){
        boolean exists = false;
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM special_calendar WHERE sc_desc = ? AND status = 1")) {

            statement.setString(1, description);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                exists = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}

