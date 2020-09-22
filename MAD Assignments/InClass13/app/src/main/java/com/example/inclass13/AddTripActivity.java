package com.example.inclass13;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.example.inclass13.MainActivity.REQUEST_CODE;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {
    private Button searchButton, addTripButton;
    private EditText tripName, placeName;
    private String city,trip;
    ArrayList<String> cityList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        searchButton = findViewById(R.id.searchButton);
        addTripButton = findViewById(R.id.addTripButton);
        tripName = findViewById(R.id.tripName);
        placeName = findViewById(R.id.placeName);
        searchButton.setOnClickListener(this);
        addTripButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.searchButton) {
            if (isConnected()) {
                String city = placeName.getText().toString().trim();
                new GetCity().execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDRLa1nXCTHNjddKR-r-jLFv2kfeU80StE&types=(cities)&input="+city+"");
            }
        }
        if (view.getId() == R.id.addTripButton) {
                              Intent i = new Intent(AddTripActivity.this,MainActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("CITY_NAME",city);
                    extras.putString("TRIP_NAME",trip);
                    i.putExtras(extras);

        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    protected class GetCity extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
           cityList.clear();
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
                        Toast.makeText(AddTripActivity.this, "No Cities Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    //JSONObject message = root.getJSONObject("message");
                   // JSONObject body = message.getJSONObject("body");

                    JSONArray cityArray = root.getJSONArray("predictions");
                    String description="";
                    for(int i=0;i < cityArray.length(); i++) {

                        JSONObject cityJSON = cityArray.getJSONObject(i);
                            description= cityJSON.getString("description");
                        cityList.add(description);
                        }

                    Log.d("cityList",""+cityList);
                    }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> cities) {
            super.onPostExecute(cities);
            final ArrayList<String> description = new ArrayList<>();
for(int i=0;i<cities.size();i++){
     description.add(cities.get(i).toString());
}
            AlertDialog.Builder builder = new AlertDialog.Builder(AddTripActivity.this);
            builder.setTitle("Select a city");
            builder.setItems( description.toArray( new String[description.size()]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    city = description.get(item);
                    placeName.setText(description.get(item));
                    trip = tripName.getText().toString().trim();
           /*         Intent i = new Intent();
                    Bundle extras = new Bundle();
                    extras.putString("CITY_NAME",cityName);
                    extras.putString("TRIP_NAME",trip);
                    i.putExtras(extras);
                    setResult(REQUEST_CODE,i);*/
                    dialog.dismiss();

                }
            });
            builder.show();
        }
    }
}