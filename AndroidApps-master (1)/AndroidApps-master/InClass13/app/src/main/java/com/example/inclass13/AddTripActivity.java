package com.example.inclass13;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class AddTripActivity extends AppCompatActivity {

    EditText addtripname, citysearch;
    ArrayList<City>citylist = new ArrayList<>();
    AlertDialog.Builder builder;
    City selectedcity = null;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        citysearch = findViewById(R.id.citysearch);
        addtripname = findViewById(R.id.tripname);

        builder = new AlertDialog.Builder(AddTripActivity.this);
        builder.setTitle("Select the City");



        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = ((EditText) findViewById(R.id.citysearch)).getText().toString();
                new GetCity().execute(place);
            }
        });

        findViewById(R.id.addtrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addtripname.getText().toString().trim().equals("")){
                    Toast.makeText(AddTripActivity.this, "Please Enter Trip Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedcity != null){
                    selectedcity.tripname = addtripname.getText().toString();
                    i = new Intent();
                    i.putExtra("selectedcity", selectedcity);
                    setResult(RESULT_OK, i);
                    finish();
                }
                else{
                    Toast.makeText(AddTripActivity.this, "Please Enter Trip Name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    protected class GetCity extends AsyncTask<String, Void, ArrayList<City>>{

        HttpURLConnection connection = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<City> s) {
            super.onPostExecute(s);
            String[] description = new String[s.size()];

            for(int i = 0; i < s.size();i++){
                description[i] = s.get(i).description;
            }
            builder.setItems(description, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedcity = citylist.get(which);
                    citysearch.setText(selectedcity.description);
                }
            });
            builder.show();

        }

        @Override
        protected ArrayList<City> doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAlxnZIyWkn46Cr9CgPRGdOR82U8AucPYw&type=(cities)&input=" + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray predictionsarray = root.getJSONArray("predictions");
                    for (int i=0;i<predictionsarray.length();i++) {
                        JSONObject jsonObject = predictionsarray.getJSONObject(i);
                        String description = jsonObject.getString("description");
                        citylist.add(new City(jsonObject.getString("place_id"), description, ""));
                    }
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
