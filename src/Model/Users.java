/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import javafx.scene.image.Image;

/**
 *
 * @author admin
 */
public class Users {
    private int id;
    private String password;
    private String email;
    private String address;
    private String user_fname;
    private String user_lname;
    private String user_mname;
    private String suffix;
    private String user_type;
    private int user_cntct;
    private int user_status;
    private Image user_img;

    public Users(int id, String password, String email, String address, String user_fname, String user_lname, String user_mname, String suffix, String user_type, int user_cntct, int user_status, Image user_img) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.address = address;
        this.user_fname = user_fname;
        this.user_lname = user_lname;
        this.user_mname = user_mname;
        this.suffix = suffix;
        this.user_type = user_type;
        this.user_cntct = user_cntct;
        this.user_status = user_status;
        this.user_img = user_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public void setUser_fname(String user_fname) {
        this.user_fname = user_fname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public void setUser_lname(String user_lname) {
        this.user_lname = user_lname;
    }

    public String getUser_mname() {
        return user_mname;
    }

    public void setUser_mname(String user_mname) {
        this.user_mname = user_mname;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getUser_cntct() {
        return user_cntct;
    }

    public void setUser_cntct(int user_cntct) {
        this.user_cntct = user_cntct;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public Image getUser_img() {
        return user_img;
    }

    public void setUser_img(Image user_img) {
        this.user_img = user_img;
    }
    
}
