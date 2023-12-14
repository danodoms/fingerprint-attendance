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
public class Special_Calendar {
    private int id;
    private String type;
    private String description;
    private Date startDate;
    private Date endDate;
    private int total;
    
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
     public Special_Calendar(Date startDate,int total){
         this.startDate = startDate;
         this.total = total;
     }

     public static ObservableList<Special_Calendar> getCalendarByUserId(int user_id) {
    ObservableList<Special_Calendar> calendar = FXCollections.observableArrayList();
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM `special_calendar_view` WHERE id = ?")) {

        statement.setInt(1, user_id); // Set the user_id in the PreparedStatement.

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
//    Date startDate = rs.getDate("startDate");
//    Date endDate = rs.getDate("endDate");
//    
//    // Splitting startDate components into an int array.
//    int[] startArray = splitDateComponents(startDate);
//    
//    // Splitting endDate components into an int array.
//    int[] endArray = splitDateComponents(endDate);
//    
//    // Calculate the total taking into account year, month, and day differences.
//    int total = calculateTotal(startArray, endArray);

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
//    private static int[] splitDateComponents(Date date) {
//    // Convert java.sql.Date to java.util.Date.
//    java.util.Date utilDate = new java.util.Date(date.getTime());
//
//    LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    int[] dateArray = {localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()};
//    return dateArray;
//}
//
//     private static int calculateTotal(int[] startArray, int[] endArray) {
//    LocalDate startDate = LocalDate.of(startArray[0], startArray[1], startArray[2]);
//    LocalDate endDate = LocalDate.of(endArray[0], endArray[1], endArray[2]);
//
//    // Calculate the total taking into account year, month, and day differences using ChronoUnit.
//    return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
//}

}

