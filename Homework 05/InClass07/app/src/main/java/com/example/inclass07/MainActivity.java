package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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

public class MainActivity extends AppCompatActivity {
    public ArrayList<Song> songslist = new ArrayList<>();
    ListView sourceslistview;
    EditText songName;
    TextView limit;
    SeekBar seekBar;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sourceslistview=findViewById(R.id.listView);
        seekBar = findViewById(R.id.seekBar);
        limit=findViewById(R.id.limit);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                limit.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    //Toast.makeText("No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    songName = findViewById(R.id.songName);
                    int i = seekBar.getProgress();
                    new GetSources().execute("http://api.musixmatch.com/ws/1.1/track.search?q=" + songName.getText() + "&page_size=" + i + "&s_artist_rating=desc&apikey=bb7b688f93e5ab7e5d4f040868b556ba");
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

    private class GetSources extends AsyncTask<String, Void, ArrayList<Song>> {

        @Override
        protected ArrayList<Song> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                Log.d("demo",""+url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


               if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if (json.equals("")) {
                        Toast.makeText(MainActivity.this, "No songs Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONArray songsJSONArray = root.getJSONArray("Songs");

                    for (int i = 0; i < songsJSONArray.length(); i++) {

                        JSONObject sourceJSON = songsJSONArray.getJSONObject(i);

                       //songslist.add(new Song(sourceJSON.getString("id"), sourceJSON.getString("name")));

                    }

                   // return songslist;

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
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
            ArrayAdapter<Song> sourcesadapter = new ArrayAdapter<Song>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, songslist);
            sourceslistview.setAdapter(sourcesadapter);

            sourceslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!isConnected()) {
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {
                       // Intent i = new Intent(MainActivity.this, NewsActivity.class);
                      //  Source source = (Source) parent.getItemAtPosition(position);
                      //  i.putExtra(selectedsource, source);
                     //   startActivity(i);
                    }
                }
            });


        }
    }
}
