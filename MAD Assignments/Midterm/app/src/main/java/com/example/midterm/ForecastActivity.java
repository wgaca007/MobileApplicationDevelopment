package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
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

public class ForecastActivity extends AppCompatActivity {

    CardView card;
    LinearLayout ll1;
    TextView time,temp,humidity,desc;
    ImageView imageView;
    Weather weather;
    City city;
    ArrayList<Weather> weatherList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        card=findViewById(R.id.card);
        ll1=findViewById(R.id.ll1);
        time=findViewById(R.id.time);
        temp=findViewById(R.id.temp);
        humidity=findViewById(R.id.desc);
        desc=findViewById(R.id.card);
        imageView=findViewById(R.id.imageView);

        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(ForecastActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() != null && getIntent().getExtras() != null){
            city = getIntent().getParcelableExtra(selectedcity);
            if(!isConnected()){
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
            else {
                new GetForecast().execute("http://api.openweathermap.org/data/2.5/forecast?q=\"+city.city+\",\"+city.country+\"&appid=db5363c37a85d9bfbd711059144591e9");

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
    private class GetForecast extends AsyncTask<String, Void, ArrayList<Weather>> {
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
                        Toast.makeText(ForecastActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    //JSONObject weather = root.getJSONObject("weather");
                   // JSONObject main = root.getJSONObject("main");
                   // JSONObject wind = root.getJSONObject("wind");
                    JSONArray weatherJSONArray = root.getJSONArray("list");


                    for(int i=0;i < weatherJSONArray.length(); i++){

                        JSONObject sourceJSON = weatherJSONArray.getJSONObject(i);
                        JSONObject wind = sourceJSON.getJSONObject("wind");
                        JSONObject main = sourceJSON.getJSONObject("main");
                        JSONObject weather = sourceJSON.getJSONObject("weather");
                        JSONObject date = sourceJSON.getJSONObject("dt_txt");
                        weatherList.add(new Weather(main.getString("temp"),main.getString("temp_max"),main.getString("temp_min"),main.getString("humidity"),sourceJSON.getString("description"),wind.getString("speed")));

                    }


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
            if(sources.size() == 0){
                Toast.makeText(ForecastActivity.this, "No Tracks Found", Toast.LENGTH_SHORT).show();
                return;
            }

            recyclerView.setAlpha(1);

            MyAdapter myadapter = new MyAdapter(sources, ForecastActivity.this);
            recyclerView.setAdapter(myadapter);
        }
    }
}
