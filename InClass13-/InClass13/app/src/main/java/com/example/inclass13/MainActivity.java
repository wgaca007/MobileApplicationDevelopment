package com.example.inclass13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView addtripicon;
    City selectedcity;
    String tripname;

    RecyclerView myrecyclerView;
    MyAdapter myAdapter;
    ArrayList<City> cityinfolist = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            assert data != null;
            selectedcity = (City) data.getSerializableExtra("selectedcity");
            cityinfolist.add(selectedcity);
            myAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addtripicon = findViewById(R.id.addtripicon);

        myrecyclerView = findViewById(R.id.myrecyclerview);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(cityinfolist);
        myrecyclerView.setAdapter(myAdapter);

        addtripicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTripActivity.class);
                startActivityForResult(i, 100);
            }
        });
    }

    protected class GetGeoCode extends AsyncTask<City, Void, ArrayList<City>> {

        HttpURLConnection connection = null;



        @Override
        protected ArrayList<City> doInBackground(City... cities) {
            URL url = null;
            try {
                url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAlxnZIyWkn46Cr9CgPRGdOR82U8AucPYw&placeid=" + cities[0].id);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject result = root.getJSONObject("result");
                    JSONObject location = result;
                    /*for (int i = 0; i < predictionsarray.length(); i++) {
                        JSONObject jsonObject = predictionsarray.getJSONObject(i);
                        String description = jsonObject.getString("description");
                        citylist.add(new City(jsonObject.getString("place_id"), description, ""));
                    }*/
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return citylist;
        }
    }
}
