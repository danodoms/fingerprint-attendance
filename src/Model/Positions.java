/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author admin
 */
public class Positions {
    private int id;
    private String position_name;
    private String position_description;
    private int department_ID;
    
    public Positions(int id, String position_name){
        this.id = id;
        this.position_name = position_name;
    }
    
    public Positions(String position_name){
        this.position_name = position_name;
    }
    
    @Override
    public String toString() {
        return position_name;
    }
    
}
