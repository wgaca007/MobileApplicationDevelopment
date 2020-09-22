package com.example.finalexam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
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

public class AddPlace extends AppCompatActivity {

    private EditText placeName;
    private Button goButton;
    public ArrayList<String> cityList = new ArrayList<String>();
    private String city,trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        placeName = findViewById(R.id.placeName);
        goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    String city = placeName.getText().toString().trim();
                    new GetPlace().execute("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDRLa1nXCTHNjddKR-r-jLFv2kfeU80StE&placeid=" + city + "");
                }
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();

        if (network != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return false;

    }

    protected class GetPlace extends AsyncTask<String, Void, ArrayList<String>> {

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
                    if (json.equals("")) {
                        //  Toast.makeText(AddPlace.this, "No Cities Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    //JSONObject message = root.getJSONObject("message");
                    // JSONObject body = message.getJSONObject("body");

                    JSONArray cityArray = root.getJSONArray("predictions");
                    String description = "";
                    for (int i = 0; i < cityArray.length(); i++) {

                        JSONObject cityJSON = cityArray.getJSONObject(i);
                        description = cityJSON.getString("description");
                        cityList.add(description);
                    }

                    Log.d("cityList", "" + cityList);
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
            for (int i = 0; i < cities.size(); i++) {
                description.add(cities.get(i).toString());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(AddPlace.this);
            builder.setTitle("Select a place");
            builder.setItems(description.toArray(new String[description.size()]), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    city = description.get(item);
                    placeName.setText(description.get(item));
                    dialog.dismiss();

                }
            });
            builder.show();
        }
    }
}
