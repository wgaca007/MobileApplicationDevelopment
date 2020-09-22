package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Forecast extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    MyAdapter myAdapter;
    TextView forecastheading;
    Countrycity countrycity;

    ArrayList<ForecastClass> forecastClasslist = new ArrayList<ForecastClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        if(getIntent() != null && getIntent().getExtras() != null) {

            //forecastlist = (ArrayList<Forecast>)getIntent().getParcelableArrayListExtra("forecastlist");
             countrycity = (Countrycity) getIntent().getSerializableExtra("countrycity");

            new GetForecast(countrycity).execute("http://api.openweathermap.org/data/2.5/forecast?q=" +
                    countrycity.city+","+countrycity.Country +
                    "&appid=3d585da6b48ae9c63edf68edc115dadd");


            recyclerView = findViewById(R.id.recyclerView);

            layoutManager = new LinearLayoutManager(Forecast.this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setHasFixedSize(true);


            //recyclerView.setAdapter(myAdapter);


            //recyclerView.setAdapter(MyAdapter);
        }


    }

    protected class GetForecast extends AsyncTask<String, Void, ArrayList<ForecastClass>> {

        Countrycity countrycity;

        public GetForecast(Countrycity countrycity) {
            this.countrycity = countrycity;
        }

        @Override
        protected ArrayList<ForecastClass> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                System.out.println("URL =" + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(json.equals("")){
                        Toast.makeText(Forecast.this, "No JSON Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONArray listarray = root.getJSONArray("list");


                    for(int i=0;i < listarray.length(); i++){
                        JSONObject main = listarray.getJSONObject(i).getJSONObject("main");

                        String temp = main.getString("temp");
                        String humidity = main.getString("humidity");

                        String description = listarray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                        String time= listarray.getJSONObject(i).getString("dt");

                        forecastClasslist.add(new ForecastClass(time,temp,humidity,description));

                    }

                    return forecastClasslist;

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ForecastClass> forecastClasslist) {
            //forecastheading.setText(this.countrycity.city+", "+this.countrycity.Country);
            //super.onPostExecute(forecastClasses);
            myAdapter = new MyAdapter(forecastClasslist);
            recyclerView.setAdapter(myAdapter);
            //myAdapter.notifyDataSetChanged();

           //recyclerView.no

        }
    }
}
