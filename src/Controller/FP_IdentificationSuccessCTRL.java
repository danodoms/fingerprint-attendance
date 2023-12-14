/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Attendance;
import Model.User;
import Utilities.ImageUtil;
import Utilities.StringUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class FP_IdentificationSuccessCTRL implements Initializable {

    @FXML
    private ImageView userImageView;
    @FXML
    private ProgressBar progressBar;


    int delayTimeInMs = 0;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label attendanceTypeLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label nameMnameLabel;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label prevTimeInLabel;

    User userToTime;
    int getDelayTimeInMs;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }    
    
    
     public void setUserData(int delayTimeInMs, User user) {
        userToTime = user;
        this.delayTimeInMs = delayTimeInMs;
        loadUserData();
    }

    public void loadUserData(){
        //load image
        Image userImage = ImageUtil.byteArrayToImage(userToTime.getImage());
        userImageView.setImage(userImage);

        //load greeting label
        greetingLabel.setText(StringUtil.getGreeting());

        //load name labels
        String fname = userToTime.getFname();
        String mname = userToTime.getMname();
        String lname = userToTime.getLname();
        String suffix = userToTime.getSuffix();
        String[] formattedName = StringUtil.formatFullName(fname, mname, lname, suffix);

        surnameLabel.setText(formattedName[0]);
        nameMnameLabel.setText(formattedName[1]);

        prevTimeInLabel.setVisible(false);

        addAttendance();

        loadProgressbar();
    }


    //Version 1
//    public void addAttendance() {
//        //MORNING
//        if (isTimeWithinRange(getCurrentTime(), "00:00", "11:59")) {
//            if (userHasTimeIn("00:00", "11:59") || userHasTimeIn("12:00", "23:59")){
//                //PROMPT USER TO SCAN FINGERPRINT AGAIN TO TIMEOUT
//                timeOutUser_PM();
//            } else {
//                timeInUser_AM();
//            }
//            //LUNCH BREAK
//        } else if (isTimeWithinRange(getCurrentTime(), "12:00", "23:59")) {
//            if (userHasTimeIn("00:00", "11:59") && userHasTimeOut("00:00", "11:59")) {
//                timeInUser_PM();
//            } else {
//                if (userHasTimeIn("00:00", "11:59")) {
//                    timeOutUser_PM();
//                } else {
//                    timInUser_PM
//                }
//            }
//            //AFTERNOON TO MIDNIGHT
//        } else if (isTimeWithinRange(getCurrentTime(), "12:00", "23:59")) {
//            if (userHasTimeIn("12:00", "23:59")) {
//                //PROMPT USER TO SCAN FINGERPRINT AGAIN TO TIMEOUT
//                timeOutUser_PM();
//            } else {
//                timeInUser_PM();
//            }
//        }
//    }


    //PERFECT ALGORITHM, but for day shift only, fixed format
//    public void addAttendance() {
//        // Check if user has already timed in for the current period
//        boolean hasTimedIn = userHasTimeIn(getCurrentPeriod());
//
//        // Check if user has timed out for the previous period
//        boolean hasTimedOutPrevious = userHasTimeOut(getPreviousPeriod());
//
//        // Morning
//        if (isTimeWithinRange(getCurrentTime(), "00:00", "11:59")) {
//            if (hasTimedIn) {
//                // Prompt user to scan fingerprint again to timeout
//                timeOutUser(getCurrentPeriod());
//            } else {
//                timeInUser(getCurrentPeriod());
//            }
//        } else if (isTimeWithinRange(getCurrentTime(), "12:00", "23:59")) {
//            if (hasTimedIn) {
//                // Prompt user to scan fingerprint again to timeout
//                timeOutUser(getCurrentPeriod());
//            } else if (!hasTimedOutPrevious) {
//                // User has not timed out for previous period, so prompt them to time out
//                timeOutUser(getPreviousPeriod());
//            } else {
//                timeInUser(getCurrentPeriod());
//            }
//        }
//    }


    //simplified method, but time out for lunch break is not enforced, meaning if the user needs to log out first before lunch break, before they can login in the next period, if they miss the lnchbreak time out
    public void addAttendance() {
        // Check if user has already timed in for the current day
        boolean hasTimedIn = Attendance.userHasTimeInToday(userToTime.getId());
        System.out.println("Has timed in: " + hasTimedIn);

        // Check if user has timed out for the current day
        boolean hasTimedOut = Attendance.userHasTimeOutToday(userToTime.getId());
        System.out.println("Has timed out: " + hasTimedOut);

        // Time in or time out based on user's current status
        if (hasTimedIn && !hasTimedOut) {
            timeOutUser(getCurrentTime());
        } else {
            timeInUser(getCurrentTime());
        }
    }


    public void timeInUser(String time) {
        // Insert the time in into the database
        Attendance.timeIn(userToTime.getId(), time);

        System.out.println("User timed in at " + time);
        attendanceTypeLabel.setText("Timed In");
        timeLabel.setText(LocalDateTime.now().format(timeFormatter));
        dateLabel.setText(LocalDateTime.now().format(dateTimeFormatter));
    }

    public void timeOutUser(String time) {


        System.out.println("User timed out at " + time);
        attendanceTypeLabel.setText("Timed Out");
        timeLabel.setText(LocalDateTime.now().format(timeFormatter));
        dateLabel.setText(LocalDateTime.now().format(dateTimeFormatter));
        prevTimeInLabel.setText(Attendance.getHoursSinceLastTimeIn(userToTime.getId())+" SINCE LAST TIME IN");
        prevTimeInLabel.setVisible(true);

        // Insert the time in into the database
        Attendance.timeOut(userToTime.getId(), time);
    }








    public static boolean isTimeWithinRange(String timeToCheck, String rangeStart, String rangeEnd) {
        // Parse the input strings to LocalTime objects
        LocalTime time = LocalTime.parse(timeToCheck);
        LocalTime start = LocalTime.parse(rangeStart);
        LocalTime end = LocalTime.parse(rangeEnd);

        // Check if the given time is within the range
        return !time.isBefore(start) && !time.isAfter(end);
    }

    public static String getCurrentTime(){
        LocalTime time = LocalTime.now();
        return time.toString();
    }

    //make the progress bar decrease automatically in 3 seconds from 100 to 0 percent
    public void loadProgressbar(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 100; i >= 0; i--) {
                    updateProgress(i, 100);
                    Thread.sleep(delayTimeInMs/100); // Simulate some work being done
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


}
