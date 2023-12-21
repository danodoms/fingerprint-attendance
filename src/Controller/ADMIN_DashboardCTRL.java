/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Attendance;
import Model.User;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static Model.Attendance.getEmpToPieChart;
import static Model.User.getUserGender;

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
    private NumberAxis numberAxis;
    
    @FXML
    private Label femaleCount;

    @FXML
    private Label maleCount;
    
    @FXML
    private PieChart pieChart;
    @FXML
    private Pane piePane;
    @FXML
    private ImageView fView;
    @FXML
    private ImageView mView;
    @FXML
    private Pane piePane1;
    @FXML
    private TableView recentAttendanceTable;
    @FXML
    private TableColumn col_name;
    @FXML
    private TableColumn col_time;
    @FXML
    private TableColumn col_type;
    @FXML
    private Pane piePane2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        //RECENT ATTENDANCE TABLE
        col_name.setCellValueFactory(new PropertyValueFactory<Attendance, String>("name"));
        col_time.setCellValueFactory(new PropertyValueFactory<Attendance, String>("time"));
        col_type.setCellValueFactory(new PropertyValueFactory<Attendance, String>("type"));

       
       ObservableList<User> userList = getUserGender();
       AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd");
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
                    //displayDateLabel.setText(LocalDateTime.now().format(formatter));
                    //timeLabel.setText(LocalDateTime.now().format(formatter1));
                });
            }
        };
        timer.start();

        //run this on a separate thread
        new Thread(() -> {
            loadRecentAttendanceTable();
        }).start();

       
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
            if(user.getSex() == null){
                System.out.println("Undefined Sex");
            }
            else if(user.getSex().equals("Female")){
                series.getData().add(new BarChart.Data<>(user.getSex(), user.getCount()));
                femaleCount.setText(user.getCount()+"");
            }else{
                series1.getData().add(new BarChart.Data<>(user.getSex(), user.getCount()));
                maleCount.setText(user.getCount()+"");
            }
        }
        barChart.getData().addAll(series, series1);
    }

    private void loadRecentAttendanceTable(){
        ObservableList<Attendance> attendanceList = Attendance.getRecentAttendance();
        recentAttendanceTable.setItems(attendanceList);
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
