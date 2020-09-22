package com.example.midterm;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
    String city,country;

    public City(String city, String country) {
        this.city = city;
        this.country = country;
    }

    protected City(Parcel in) {
        city = in.readString();
        country = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(country);
    }
}
