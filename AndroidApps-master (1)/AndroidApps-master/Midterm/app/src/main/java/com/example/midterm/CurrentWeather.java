package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CurrentWeather extends AppCompatActivity {

    TextView cityheading, temp, temp_max, temp_min, description, humidity, windspeed;
    CurrentWeatherClass currentWeatherClass;
    Countrycity countrycity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        cityheading = findViewById(R.id.cityheading);

        LinearLayout selectedlayout = findViewById(R.id.weatherlayout);

        temp = selectedlayout.findViewById(R.id.temp);
        temp_max = selectedlayout.findViewById(R.id.temp_max);
        temp_min = selectedlayout.findViewById(R.id.temp_min);
        description = selectedlayout.findViewById(R.id.description);
        humidity = selectedlayout.findViewById(R.id.humidity);
        windspeed = selectedlayout.findViewById(R.id.windspeed);




        if(getIntent() != null && getIntent().getExtras() != null){
            countrycity = (Countrycity) getIntent().getSerializableExtra("countrycity");
            currentWeatherClass = (CurrentWeatherClass) getIntent().getSerializableExtra("currentweather");

            cityheading.setText(countrycity.city+", "+countrycity.Country);


            temp.setText("Temperature   " + currentWeatherClass.temp + " F");
            temp_max.setText("Temperature Max   " + currentWeatherClass.temp_max+ " F");
            temp_min.setText("Temperature Min  " + currentWeatherClass.temp_min + " F");
            description.setText("Description   " + currentWeatherClass.description);
            humidity.setText("Humidity   " + currentWeatherClass.humidity + "%");
            windspeed.setText("Wind Speed   " + currentWeatherClass.windspeed + " miles/hr");


        }

        findViewById(R.id.forecast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CurrentWeather.this, Forecast.class);
                i.putExtra("countrycity", countrycity);
                startActivity(i);

            }
        });
    }

}
