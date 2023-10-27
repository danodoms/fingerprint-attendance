/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author admin
 */
public class Shift {
    private int id;
    private String shiftName;
    private String startTime;
    private String endTime;

    public Shift(int id, String shiftName, String startTime, String endTime) {
        this.id = id;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
   
    public Shift(int id, String shiftName) {
        this.id = id;
        this.shiftName = shiftName;
    }
    
    public Shift(int id){
        this.id = id;
    }
    
    public Shift(String shiftName) {
        this.shiftName = shiftName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
        @Override
    public String toString() {
        return shiftName;
    }

}
