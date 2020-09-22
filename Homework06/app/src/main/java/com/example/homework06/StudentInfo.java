package com.example.homework06;

import android.graphics.drawable.Drawable;

public class StudentInfo {
    String fname, lname;
    int studentid, department, imageresource;


    public StudentInfo(String fname, String lname, int studentid, int department, int imageresource) {
        this.fname = fname;
        this.lname = lname;
        this.studentid = studentid;
        this.department = department;
        this.imageresource = imageresource;
    }
}
