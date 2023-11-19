/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author User
 */
public class Attendance {
    private Date date;
    private String attendance_status;
    private String timeIn;
    private String timeOut;
    private String name;
    private String deptName;
    private String notation;
    public static String defaultValue = "All";
    
    public Attendance(String name){
        this.name = name;
    }
    public Attendance(Date date){
        this.date=date;
    }
     public Attendance (String name, Date date, String timeIn, String notation){
        this.name = name;
        this.date = date;
        this.timeIn = timeIn;
        this.notation = notation;
    }
    public Attendance (String name, Date date, String timeIn, String timeOut, String notation, String attendance_status){
        this.name = name;
        this.date = date;
        this.attendance_status = attendance_status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.notation = notation;
    }
      public String getName() {
        return name;
      }
      public String getNotation() {
        return notation;
    }
       public String getDeptName() {
        return deptName;
    }
     public Date getDate() {
        return date;
    }
     public String getAttendance_status() {
        return attendance_status;
    }
     public String getTimeIn() {
        return timeIn;
    }
     public String getTimeOut() {
        return timeOut;
    }
    
     
     
public void setName(String name) {
    this.name = name;
}

public void setNotation(String notation) {
    this.notation = notation;
}

public void setDeptName(String deptName) {
    this.deptName = deptName;
}

public void setDate(Date date) {
    this.date = date;
}

public void setAttendance_status(String attendance_status) {
    this.attendance_status = attendance_status;
}

public void setTimeIn(String timeIn) {
    this.timeIn = timeIn;
}

public void setTimeOut(String timeOut) {
    this.timeOut = timeOut;
}   
    public static ObservableList<Attendance> getYearforLabel(){
        ObservableList<Attendance>empName = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT date FROM attendance "+
            "GROUP BY YEAR(date);");
            
