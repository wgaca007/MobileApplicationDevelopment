package com.example.midterm;

import android.os.Parcelable;

import java.io.Serializable;

public class Countrycity implements Serializable {

    String city,Country;

    public Countrycity(String city, String country) {
        this.city = city;
        Country = country;
    }

    @Override
    public String toString() {
        return ""+city+", "+Country;
    }
}
