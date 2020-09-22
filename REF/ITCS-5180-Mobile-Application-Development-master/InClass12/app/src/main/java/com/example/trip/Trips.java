package com.example.trip;

import java.io.Serializable;
import java.util.ArrayList;

public class Trips implements Serializable {
    String tripID;
    String tripName;
    City city;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    ArrayList<Places> tripPlaces;

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public ArrayList<Places> getTripPlaces() {
        return tripPlaces;
    }

    public void setTripPlaces(ArrayList<Places> tripPlaces) {
        this.tripPlaces = tripPlaces;
    }
}
