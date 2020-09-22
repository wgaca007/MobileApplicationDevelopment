package com.example.myapplication;

import java.io.Serializable;

public class User implements Serializable {
    public String Fname;
    public String Lname;
    public String genderSelected;

    public User(String fname, String lname, String genderSelected) {
        Fname = fname;
        Lname = lname;
        this.genderSelected = genderSelected;
    }

    @Override
    public String toString() {
        return "User{" +
                "Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", genderSelected='" + genderSelected + '\'' +
                '}';
    }
}
