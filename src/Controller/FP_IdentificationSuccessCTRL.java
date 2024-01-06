/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Fingerprint.IdentificationModal;
import Fingerprint.VerificationThread;
import Model.Attendance;
import Model.User;
import Utilities.ImageUtil;
import Utilities.SoundUtil;
import Utilities.StringUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    //public volatile boolean isFingerprintMatched;
    IdentificationModal identificationModal = new IdentificationModal();

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

        loadProgressbar();
        addAttendance();
    }


    //simplified method, but time out for lunch break is not enforced, meaning if the user needs to log out first before lunch break, before they can login in the next period, if they miss the lnchbreak time out
    public void addAttendance() {
        int dailyAttendanceLimit = 2;

        // Check if user has already timed in for the current day
        boolean hasTimedIn = Attendance.userHasTimeInToday(userToTime.getId());
        System.out.println("Has timed in: " + hasTimedIn);

        // Check if user has timed out for the current day
        boolean hasTimedOut = Attendance.userHasTimeOutToday(userToTime.getId());
        System.out.println("Has timed out: " + hasTimedOut);

        boolean hasTimedOutAM = Attendance.userHasTimeOutBetween(userToTime.getId(), "00:00", "11:59");

        //create boolean hastTimedOutPM
        boolean hasTimedOutPM = Attendance.userHasTimeOutBetween(userToTime.getId(), "12:00", "23:59");

        boolean hasReachedDailyAttendanceLimit = Attendance.userHasReachedDailyAttendanceLimit(userToTime.getId(), dailyAttendanceLimit);
        // Time in or time out based on user's current status


        //compare two localdates

        if(hasReachedDailyAttendanceLimit){
                SoundUtil.playDenySound();
                attendanceTypeLabel.setText("ATTENDANCE LIMIT REACHED");
                timeLabel.setText("DAILY LIMIT: " + dailyAttendanceLimit);
                dateLabel.setText(LocalDateTime.now().format(dateTimeFormatter));
        }else if (hasTimedIn) {
                timeOutUser(getCurrentTime());
        } else if (hasTimedOutAM && LocalTime.now().isBefore(LocalTime.parse("12:00"))) {
                SoundUtil.playDenySound();
                attendanceTypeLabel.setText("YOU'VE ALREADY TIMED OUT");


                LocalTime currentTime = LocalTime.now();
                // Define 12 noon
                LocalTime noonTime = LocalTime.of(12, 0);
                // Calculate the duration between the current time and 12 noon
                Duration duration = Duration.between(currentTime, noonTime);
                // Extract hours and minutes from the duration
                long hours = duration.toHours();
                long minutes = duration.minusHours(hours).toMinutes();
                String timeLeft = hours+" hours and "+minutes+" minutes";


                timeLabel.setText(timeLeft);
                dateLabel.setText("UNTIL YOUR NEXT TIME IN");
        } else {
            timeInUser(getCurrentTime());
        }
    }


    public void timeInUser(String time) {
        // Insert the time in into the database
        SoundUtil.playSuccessSound();
        Attendance.timeIn(userToTime.getId(), time);

        System.out.println("User timed in at " + time);
        attendanceTypeLabel.setText("Timed In");
        timeLabel.setText(LocalDateTime.now().format(timeFormatter));
        dateLabel.setText(LocalDateTime.now().format(dateTimeFormatter));
    }



//    public void timeOutUser(String time) {
//        SoundUtil.playPromptSound();
//
//        attendanceTypeLabel.setText("SCAN AGAIN TO TIME OUT");
//        timeLabel.setText("");
//        dateLabel.setText("");
//        prevTimeInLabel.setText(Attendance.getHoursSinceLastTimeIn(userToTime.getId()) + " SINCE LAST TIME IN");
//        prevTimeInLabel.setVisible(true);
//
//        CompletableFuture<Void> verificationFuture = CompletableFuture.runAsync(() -> {
//            VerficationThread verificationThread = new VerficationThread(userToTime.getId(), delayTimeInMs);
//            verificationThread.start();
//            try {
//                verificationThread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            if (ThreadFlags.isFingerprintMatched) {
//                Platform.runLater(() -> {
//                    timeOutActionConfirmed(time);
//                });
//            } else {
//                Platform.runLater(() -> {
//                    attendanceTypeLabel.setText("FINGERPRINT DOESN'T MATCH");
//                    SoundUtil.playFailSound();
//                });
//            }
//        });
//
//        // Handle completion asynchronously if needed
//        verificationFuture.thenRun(() -> {
//            // Code to run after verification is complete (if needed)
//        });
//    }

    public void timeOutUser(String time) {
        SoundUtil.playPromptSound();

        attendanceTypeLabel.setText("SCAN AGAIN TO TIME OUT");
        timeLabel.setText("");
        dateLabel.setText("");
        prevTimeInLabel.setText(Attendance.getHoursSinceLastTimeIn(userToTime.getId()) + " SINCE LAST TIME IN");
        prevTimeInLabel.setVisible(true);

        //delaytime is cusing issues currently
        VerificationThread verificationThread = new VerificationThread(userToTime.getId(), delayTimeInMs);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {

            verificationThread.start();
            try {
                verificationThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (verificationThread.userIsVerified) {
                Platform.runLater(() -> {
                    timeOutActionConfirmed(time);
                });
            } else if(verificationThread.isCaptureCanceled){
                Platform.runLater(() -> {
                    SoundUtil.playFailSound();
                });
            }else{
                Platform.runLater(() -> {
                    attendanceTypeLabel.setText("FINGERPRINT DOESN'T MATCH");
                    SoundUtil.playFailSound();
                });
            }
        });
        executor.shutdown();




//        Task<Void> nonUITask = new Task<>() {
//            @Override
//            protected Void call() throws Exception {
//                VerficationThread verificationThread = new VerficationThread(userToTime.getId(), delayTimeInMs);
//                verificationThread.start();
//                try {
//                    verificationThread.join();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//                if (verificationThread.userIsVerified) {
//                    Platform.runLater(() -> {
//                        timeOutActionConfirmed(time);
//                    });
//                } else {
//                    Platform.runLater(() -> {
//                        attendanceTypeLabel.setText("FINGERPRINT DOESN'T MATCH");
//                        SoundUtil.playFailSound();
//                    });
//                }
//                return null;
//            }
//        };
//
//        Thread thread = new Thread(nonUITask);
//        thread.start();




//        CompletableFuture<Void> verificationFuture = CompletableFuture.runAsync(() -> {
//            VerficationThread verificationThread = new VerficationThread(userToTime.getId(), delayTimeInMs);
//            verificationThread.start();
//            try {
//                verificationThread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            if (verificationThread.userIsVerified) {
//                Platform.runLater(() -> {
//                    timeOutActionConfirmed(time);
//                });
//            } else {
//                Platform.runLater(() -> {
//                    attendanceTypeLabel.setText("FINGERPRINT DOESN'T MATCH");
//                    SoundUtil.playFailSound();
//                });
//            }
//        });
    }

    public void timeOutActionConfirmed(String time){
        System.out.println("User timed out at " + time);
        attendanceTypeLabel.setText("Timed Out");
        timeLabel.setText(LocalDateTime.now().format(timeFormatter));
        dateLabel.setText(LocalDateTime.now().format(dateTimeFormatter));

        // Insert the time into the database
        Attendance.timeOut(userToTime.getId(), time);
        SoundUtil.playTimeOutSound();
        //ThreadFlags.isFingerprintMatched = false;
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
