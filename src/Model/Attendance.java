/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Utilities.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author User
 */
public class Attendance {
    private int id;
    private Date date;
    private String attendance_status;
    private String timeIn;
    private String timeOut;
    private String timeInAm,timeOutAm;
    private String timeInPm,timeOutPm;
    private String name;
    private String notationAM;
    private String notationPM;
    private String deptName;
    private String notation;
    public static String defaultValue = "All";
    private int totalEmployees;
    private double percentageLoggedIn;
    private double percentageNotLoggedIn;
    
    public Attendance(int totalEmployees, double percentageLoggedIn,double percentageNotLoggedIn){
        this.totalEmployees = totalEmployees;
        this.percentageLoggedIn=percentageLoggedIn;
        this.percentageNotLoggedIn=percentageNotLoggedIn;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public double getPercentageLoggedIn() {
        return percentageLoggedIn;
    }

    public double getPercentageNotLoggedIn() {
        return percentageNotLoggedIn;
    }

    public void setPercentageLoggedIn(double percentageLoggedIn) {
        this.percentageLoggedIn = percentageLoggedIn;
    }

    public void setPercentageNotLoggedIn(double percentageNotLoggedIn) {
        this.percentageNotLoggedIn = percentageNotLoggedIn;
    }

    public Attendance(int id, Date date, String name, String timeIn, String notation) {
        this.id=id;
        this.date = date;
        this.timeIn = timeIn;
        this.name = name;
        this.notation = notation;
    }
    
    public Attendance(String name){
        this.name = name;
    }
    public Attendance(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Attendance(Date date){
        this.date=date;
    }
     public Attendance (String name, Date date, String timeInAm, String timeOutAm, String timeInPm, String timeOutPm, String attendance_status){
        this.name = name;
        this.date = date;
        this.attendance_status = attendance_status;
        this.timeInAm = timeInAm;
        this.timeOutAm = timeOutAm;
        this.timeInPm = timeInPm;
        this.timeOutPm = timeOutPm;
    }

    public void setNotationAM(String notationAM) {
        this.notationAM = notationAM;
    }

    public void setNotationPM(String notationPM) {
        this.notationPM = notationPM;
    }

    public String getNotationAM() {
        return notationAM;
    }

    public String getNotationPM() {
        return notationPM;
    }
    public Attendance (String name, Date date, String timeIn, String timeOut, String notation, String attendance_status){
        this.name = name;
        this.date = date;
        this.attendance_status = attendance_status;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.notation = notation;
    }
    public int getId() {
        return id;
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
     public String getTimeInAm() {
        return timeInAm;
    }
     public String getTimeOutAm() {
        return timeOutAm;
    }
     public String getTimeInPm() {
        return timeInPm;
    }
     public String getTimeOutPm() {
        return timeOutPm;
    }
    
     
public void setId(int id) {
    this.id = id;
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
public void setTimeInAm(String timeInAm) {
    this.timeInAm = timeInAm;
}

public void setTimeOutAm(String timeOutAm) {
    this.timeOutAm = timeOutAm;
}   
public void setTimeInPm(String timeInPm) {
    this.timeInPm = timeInPm;
}

public void setTimeOutPm(String timeOutPm) {
    this.timeOutPm = timeOutPm;
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
            
            ResultSet rs = statement.executeQuery("SELECT user_id as id, CONCAT(user_fname, ' ', user_lname) AS name FROM user "+
            "WHERE user_status = 1 GROUP BY user_id ORDER BY id;");
            while (rs.next()) {
               
                empName.add(new Attendance(
                        rs.getInt("id"),
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
                   if(out == null){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "   ✓";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Present";
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
                    statusString = "   ✓";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Present";
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
         
         public static ObservableList<Attendance> getEmpToPieChart(){
            ObservableList<Attendance> attendance = FXCollections.observableArrayList();
            try (Connection connection = DatabaseUtil.getConnection();
                Statement statement = connection.createStatement()){
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ResultSet rs = statement.executeQuery("SELECT total_employees as count, percentage_logged_in as logged, percentage_not_logged_in as notLogged  FROM employee_status_view;");
                
                while (rs.next()){
                   
                            attendance.add(new Attendance(
                        rs.getInt("count"),
                        rs.getDouble("logged"),
                        rs.getDouble("notLogged")
                         ));
                }
            }catch (SQLException e) {
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
                       out = " -";
                   }

                if (statusInt == 1) {
                    statusString = "   ✓";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "No Out";
                } else {
                    statusString = "Present";
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
                                out = (convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }else{
                                out = "0"+(convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }
                        }
                        
                    }
                   if(out.equals("00:00:00")){
                       out = " ";
                   }

                if (statusInt == 1) {
                    statusString = "  ✓";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "Undertime";
                } else {
                    statusString = "No Out";
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
        //Note: Sabta nlng gyud akong mga comments. THis is for remembering lang fo.
        public static ObservableList<Attendance> getAttendancebyLate(int user_id){
        ObservableList<Attendance> attendance = FXCollections.observableArrayList();
        try (Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement()){
            
            String query =("SELECT date, CONCAT(firstname,' ', lastname) AS name, timeInAm, timeOutAm, timeInPm, timeOutPm, status FROM dtr WHERE user_id=?;");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            
            ResultSet rs = preparedStatement.executeQuery();
        
            // Iteration huhu -_- Sukol!! 
               while (rs.next()) {
                String name = rs.getString("name");
                String timeInAm1 = rs.getString("timeInAm") ;
                String timeOutAm1 = rs.getString("timeOutAm");
                String in = rs.getString("timeInPm") ;
                String out = rs.getString("timeOutPm") ;
                int statusInt = rs.getInt("status");
                String rs1Date = rs.getDate("date").toString();
                
                // timeIn convertion from 24 hours to 12 hours.
                if(timeOutAm1!=null){
                    String[] splitOut = timeInAm1.split(":");
                        int convertIn = Integer.parseInt(splitOut[0]);
                        if(convertIn > 12){
                            convertIn = convertIn - 12;
                            if(convertIn>9){
                                timeOutAm1 = (convertIn+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }else{
                                timeOutAm1 = "0"+(convertIn+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }
                        }
                }
                else{
                   timeOutAm1 = "    --";
               }
                if(timeInAm1==null){
                   timeInAm1="    --";
               }
               if(in!=null){
                        String[] splitIn = in.split(":");
                        int convertIn = Integer.parseInt(splitIn[0]);
                        if(convertIn > 12){
                            convertIn = convertIn - 12;
                            if(convertIn>9){
                                in = (convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }else{
                                in = "0"+(convertIn+"")+ ":"+splitIn[1]+":"+splitIn[2];
                            }
                        }
               }
               else{
                   in = "    --";
               }
                if(out!=null){
                        String[] splitOut = out.split(":");
                        int convertOut = Integer.parseInt(splitOut[0]);
                        if(convertOut > 12){
                            convertOut = convertOut - 12;
                            if(convertOut>9){
                                out = (convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }else{
                                out = "0"+(convertOut+"")+ ":"+splitOut[1]+":"+splitOut[2];
                            }
                        }
                }
                else{
                   out = "    --";
               }
                
                String statusString;
                if (statusInt == 1) {
                    statusString = "Present";
                } else if (statusInt == 2) {
                    statusString = "Late";
                } else if (statusInt == 3) {
                    statusString = "Undertime";
                } else {
                    statusString = "No Out";
                }

                attendance.add(new Attendance(
                    name,
                    rs.getDate("date"),
                    timeInAm1,
                    timeOutAm1,
                    in,
                    out,
                    statusString
                ));
            } 

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        for (Attendance record : attendance) {
//            System.out.println("Employee Name: " + record.getName());
//            System.out.println("Date: " + record.getDate());
//            System.out.println("Time In (AM): " + record.getTimeInAm());
//            System.out.println("Time Out (AM): " + record.getTimeOutAm());
//            System.out.println("Time In (PM): " + record.getTimeInPm());
//            System.out.println("Time Out (PM): " + record.getTimeOutPm());
//            System.out.println("Attendance Status: " + record.getAttendance_status());
//
//            // Add a separator for better readability
//            System.out.println("----------------------------");
//        }
        return attendance;
    }




    public static void timeIn(int userId, String time){
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()){

            String query = "INSERT INTO attendance (user_id, date, time_in) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setString(3, time);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //create timeOut method where it  date is equal to today, user_id is equal to the given user_id and time_out is null
    public static void timeOut(int userId, String time) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE attendance SET time_out = ? WHERE user_id = ? AND date = ? AND time_out IS NULL")) {
            statement.setString(1, time);
            statement.setInt(2, userId);
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userHasTimeInToday(int userId) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     //"SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL ORDER by time_in desc limit 1")) {
                     "SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL AND time_out IS NULL;")){
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0; // Check if there is at least one record
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume no time-in record on error
        }
    }

    public static boolean userHasTimeOutBetween(int userId, String startTime, String endTime) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     //"SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL ORDER by time_in desc limit 1")) {
                     "SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_out BETWEEN ? AND ?")){
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            statement.setString(3, startTime);
            statement.setString(4, endTime);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0; // Check if there is at least one record

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume no time-in record on error
        }
    }

    public static boolean userHasTimeOutToday(int userId) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_out IS NOT NULL AND time_in IS NOT NULL")) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0; // Check if there is at least one record
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume no time-out record on error
        }
    }

    public static boolean userHasReachedDailyAttendanceLimit(int userId, int attendanceLimit) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL AND time_out IS NOT NULL")) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) >= attendanceLimit; // Check if there is at least one record
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Assume no time-out record on error
        }
    }

    public static String getHoursSinceLastTimeIn(int userId){
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     //"SELECT COUNT(*) FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL ORDER by time_in desc limit 1")) {
                     "SELECT time_in FROM attendance WHERE user_id = ? AND date = ? AND time_in IS NOT NULL AND time_out IS NULL;")){
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();


            LocalTime earlierTime = LocalTime.parse(resultSet.getString("time_in"));
            LocalTime laterTime = LocalTime.now();

            // Calculate the time difference
            long hours = ChronoUnit.HOURS.between(earlierTime, laterTime);
            long minutes = ChronoUnit.MINUTES.between(earlierTime, laterTime) % 60;


            return hours + " HOUR(S) AND " + minutes + " MINUTES";
        } catch (SQLException e) {
            e.printStackTrace();
            return "DATABASE ERROR: No time-in record found";
        }

    }





}
