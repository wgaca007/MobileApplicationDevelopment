package com.example.studentprofilebuilder;

import java.io.Serializable;

public class Student implements Serializable {

    private int id;
    private String FirstName;
    private String LastName;
    private int ImageID;
    private String Department;

    public int getId() {
        return id;
    }

    public Student(int id, String firstName, String lastName, int imageID, String department) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        ImageID = imageID;
        Department = department;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstname) {
        this.FirstName = firstname;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastname) {
        this.LastName = lastname;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        this.ImageID = imageID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        this.Department = department;
    }
}