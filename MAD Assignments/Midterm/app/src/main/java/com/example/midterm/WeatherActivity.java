package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.midterm.MainActivity.selectedcity;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    City city;
    LinearLayout linearLayout,linearLayout2,templl,maxll,minll,descll,humll,wsll;
    Button forecast;
    TextView temp,tempmax,tempmin,desc,humidity,speed,city1,country;
    ArrayList<Weather> weatherList = new ArrayList<>();
    String city2;
    String country1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        linearLayout= findViewById(R.id.linearLayout);
        linearLayout2=findViewById(R.id.linearLayout2);
        templl=findViewById(R.id.templl);
        maxll=findViewById(R.id.maxll);
        minll=findViewById(R.id.minll);
        descll=findViewById(R.id.minll);
        humll=findViewById(R.id.humll);
        wsll=findViewById(R.id.wsll);
        forecast=findViewById(R.id.forecast);
        temp=findViewById(R.id.temp);
        tempmax=findViewById(R.id.tempMax);
        tempmin=findViewById(R.id.tempMin);
        humidity=findViewById(R.id.humidity);
        desc=findViewById(R.id.desc);
        speed=findViewById(R.id.speed);
        city1=findViewById(R.id.city);
        country=findViewById(R.id.country);


        forecast.setOnClickListener(this);

        if(getIntent() != null && getIntent().getExtras() != null){
            city = getIntent().getParcelableExtra(selectedcity);
            if(!isConnected()){
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
            else {
                city1.setText(city.city);
                country.setText(city.country);
                city2=city.city.toString();
                country1=city.country.toString();
                new GetWeather().execute("http://api.openweathermap.org/data/2.5/weather?q="+city.city+","+city.country+"&appid=db5363c37a85d9bfbd711059144591e9");

            }
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if(network != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return false;

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(WeatherActivity.this, ForecastActivity.class);
        City city = new City(city2,country1);
        i.putExtra(selectedcity, (Parcelable) city);
        startActivity(i);
    }

    private class GetWeather extends AsyncTask<String, Void, ArrayList<Weather>> {
        JSONObject sourceJSON;
        @Override
        protected ArrayList<Weather> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(json.equals("")){
                        Toast.makeText(WeatherActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONObject weather = root.getJSONObject("weather");
                    JSONObject main = root.getJSONObject("main");
                    JSONObject wind = root.getJSONObject("wind");
                    JSONArray weatherJSONArray = root.getJSONArray("weather");


                    for(int i=0;i < weatherJSONArray.length(); i++){

                        JSONObject sourceJSON = weatherJSONArray.getJSONObject(i);
                    }
                    weatherList.add(new Weather(main.getString("temp"),main.getString("temp_max"),main.getString("temp_min"),main.getString("humidity"),sourceJSON.getString("description"),wind.getString("speed")));


                    return weatherList;

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Weather> sources) {
if(sources != null){
    temp.setText(sources.get(0).temp);
    tempmax.setText(sources.get(0).tempmax);
    tempmin.setText(sources.get(0).tempmin);
    desc.setText(sources.get(0).desc);
            humidity.setText(sources.get(0).humidity);
            speed.setText(sources.get(0).speed);

}
        }
    }
}