            while (rs.next()) {
                empName.add(new Attendance(
                        rs.getDate("date")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empName;
    }
     public static ObservableList<Attendance> getEmpName(){
        ObservableList<Attendance>empName = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(user_fname, ' ', user_lname) AS name FROM user "+
            "WHERE user_status = 1 GROUP BY user_id ORDER BY user_fname;");
            while (rs.next()) {
               
                empName.add(new Attendance(
                    rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empName;
    }

        public static ObservableList<Attendance> getAttendance(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(u.user_fname, ' ', u.user_lname) AS name, c.date, "
            + "c.time_in as timeIn, c.time_out as timeOut, c.time_notation as notation, c.attendance_status \n" +
            "FROM attendance c \n" +
            "JOIN user u ON c.user_id = u.user_id "+
            "JOIN assignment a ON u.user_id = a.user_id " +
            "JOIN position p ON a.position_id = p.position_id " +
            "JOIN department d ON p.department_id = d.department_id " +
            "WHERE u.user_status = 1 GROUP BY c.attendance_id ORDER BY c.attendance_id;");
            //d.department_id = 1 && 
            while (rs.next()) {
                int statusInt = rs.getInt("attendance_status");
                String in= rs.getString("timeIn");
                String out= rs.getString("timeOut");
                String notationPM = rs.getString("notation");
                String statusString;
                    if(notationPM.equals("PM")){
                        String[] splitIn = in.split(":");
                        String[] splitOut = out.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        int convertOut = Integer.parseInt(splitOut[0]);
                        if(convertIn >= 13){
                            convertIn = convertIn - 12;
                            in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                        }if(convertOut >= 13){
                            convertOut = convertOut - 12;
                            out = "0"+(convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                        }
                        
                    }
                   if(out.equals("00:00:00")){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "   ♥";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Unknown";
                }

                attendance.add(new Attendance(
                    rs.getString("name"),
                    rs.getDate("date"),
                  in,
                 out,
                rs.getString("notation"), 
         statusString
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    }
        
         public static ObservableList<Attendance> getAdministrative(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(u.user_fname, ' ', u.user_lname) AS name, c.date, "
            + "c.time_in as timeIn, c.time_out as timeOut, c.time_notation as notation, c.attendance_status \n" +
            "FROM attendance c \n" +
            "JOIN user u ON c.user_id = u.user_id "+
            "JOIN assignment a ON u.user_id = a.user_id " +
            "JOIN position p ON a.position_id = p.position_id " +
            "JOIN department d ON p.department_id = d.department_id " +
            "WHERE d.department_id = 1 && u.user_status = 1 GROUP BY c.attendance_id ORDER BY c.attendance_id;");
            //d.department_id = 1 && 
            while (rs.next()) {
                int statusInt = rs.getInt("attendance_status");
                String in= rs.getString("timeIn");
                String out= rs.getString("timeOut");
                String notationPM = rs.getString("notation");
                String statusString;
                    if(notationPM.equals("PM")){
                        String[] splitIn = in.split(":");
                        String[] splitOut = out.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        int convertOut = Integer.parseInt(splitOut[0]);
                        if(convertIn >= 13){
                            convertIn = convertIn - 12;
                            in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                        }if(convertOut >= 13){
                            convertOut = convertOut - 12;
                            out = "0"+(convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                        }
                        
                    }

                   if(out.equals("00:00:00")){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "   ♥";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Unknown";
                }

                attendance.add(new Attendance(
                    rs.getString("name"),
                    rs.getDate("date"),
                  in,
                 out,
                rs.getString("notation"),
         statusString
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    } 
         
         public static ObservableList<Attendance> getInstruction(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(u.user_fname, ' ', u.user_lname) AS name, c.date, "
            + "c.time_in as timeIn, c.time_out as timeOut, c.time_notation as notation, c.attendance_status \n" +
            "FROM attendance c \n" +
            "JOIN user u ON c.user_id = u.user_id "+
            "JOIN assignment a ON u.user_id = a.user_id " +
            "JOIN position p ON a.position_id = p.position_id " +
            "JOIN department d ON p.department_id = d.department_id " +
            "WHERE d.department_id = 2 && u.user_status = 1 GROUP BY c.attendance_id ORDER BY c.attendance_id;");
            //d.department_id = 1 && 
            while (rs.next()) {
                int statusInt = rs.getInt("attendance_status");
                String in= rs.getString("timeIn");
                String out= rs.getString("timeOut");
                String notationPM = rs.getString("notation");
                String statusString;
                    if(notationPM.equals("PM")){
                        String[] splitIn = in.split(":");
                        String[] splitOut = out.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        int convertOut = Integer.parseInt(splitOut[0]);
                        if(convertIn >= 13){
                            convertIn = convertIn - 12;
                            in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                        }if(convertOut >= 13){
                            convertOut = convertOut - 12;
                            out = "0"+(convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                        }
                        
                    }
                   if(out.equals("00:00:00")){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "   ♥";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Unknown";
                }

                attendance.add(new Attendance(
                    rs.getString("name"),
                    rs.getDate("date"),
                  in,
                 out, 
            rs.getString("notation"),
         statusString
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    }
        
        public static ObservableList<Attendance> getAttendancebyDate(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(u.user_fname, ' ', u.user_lname) AS name, c.date, "
            + "c.time_in as timeIn, c.time_out as timeOut, c.time_notation as notation, c.attendance_status \n" +
            "FROM attendance c \n" +
            "JOIN user u ON c.user_id = u.user_id "+
            "JOIN assignment a ON u.user_id = a.user_id " +
            "JOIN position p ON a.position_id = p.position_id " +
            "JOIN department d ON p.department_id = d.department_id " +
            "WHERE u.user_status = 1 GROUP BY c.attendance_id ORDER BY c.attendance_id;");
            
            while (rs.next()) {
                int statusInt = rs.getInt("attendance_status");
                String in= rs.getString("timeIn");
                String out= rs.getString("timeOut");
                String notationPM = rs.getString("notation");
                String statusString;
                    if(notationPM.equals("PM")){
                        String[] splitIn = in.split(":");
                        String[] splitOut = out.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        int convertOut = Integer.parseInt(splitOut[0]);
                        if(convertIn >= 13){
                            convertIn = convertIn - 12;
                            if(convertIn>9){
                                in = (convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }else{
                                in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }
                        }if(convertOut >= 13){
                            convertOut = convertOut - 12;
                            if(convertOut>9){
                                out = (convertOut+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }else{
                                out = "0"+(convertOut+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }
                        }
                        
                    }
                   if(out.equals("00:00:00")){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "  √";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Overtime";
                }

                attendance.add(new Attendance(
                    rs.getString("name"),
                    rs.getDate("date"),
                  in,
                 out, 
                 rs.getString("notation"),
         statusString
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    }
        public static ObservableList<Attendance> getAttendancebyLate(){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            ResultSet rs = statement.executeQuery("SELECT CONCAT(u.user_fname, ' ', u.user_lname) AS name, c.date,\n" +
"            c.time_in as timeIn, c.time_out as timeOut, c.time_notation as notation,\n" +
"            c.attendance_status \n" +
"            FROM attendance c \n" +
"            JOIN user u ON c.user_id = u.user_id \n" +
"            JOIN assignment a ON u.user_id = a.user_id \n" +
"            JOIN position p ON a.position_id = p.position_id\n" +
"            JOIN department d ON p.department_id = d.department_id \n" +
"            WHERE u.user_status = 1 AND c.attendance_status = 2 \n" +
"            GROUP BY c.attendance_id ORDER BY c.attendance_id;");
            
            while (rs.next()) {
                int statusInt = rs.getInt("attendance_status");
                String in= rs.getString("timeIn");
                String notationPM = rs.getString("notation");
                String statusString;
                    if(notationPM.equals("PM")){
                        String[] splitIn = in.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        if(convertIn >= 13){
                            convertIn = convertIn - 12;
                            if(convertIn>9){
                                in = (convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }else{
                                in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }
                        }
                    }
                attendance.add(new Attendance(
                    rs.getString("name"),
                    rs.getDate("date"),
                  in,
                 rs.getString("notation")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendance;
    }
}
