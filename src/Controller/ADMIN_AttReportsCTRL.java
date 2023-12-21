/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Attendance;
import static Model.Attendance.getAttendancebyLate;
import static Model.Attendance.getEmpName;
import static Model.Attendance.getYearforLabel;
import Model.Special_Calendar;
import static Model.Special_Calendar.getCalendarByUserId;
import Model.User;
import java.time.YearMonth;
import Utilities.DatabaseUtil;
import Utilities.PaneUtil;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author User
 */
public class ADMIN_AttReportsCTRL implements Initializable{
    @FXML
    private TableView<Attendance> empNameTable, tardinessTable;
    @FXML
    private TableColumn<Attendance, String> empName, tiAM, toAM, tiPM, toPM, remCol, user_id;
    @FXML
    private TableColumn<Attendance, Date> dateCol;
    @FXML
    private Label monthYearLabel, nameLabel, dateTimeLabel, totalLogin, totalAbsent, byPercent, totalLate, totalTardiness;
    @FXML
    private ChoiceBox <String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private Button resetBtn, selectBtn, lateBtn, absentBtn, tardBtn, oneDTRBtn, allDTRBtn;
    
    
    DatabaseUtil dbMethods = new DatabaseUtil();
    PaneUtil method = new PaneUtil();
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    Month currentMonth = currentDate.getMonth();
    int currentDay = currentDate.getDayOfMonth();
    String capitalizedMonth = currentMonth.toString().substring(0, 1) + currentMonth.toString().substring(1).toLowerCase();
    
    @Override
    public void initialize(URL location, ResourceBundle rb) {
         try {
        setTable();
        //Tardiness Table
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tiAM.setCellValueFactory(new PropertyValueFactory<>("timeInAm"));
        toAM.setCellValueFactory(new PropertyValueFactory<>("timeOutAm"));
        tiPM.setCellValueFactory(new PropertyValueFactory<>("timeInPm"));
        toPM.setCellValueFactory(new PropertyValueFactory<>("timeOutPm"));
        remCol.setCellValueFactory(new PropertyValueFactory<>("attendance_status"));
        //Selection Name table
        user_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        empName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        dateTimeLabel.setText(String.valueOf(currentMonth)+" "+currentDay+", "+ currentYear);
        ObservableList<String> monthList = FXCollections.observableArrayList();
        monthList.addAll("January", "February", "March", 
                "April", "May", "June", "July", 
                "August", "September", "October", 
                "November", "December");
        monthChoiceBox.setItems(monthList);
        monthChoiceBox.setValue(capitalizedMonth);
        monthChoiceBox.setOnAction(event -> selectMonth());
        yearChoiceBox.setOnAction(event -> selectYear());
        
        ObservableList<String> monthYear = FXCollections.observableArrayList();
                monthYear.addAll("2021","2022");
                for (Attendance attendance : getYearforLabel()){
                        String[] splitItems = attendance.getDate().toString().split("-");
                        monthYear.add(splitItems[0]);
                }
                yearChoiceBox.setItems(monthYear);
                yearChoiceBox.setValue(currentYear+"");
                
          } catch (Exception e) {
        e.printStackTrace();
    }     
    }
    
    @FXML
    public void generateDTR(ActionEvent event){
        generateDOCX();
    }
    
