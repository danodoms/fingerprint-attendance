/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getEmpToPieChart;
import Model.User;
import static Model.User.getUserGender;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ADMIN_DashboardCTRL implements Initializable {
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();

   @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private Label displayDateLabel, timeLabel;

    @FXML
    private NumberAxis numberAxis;
    
    @FXML
    private Label femaleCount;

    @FXML
    private Label maleCount;
    
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       ObservableList<User> userList = getUserGender();
       AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd");
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
                    displayDateLabel.setText(LocalDateTime.now().format(formatter));
                    timeLabel.setText(LocalDateTime.now().format(formatter1));
                });
            }
        };
        timer.start();
       
       
    // Populate the bar chart
    populateBarChart(userList);
    schedulePieChartRefresh();
//    populatePieChart();
}

    private void populateBarChart(ObservableList<User> userList) {
        // Clear existing data in the chart
        barChart.getData().clear();

        // Create series for the chart
        BarChart.Series<String, Integer> series = new BarChart.Series<>();
        BarChart.Series<String, Integer> series1 = new BarChart.Series<>();

        // Add data to series
        for (User user : userList) {
            if(user.getSex().equals("Female")){
                series.getData().add(new BarChart.Data<>(user.getSex(), user.getCount()));
                femaleCount.setText(user.getCount()+"");
            }else{
                series1.getData().add(new BarChart.Data<>(user.getSex(), user.getCount()));
                maleCount.setText(user.getCount()+"");
            }
        }
        barChart.getData().addAll(series, series1);
    }

//     private void populatePieChart() {
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
//            
//            for (Attendance attendance : getEmpToPieChart()) {
//            pieChartData.add(new PieChart.Data("Logged In", attendance.getPercentageLoggedIn()));
//            pieChartData.add(new PieChart.Data("Not Logged In", attendance.getPercentageNotLoggedIn()));
//                }
//            pieChart.setData(pieChartData);
//     }
    private void schedulePieChartRefresh() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the refresh every 10 seconds, adjust the interval as needed
        scheduler.scheduleAtFixedRate(this::populatePieChart, 0, 10, TimeUnit.SECONDS);
    }

    private void populatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Attendance attendance : getEmpToPieChart()) {
            pieChartData.add(new PieChart.Data("Logged In", attendance.getPercentageLoggedIn()));
            pieChartData.add(new PieChart.Data("Not Logged In", attendance.getPercentageNotLoggedIn()));
        }

        // Update the UI in the JavaFX Application Thread
        Platform.runLater(() -> pieChart.setData(pieChartData));
    }
}
