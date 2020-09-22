package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    ArrayList<Source> sourceslist = new ArrayList<>();
    ListView sourceslistview;
    ProgressBar pb;

    static String selectedsource = "selectedsource";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progressBar);
        sourceslistview = findViewById(R.id.sourceslistview);
        if(!isConnected()){
        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }
        else {
        pb.setVisibility(View.VISIBLE);
        new GetSources().execute("https://newsapi.org/v2/sources?apiKey=a717dffdd6074b95be6a2c9e7fc2d841");
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


    private class GetSources extends AsyncTask<String, Void, ArrayList<Source>>{

        @Override
        protected ArrayList<Source> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(json.equals("")){
                        Toast.makeText(MainActivity.this, "No Sources Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONArray sourcesJSONArray = root.getJSONArray("sources");

                    for(int i=0;i < sourcesJSONArray.length(); i++){

                        JSONObject sourceJSON = sourcesJSONArray.getJSONObject(i);

                        sourceslist.add(new Source(sourceJSON.getString("id"), sourceJSON.getString("name")));

                    }

                    return sourceslist;

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
        protected void onPostExecute(ArrayList<Source> sources) {
            super.onPostExecute(sources);
            pb.setVisibility(View.INVISIBLE);
            ArrayAdapter<Source> sourcesadapter = new ArrayAdapter<Source>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sourceslist);
            sourceslistview.setAdapter(sourcesadapter);

            sourceslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isConnected()){
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, NewsActivity.class);
                        Source source = (Source) parent.getItemAtPosition(position);
                        i.putExtra(selectedsource, source);
                        startActivity(i);
                    }
                }
            });


        }
    }

}