    private void generateDOCX() {
        try {
            // Create a new XWPFDocument
            XWPFDocument document = new XWPFDocument();

            // Create a paragraph
            XWPFParagraph paragraph = document.createParagraph();

            // Create a run and add text to it
            XWPFRun run = paragraph.createRun();
            run.setText("Hello, I'm Under the Water, Please Help me!");

            // Save the document to a file
            try (FileOutputStream out = new FileOutputStream("sample.docx")) {
                document.write(out);
            }

            System.out.println("DOCX file generated successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   public void selectYear() {
        try {
            String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
            String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
            monthYearLabel.setText(selectedMonth + " , " + selectedYear);

            if (nameLabel.getText() != null) {
                Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
                showAttTable(selectedItem.getId());
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace(); // You can replace this with proper logging
            // You can also show a user-friendly error message if needed
        }
    }
    public void selectMonth(){
         try {
            String selectedMonth = (String) monthChoiceBox.getSelectionModel().getSelectedItem();
            String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
            monthYearLabel.setText(selectedMonth + " , " + selectedYear);

            if (nameLabel.getText() != null) {
                Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
                showAttTable(selectedItem.getId());
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace(); // You can replace this with proper logging
            // You can also show a user-friendly error message if needed
        }
    }
    
    public void setTable(){
        
        empNameTable.setItems(Attendance.getEmpName());
        monthChoiceBox.setValue(capitalizedMonth);
        yearChoiceBox.setValue(currentYear+"");
        searchBar.setText("");
        nameLabel.setText("");
        totalLogin.setText("");
        totalTardiness.setText("");
        byPercent.setText("0%");
        totalLate.setText("");
        totalAbsent.setText("");
        searchBar.setPromptText("Search name...");
        tardinessTable.setItems(null);
        monthChoiceBox.setOnAction(event -> selectMonth());
        yearChoiceBox.setOnAction(event -> selectYear());
    }
    @FXML
    private void filterBySearchBar(KeyEvent event) {
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
        String keyword = searchBar.getText().toLowerCase(); 
        
            for (Attendance attendance : getEmpName()) {   
                    if ((attendance.getName().toLowerCase()).contains(keyword)){
                        filteredData.add(attendance);
                    }
                }
        empNameTable.setItems(filteredData);
    }
    @FXML
    private void selectEMployee(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showAttTable(selectedItem.getId());
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        }else{
            totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalAbsent.setText("");
            totalTardiness.setText("");
            searchBar.setPromptText("Search name...");
            tardinessTable.setItems(null);
        }
    }
    @FXML
    private void selectEMployeeLate(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showAttTableLate(selectedItem.getId());
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        }else{
            totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalAbsent.setText("");
            totalTardiness.setText("");
            searchBar.setPromptText("Search name...");
            tardinessTable.setItems(null);
        }
    }
     @FXML
    private void selectEMployeeAbsent(MouseEvent event) {
        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
        showAttTableAbsent(selectedItem.getId());
        if (selectedItem != null) {
            nameLabel.setText(selectedItem.getName());
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        }else{
            totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalAbsent.setText("");
            totalTardiness.setText("");
            searchBar.setPromptText("Search name...");
            tardinessTable.setItems(null);
        }
    }
    
     public void showAttTable(int user_id){
         totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalTardiness.setText("");
            totalAbsent.setText("");
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
                List<String> dayHolder = new LinkedList<>();
                List<String> holidayHolder = new LinkedList<>();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        String[] dateOnCTRL = monthYearLabel.getText().split(" ");
        String selectedName = nameLabel.getText();
        String monthToNum = "",converted ="";
        // Count of days that are not Saturday or Sunday
        int workingDaysInMonth = 0, SpecialDays=0;
        
        
        double tardiness;
        int daysInMonth=0, lateCount = 0, absentCount = 0;
                if(dateOnCTRL[0].equals("January")){monthToNum="1";}if(dateOnCTRL[0].equals("February")){monthToNum="2";}
                if(dateOnCTRL[0].equals("March")){monthToNum="3";}if(dateOnCTRL[0].equals("April")){monthToNum="4";}
                if(dateOnCTRL[0].equals("May")){monthToNum="5";}if(dateOnCTRL[0].equals("June")){monthToNum="6";}
                if(dateOnCTRL[0].equals("July")){monthToNum="7";}if(dateOnCTRL[0].equals("August")){monthToNum="8";}
                if(dateOnCTRL[0].equals("September")){monthToNum="9";}if(dateOnCTRL[0].equals("October")){monthToNum="10";}
                if(dateOnCTRL[0].equals("November")){monthToNum="11";}if(dateOnCTRL[0].equals("December")){monthToNum="12";}
                 
        int year= Integer.parseInt(dateOnCTRL[2]);
                int month = Integer.parseInt(monthToNum);
                YearMonth yearMonth = YearMonth.of(year, month);
                daysInMonth = yearMonth.lengthOfMonth();
                
                //Checking if there's the Special calendar
                    for (Special_Calendar sCalendar : getCalendarByUserId(user_id)) {
                        String[] dateOnModel = sCalendar.getStartDate().toString().split("-");
                        int total = (int) sCalendar.getTotal();
                        if (dateOnModel[1].equals(monthToNum)) {
                            int toNum = Integer.parseInt(dateOnModel[2]);
                            for (int l = 0; l < total; l++) {
                                if (toNum < daysInMonth && toNum >= 1) {
                                    LocalDate specialDate = LocalDate.of(year, month, toNum);
                                    if (specialDate.getDayOfWeek() != DayOfWeek.SATURDAY && specialDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                        SpecialDays++;
                                    }
                                    holidayHolder.add(toNum + "");
                                }
                                toNum++;
                            }
                        }
                    }
                    
                for (int k = 1; k <= daysInMonth; k++) {
                    LocalDate localDate1 = LocalDate.of(year, month, k);
                // Check if the day is not Saturday (DayOfWeek.SATURDAY) or Sunday (DayOfWeek.SUNDAY)
                    if (localDate1.getDayOfWeek() != DayOfWeek.SATURDAY && localDate1.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        workingDaysInMonth++;
                    }
                }
                
                    System.out.println(daysInMonth);
                    System.out.println(Arrays.toString(holidayHolder.toArray()));
                for (Attendance attendance : getAttendancebyLate(user_id)){
                    System.out.println(attendance.getDate()+", "+attendance.getTimeInAm()+", "+attendance.getTimeOutAm()+", "+attendance.getTimeInPm()+", "+attendance.getTimeOutPm());
                    String[] dateOnModel = attendance.getDate().toString().split("-");
                    if(dateOnModel[1].equals(monthToNum)){
                        dayHolder.add(dateOnModel[2]);
                    }
                }
                for(int i =1; i<=daysInMonth; i++){
                    LocalDate localDate = LocalDate.of(year, month, i);
                    if(holidayHolder.contains(String.valueOf(i))){
                        // Skip Holidays and TimeOFFs
                    }else
                    if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        // Skip Saturday and Sunday
                    }else{
                        boolean dayChecker = false;
                        for(int j =0; j<dayHolder.size(); j++){
                            int amDay = Integer.parseInt(dayHolder.get(j));
                                if(i==amDay){
                                dayChecker=true;
                            }
                        }
                        if(dayChecker==true){
                            for (Attendance attendance : getAttendancebyLate(user_id)){

                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Late")){
                                    filteredData.add(attendance);
                                     lateCount++;
                                    break;
                                }
                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Absent")){
                                    filteredData.add(attendance);
                                     absentCount++;
                                    break;
                                }
                            }
                        }
                        if(dayChecker==false){
                            for (Attendance attendance : getAttendancebyLate(user_id)){
                                attendance.setDate(java.sql.Date.valueOf(localDate));
                                attendance.setTimeInAm("    --");
                                attendance.setTimeOutAm("    --");
                                attendance.setTimeInPm("    --");
                                attendance.setTimeOutPm("    --");
                                attendance.setAttendance_status("Absent");
                                filteredData.add(attendance);
                                absentCount++;
                                break;
                            }
                        }
                    }
                }
                int roundedTardiness=0;
            if (lateCount != 0 || absentCount != 0) {
                tardiness = ((double) (lateCount + absentCount) / (double) 10) * 100;
                if (tardiness > 100.0) {
                    tardiness = 100.0;  // Cap the tardiness at 100%
                }
                converted = String.format("%.1f", tardiness);
                roundedTardiness = (int) Math.round(Double.parseDouble(converted));
                workingDaysInMonth = workingDaysInMonth-SpecialDays;
                totalLogin.setText(workingDaysInMonth + "");  
                totalLate.setText(lateCount + "");
                totalAbsent.setText(absentCount + "");
                int totalTard = (int)lateCount + absentCount;
                totalTardiness.setText(totalTard+ "");
            } else {
                converted = "0";
            }
            tardinessTable.setItems(filteredData);
            byPercent.setText(roundedTardiness + "%"); 
     }   
      public void showAttTableLate(int user_id){
         totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalTardiness.setText("");
            totalAbsent.setText("");
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
                List<String> dayHolder = new LinkedList<>();
                List<String> holidayHolder = new LinkedList<>();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        String[] dateOnCTRL = monthYearLabel.getText().split(" ");
        String selectedName = nameLabel.getText();
        String monthToNum = "",converted ="";
        // Count of days that are not Saturday or Sunday
        int workingDaysInMonth = 0, SpecialDays=0;
        
        
        double tardiness;
        int daysInMonth=0, lateCount = 0, absentCount = 0;
                if(dateOnCTRL[0].equals("January")){monthToNum="1";}if(dateOnCTRL[0].equals("February")){monthToNum="2";}
                if(dateOnCTRL[0].equals("March")){monthToNum="3";}if(dateOnCTRL[0].equals("April")){monthToNum="4";}
                if(dateOnCTRL[0].equals("May")){monthToNum="5";}if(dateOnCTRL[0].equals("June")){monthToNum="6";}
                if(dateOnCTRL[0].equals("July")){monthToNum="7";}if(dateOnCTRL[0].equals("August")){monthToNum="8";}
                if(dateOnCTRL[0].equals("September")){monthToNum="9";}if(dateOnCTRL[0].equals("October")){monthToNum="10";}
                if(dateOnCTRL[0].equals("November")){monthToNum="11";}if(dateOnCTRL[0].equals("December")){monthToNum="12";}
                 
        int year= Integer.parseInt(dateOnCTRL[2]);
                int month = Integer.parseInt(monthToNum);
                YearMonth yearMonth = YearMonth.of(year, month);
                daysInMonth = yearMonth.lengthOfMonth();
                
                //Checking if there's the Special calendar
                    for (Special_Calendar sCalendar : getCalendarByUserId(user_id)) {
                        String[] dateOnModel = sCalendar.getStartDate().toString().split("-");
                        int total = (int) sCalendar.getTotal();
                        if (dateOnModel[1].equals(monthToNum)) {
                            int toNum = Integer.parseInt(dateOnModel[2]);
                            for (int l = 0; l < total; l++) {
                                if (toNum < daysInMonth && toNum >= 1) {
                                    LocalDate specialDate = LocalDate.of(year, month, toNum);
                                    if (specialDate.getDayOfWeek() != DayOfWeek.SATURDAY && specialDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                        SpecialDays++;
                                    }
                                    holidayHolder.add(toNum + "");
                                }
                                toNum++;
                            }
                        }
                    }
 // -------------------------naay bug, pag saturday or sunday ang holiday. magDouble ang minus sa working days.
                for (int k = 1; k <= daysInMonth; k++) {
                    LocalDate localDate1 = LocalDate.of(year, month, k);
                // Check if the day is not Saturday (DayOfWeek.SATURDAY) or Sunday (DayOfWeek.SUNDAY)
                    if (localDate1.getDayOfWeek() != DayOfWeek.SATURDAY && localDate1.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        workingDaysInMonth++;
                    }
                }
                
                    System.out.println(daysInMonth);
                    System.out.println(Arrays.toString(holidayHolder.toArray()));
                for (Attendance attendance : getAttendancebyLate(user_id)){
                    System.out.println(attendance.getDate()+", "+attendance.getTimeInAm()+", "+attendance.getTimeOutAm()+", "+attendance.getTimeInPm()+", "+attendance.getTimeOutPm());
                    String[] dateOnModel = attendance.getDate().toString().split("-");
                    if(dateOnModel[1].equals(monthToNum)){
                        dayHolder.add(dateOnModel[2]);
                    }
                }
                for(int i =1; i<=daysInMonth; i++){
                    LocalDate localDate = LocalDate.of(year, month, i);
                    if(holidayHolder.contains(String.valueOf(i))){
                        // Skip Holidays and TimeOFFs
                    }else
                    if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        // Skip Saturday and Sunday
                    }else{
                        boolean dayChecker = false;
                        for(int j =0; j<dayHolder.size(); j++){
                            int amDay = Integer.parseInt(dayHolder.get(j));
                                if(i==amDay){
                                dayChecker=true;
                            }
                        }
                        if(dayChecker==true){
                            for (Attendance attendance : getAttendancebyLate(user_id)){

                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Late")){
                                    filteredData.add(attendance);
                                     lateCount++;
                                    break;
                                }
                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Absent")){
//                                    filteredData.add(attendance);
                                     absentCount++;
                                    break;
                                }
                            }
                        }
                        if(dayChecker==false){
                            for (Attendance attendance : getAttendancebyLate(user_id)){
//                                attendance.setDate(java.sql.Date.valueOf(localDate));
//                                attendance.setTimeInAm("    --");
//                                attendance.setTimeOutAm("    --");
//                                attendance.setTimeInPm("    --");
//                                attendance.setTimeOutPm("    --");
//                                attendance.setAttendance_status("Absent");
//                                filteredData.add(attendance);
                                absentCount++;
                                break;
                            }
                        }
                    }
                }
                int roundedTardiness=0;
            if (lateCount != 0 || absentCount != 0) {
                tardiness = ((double) (lateCount + absentCount) / (double) 10) * 100;
                if (tardiness > 100.0) {
                    tardiness = 100.0;  // Cap the tardiness at 100%
                }
                converted = String.format("%.1f", tardiness);
                roundedTardiness = (int) Math.round(Double.parseDouble(converted));
                workingDaysInMonth = workingDaysInMonth-SpecialDays;
                totalLogin.setText(workingDaysInMonth + "");  
                totalLate.setText(lateCount + "");
                totalAbsent.setText(absentCount + "");
                int totalTard = (int)lateCount + absentCount;
                totalTardiness.setText(totalTard+ "");
            } else {
                converted = "0";
            }
            tardinessTable.setItems(filteredData);
            byPercent.setText(roundedTardiness + "%"); 
     }  
     public void showAttTableAbsent(int user_id){
         totalLogin.setText("");
            byPercent.setText("0%");
            totalLate.setText("");
            totalTardiness.setText("");
            totalAbsent.setText("");
            searchBar.setText("");
            searchBar.setPromptText("Search name...");
        ObservableList<Attendance> filteredData = FXCollections.observableArrayList();
                List<String> dayHolder = new LinkedList<>();
                List<String> holidayHolder = new LinkedList<>();
        String selectedYear = (String) yearChoiceBox.getSelectionModel().getSelectedItem();
        String[] dateOnCTRL = monthYearLabel.getText().split(" ");
        String selectedName = nameLabel.getText();
        String monthToNum = "",converted ="";
        // Count of days that are not Saturday or Sunday
        int workingDaysInMonth = 0, SpecialDays=0;
        
        
        double tardiness;
        int daysInMonth=0, lateCount = 0, absentCount = 0;
                if(dateOnCTRL[0].equals("January")){monthToNum="1";}if(dateOnCTRL[0].equals("February")){monthToNum="2";}
                if(dateOnCTRL[0].equals("March")){monthToNum="3";}if(dateOnCTRL[0].equals("April")){monthToNum="4";}
                if(dateOnCTRL[0].equals("May")){monthToNum="5";}if(dateOnCTRL[0].equals("June")){monthToNum="6";}
                if(dateOnCTRL[0].equals("July")){monthToNum="7";}if(dateOnCTRL[0].equals("August")){monthToNum="8";}
                if(dateOnCTRL[0].equals("September")){monthToNum="9";}if(dateOnCTRL[0].equals("October")){monthToNum="10";}
                if(dateOnCTRL[0].equals("November")){monthToNum="11";}if(dateOnCTRL[0].equals("December")){monthToNum="12";}
                 
        int year= Integer.parseInt(dateOnCTRL[2]);
                int month = Integer.parseInt(monthToNum);
                YearMonth yearMonth = YearMonth.of(year, month);
                daysInMonth = yearMonth.lengthOfMonth();
                
                //Checking if there's the Special calendar
                    for (Special_Calendar sCalendar : getCalendarByUserId(user_id)) {
                        String[] dateOnModel = sCalendar.getStartDate().toString().split("-");
                        int total = (int) sCalendar.getTotal();
                        if (dateOnModel[1].equals(monthToNum)) {
                            int toNum = Integer.parseInt(dateOnModel[2]);
                            for (int l = 0; l < total; l++) {
                                if (toNum < daysInMonth && toNum >= 1) {
                                    LocalDate specialDate = LocalDate.of(year, month, toNum);
                                    if (specialDate.getDayOfWeek() != DayOfWeek.SATURDAY && specialDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                        SpecialDays++;
                                    }
                                    holidayHolder.add(toNum + "");
                                }
                                toNum++;
                            }
                        }
                    }
 // -------------------------naay bug, pag saturday or sunday ang holiday. magDouble ang minus sa working days.
                for (int k = 1; k <= daysInMonth; k++) {
                    LocalDate localDate1 = LocalDate.of(year, month, k);
                // Check if the day is not Saturday (DayOfWeek.SATURDAY) or Sunday (DayOfWeek.SUNDAY)
                    if (localDate1.getDayOfWeek() != DayOfWeek.SATURDAY && localDate1.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        workingDaysInMonth++;
                    }
                }
                
                    System.out.println(daysInMonth);
                    System.out.println(Arrays.toString(holidayHolder.toArray()));
                for (Attendance attendance : getAttendancebyLate(user_id)){
                    System.out.println(attendance.getDate()+", "+attendance.getTimeInAm()+", "+attendance.getTimeOutAm()+", "+attendance.getTimeInPm()+", "+attendance.getTimeOutPm());
                    String[] dateOnModel = attendance.getDate().toString().split("-");
                    if(dateOnModel[1].equals(monthToNum)){
                        dayHolder.add(dateOnModel[2]);
                    }
                }
                for(int i =1; i<=daysInMonth; i++){
                    LocalDate localDate = LocalDate.of(year, month, i);
                    if(holidayHolder.contains(String.valueOf(i))){
                        // Skip Holidays and TimeOFFs
                    }else
                    if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        // Skip Saturday and Sunday
                    }else{
                        boolean dayChecker = false;
                        for(int j =0; j<dayHolder.size(); j++){
                            int amDay = Integer.parseInt(dayHolder.get(j));
                                if(i==amDay){
                                dayChecker=true;
                            }
                        }
                        if(dayChecker==true){
                            for (Attendance attendance : getAttendancebyLate(user_id)){

                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Late")){
//                                    filteredData.add(attendance);
                                     lateCount++;
                                    break;
                                }
                                if(attendance.getDate().equals(java.sql.Date.valueOf(localDate))
                                        && attendance.getAttendance_status().equals("Absent")){
                                    filteredData.add(attendance);
                                     absentCount++;
                                    break;
                                }
                            }
                        }
                        if(dayChecker==false){
                            for (Attendance attendance : getAttendancebyLate(user_id)){
                                attendance.setDate(java.sql.Date.valueOf(localDate));
                                attendance.setTimeInAm("    --");
                                attendance.setTimeOutAm("    --");
                                attendance.setTimeInPm("    --");
                                attendance.setTimeOutPm("    --");
                                attendance.setAttendance_status("Absent");
                                filteredData.add(attendance);
                                absentCount++;
                                break;
                            }
                        }
                    }
                }
                int roundedTardiness=0;
            if (lateCount != 0 || absentCount != 0) {
                tardiness = ((double) (lateCount + absentCount) / (double) 10) * 100;
                if (tardiness > 100.0) {
                    tardiness = 100.0;  // Cap the tardiness at 100%
                }
                converted = String.format("%.1f", tardiness);
                roundedTardiness = (int) Math.round(Double.parseDouble(converted));
                workingDaysInMonth = workingDaysInMonth-SpecialDays;
                totalLogin.setText(workingDaysInMonth + "");  
                totalLate.setText(lateCount + "");
                totalAbsent.setText(absentCount + "");
                int totalTard = (int)lateCount + absentCount;
                totalTardiness.setText(totalTard+ "");
            } else {
                converted = "0";
            }
            tardinessTable.setItems(filteredData);
            byPercent.setText(roundedTardiness + "%"); 
     }
//   
    
    @FXML
    private void resetSelectedItems(ActionEvent event){
        setTable();
    }
    
//    @FXML
//     public void showOnLabels() {
//        Attendance selectedItem = empNameTable.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            nameLabel.setText(selectedItem.getName());
//        }
//    }
}

