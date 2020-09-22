package com.example.midterm;

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastClass implements Parcelable {

    String time, temp, humidity, description;

    public ForecastClass(String time, String temp, String humidity, String description) {
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.description = description;
    }

    protected ForecastClass(Parcel in) {
        time = in.readString();
        temp = in.readString();
        humidity = in.readString();
        description = in.readString();
    }

    public static final Creator<ForecastClass> CREATOR = new Creator<ForecastClass>() {
        @Override
        public ForecastClass createFromParcel(Parcel in) {
            return new ForecastClass(in);
        }

        @Override
        public ForecastClass[] newArray(int size) {
            return new ForecastClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(temp);
        dest.writeString(humidity);
        dest.writeString(description);
    }
}
