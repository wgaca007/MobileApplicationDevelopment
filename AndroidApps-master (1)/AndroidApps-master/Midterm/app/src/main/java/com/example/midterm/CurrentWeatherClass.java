package com.example.midterm;

import java.io.Serializable;

public class CurrentWeatherClass implements Serializable {
    String temp, temp_max, temp_min, description, humidity, windspeed;

    public CurrentWeatherClass(String temp, String temp_max, String temp_min, String description, String humidity, String windpseed) {
        this.temp = temp;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.description = description;
        this.humidity = humidity;
        this.windspeed = windpseed;
    }
}
