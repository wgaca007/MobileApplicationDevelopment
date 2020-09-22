package com.example.homework07;

import java.util.ArrayList;

public class User {

    public String firstname, lastname, photourl;
    public String email,userId;
    public ArrayList<String>trips = new ArrayList<String>();
    private boolean isSelected = false;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstname, String lastname, String email, String userId, String photourl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.photourl = photourl;
        this.email = email;
        this.userId = userId;
    }

    public void setTrip(String trip) {
        this.trips.add(trip);
    }


    public ArrayList<String> getTrips() {
        return trips;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTrips(ArrayList<String> trips) {
        this.trips = trips;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}

