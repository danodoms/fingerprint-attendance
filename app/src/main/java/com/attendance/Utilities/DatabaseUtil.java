/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.attendance.Utilities;

import java.sql.*;

/**
 *
 * @author admin
 */
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendance";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public static Connection getConnection(){
        Connection connection;
        try{
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static void executeQuery(String query){
        Connection con = getConnection();
        Statement st;
        try{
            st = con.createStatement();
            st.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
