/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.Attendance;
import Model.User;
import Session.Session;
import Utilities.DatabaseUtil;
import Utilities.ImageUtil;
import Utilities.PaneUtil;
import com.dlsc.gemsfx.DrawerStackPane;
import com.dlsc.gemsfx.infocenter.InfoCenterPane;
import com.dlsc.gemsfx.infocenter.InfoCenterView;
import com.dlsc.gemsfx.infocenter.Notification;
import com.dlsc.gemsfx.infocenter.Notification.OnClickBehaviour;
import com.dlsc.gemsfx.infocenter.NotificationAction;
import com.dlsc.gemsfx.infocenter.NotificationGroup;
import com.dlsc.gemsfx.infocenter.NotificationView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZonedDateTime;
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
    private Pane piePane21111;
    @FXML
    private Label timeInCountLabel;
    @FXML
    private Pane piePane211111;
    @FXML
    private Label timeOutCountLabel;
    @FXML
    private Pane piePane211112;
    @FXML
    private Pane piePane2111121;
    @FXML
    private Pane piePane2111122;
    @FXML
    private Pane piePane21111221;
    @FXML
    private Label attendancePercentLabel;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label userNameLabel;
    @FXML
    private Button showPendingActionsBtn;
    @FXML
    private DrawerStackPane drawerStackPane;
    @FXML
    private Button sampleBtn;

    private InfoCenterPane infoCenterPane = new InfoCenterPane();
    private NotificationGroup<Employee, EmployeeNotification> employeeGroup = new NotificationGroup<>("Employee");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeGroup.setViewFactory(n -> {
            NotificationView<Employee, EmployeeNotification> view = new NotificationView<>(n);
            //view.setGraphic(createImageView(new Image(InfoCenterApp.class.getResourceAsStream("notification/mail.png"))));
            return view;
        });


        InfoCenterView infoCenterView = infoCenterPane.getInfoCenterView();
        infoCenterView.getGroups().setAll(employeeGroup);
        infoCenterView.setAutoOpenGroup(true);
        infoCenterView.setShowAllGroup(employeeGroup);

        Notification notification = new EmployeeNotification(new Employee("Sample Notification", "This is a sample notification.", ZonedDateTime.now()));
        employeeGroup.getNotifications().add((EmployeeNotification) notification);
        infoCenterPane.setShowInfoCenter(true);







        //DRAWER STACK PANE INITIALIZATION
        drawerStackPane.setVisible(false);
        drawerStackPane.onDrawerCloseProperty().set(() -> {
            drawerStackPane.setVisible(false);
        });

        drawerStackPane.setDrawerContent(infoCenterPane);





        //LOGIN DETAILS
        if(Session.getInstance().getLoggedInUser() == null){
            System.out.println("No user logged in");
        }else{
            userNameLabel.setText(Session.getInstance().getLoggedInUser().getFullNameWithInitial());
            userImageView.setImage(ImageUtil.byteArrayToImage(Session.getInstance().getLoggedInUser().getImage()));
        }



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


        loadMetrics();


       
    // Populate the bar chart
    populateBarChart(userList);
    schedulePieChartRefresh();
//    populatePieChart();
}

    private class Employee {

        private String title;
        private String description;
        private ZonedDateTime dateTime;

        public Employee(String title, String description, ZonedDateTime dateTime) {
            this.title = title;
            this.description = description;
            this.dateTime = dateTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ZonedDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }

    private class EmployeeNotification extends Notification<Employee> {
        public EmployeeNotification(Employee employee) {
            super(employee.getTitle(), employee.getDescription(), employee.getDateTime());
            setUserObject(employee);
            setOnClick(notification -> OnClickBehaviour.NONE);
        }
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





    private void loadMetrics(){
        new Thread(() -> {
            timeInCountLabel.setText(Attendance.getTimeInCountToday()+"");
            timeOutCountLabel.setText(Attendance.getTimeOutCountToday()+"");
            loadRecentAttendanceTable();
        }).start();
    }

    private void schedulePieChartRefresh() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Schedule the refresh every 10 seconds, adjust the interval as needed
        scheduler.scheduleAtFixedRate(this::populatePieChart, 0, 30, TimeUnit.SECONDS);
    }



    private void populatePieChart() {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Attendance attendance : getEmpToPieChart()) {
            double percentage = attendance.getPercentageLoggedIn();
            int roundedValue = (int) Math.round(percentage);
            attendancePercentLabel.setText(roundedValue+"%");

            pieChartData.add(new PieChart.Data("Logged In", attendance.getPercentageLoggedIn()));
            pieChartData.add(new PieChart.Data("Not Logged In", attendance.getPercentageNotLoggedIn()));
        }

        // Update the UI in the JavaFX Application Thread
        Platform.runLater(() -> pieChart.setData(pieChartData));
    }

    @FXML
    public void showDrawerStackPane(ActionEvent actionEvent) {
        drawerStackPane.setVisible(true);
        drawerStackPane.setShowDrawer(true);
    }
}
