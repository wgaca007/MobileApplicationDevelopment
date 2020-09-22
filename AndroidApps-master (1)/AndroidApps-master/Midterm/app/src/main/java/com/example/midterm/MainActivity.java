package com.example.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<Countrycity> countrylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listview);

        ArrayAdapter myadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, countrylist);
        listview.setAdapter(myadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Countrycity countrycity = (Countrycity) parent.getItemAtPosition(position);

                new GetWeather(countrycity).execute("http://api.openweathermap.org/data/2.5/weather?q=" +
                        countrycity.city+","+countrycity.Country +
                        "&appid=3d585da6b48ae9c63edf68edc115dadd");


            }
        });


        String json = loadJSONFromAsset(MainActivity.this);
        System.out.println("json = " + json);

        try {
            JSONObject root = new JSONObject(json);
            JSONArray dataarray = root.getJSONArray("data");

            for(int i = 0; i < dataarray.length();i++){

                JSONObject citycountry = dataarray.getJSONObject(i);
                countrylist.add(new Countrycity(citycountry.getString("city"), citycountry.getString("country")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
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

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.cities);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    protected class GetWeather extends AsyncTask<String, Void, CurrentWeatherClass> {

        Countrycity countrycity;

        public GetWeather(Countrycity countrycity) {
            this.countrycity = countrycity;
        }

        @Override
        protected CurrentWeatherClass doInBackground(String... strings) {
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
                        Toast.makeText(MainActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONObject main = root.getJSONObject("main");

                    String temp = main.getString("temp");
                    String temp_max = main.getString("temp_max");
                    String temp_min = main.getString("temp_min");
                    String humidity = main.getString("humidity");

                    JSONArray descriptionjsonarray = root.getJSONArray("weather");
                    String description = descriptionjsonarray.getJSONObject(0).getString("description");

                    JSONObject windjson = root.getJSONObject("wind");
                    String windspeed = windjson.getString("speed");

                    CurrentWeatherClass c = new CurrentWeatherClass(temp, temp_max, temp_min, description, humidity, windspeed);

                    return c;




                    /*JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");

                    JSONArray tracklistArray = body.getJSONArray("track_list");

                    for(int i=0;i < tracklistArray.length(); i++){

                        JSONObject trackJSON = tracklistArray.getJSONObject(i);
                        JSONObject track = trackJSON.getJSONObject("track");

                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.US);
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

                        String date = track.getString("updated_time");
                        Date my_date = inputFormat.parse(date);
                        //track.date = outputFormat.format(my_date);

                        tracklists.add(new Song(track.getString("track_name"), track.getString("artist_name"), track.getString("album_name"), outputFormat.format(my_date), track.getString("track_share_url")));

                    }

                    return tracklists;*/

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

        /*@Override
        protected void onPostExecute(CurrentWeatherClass) {
            return;
            //pb.setVisibility(View.INVISIBLE);
            *//*if(tracklists.size() == 0){
                Toast.makeText(MainActivity.this, "No Tracks Found", Toast.LENGTH_SHORT).show();
                return;
            }*/

        @Override
        protected void onPostExecute(CurrentWeatherClass currentWeatherClass) {
            super.onPostExecute(currentWeatherClass);

            Intent i = new Intent(MainActivity.this, CurrentWeather.class);
                i.putExtra("currentweather", currentWeatherClass);
                i.putExtra("countrycity", this.countrycity);
                startActivity(i);
        }

    }




    }
