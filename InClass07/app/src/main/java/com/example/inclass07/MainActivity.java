package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyAdapter.TrackClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int selectedid;
    ProgressBar pb;



    private RadioButton trackrating, artistrating;
    RadioGroup radioGroup;
    SeekBar sb;
    EditText songname;
    TextView limit;

    ArrayList<Song> tracklists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        songname = findViewById(R.id.songname);
        sb = findViewById(R.id.seekBar);
        trackrating = findViewById(R.id.trackrating);
        artistrating = findViewById(R.id.artistrating);
        radioGroup = findViewById(R.id.radioGroup);
        limit = findViewById(R.id.limit);
        pb = findViewById(R.id.progressBar);


        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                makerequest(checkedId == R.id.trackrating);
            }
        });





        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                limit.setText("Limit: " + sb.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makerequest(trackrating.isChecked());
            }
        });


    }

    private void makerequest(Boolean istrackrating){

        recyclerView.setAlpha(0);

        if (!isConnected()) {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if(songname.getText().toString().equals("")){
            songname.setError("Enter the song name");
        }else {
            tracklists.clear();
            pb.setVisibility(View.VISIBLE);
            String url = "http://api.musixmatch.com/ws/1.1/track.search?q=" + songname.getText().toString()
                    + "&page_size=" + sb.getProgress() +"&"
                    + (istrackrating ? "s_track_rating" : "s_artist_rating")
                    + "=desc"
                    + "&apikey=a949878b9590f55dacfc7a194906a066";
            new GetTracks().execute(url);
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
    public void onTrackClick(int position) {
        Song song = tracklists.get(position);
        if(!isConnected()){
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else if(song.trackshareurl.trim().length() == 0){
            Toast.makeText(this, "No URL Found", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(song.trackshareurl));
            startActivity(i);
        }
    }

    private class GetTracks extends AsyncTask<String, Void, ArrayList<Song>>{

        @Override
        protected ArrayList<Song> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            URL url = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(json.equals("")){
                        Toast.makeText(MainActivity.this, "No Songs Found", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
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

                    return tracklists;

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
        protected void onPostExecute(ArrayList<Song> tracklists) {
            pb.setVisibility(View.INVISIBLE);
            if(tracklists.size() == 0){
                Toast.makeText(MainActivity.this, "No Tracks Found", Toast.LENGTH_SHORT).show();
                return;
            }

            recyclerView.setAlpha(1);

            MyAdapter myadapter = new MyAdapter(tracklists, MainActivity.this);
            recyclerView.setAdapter(myadapter);

        }
    }

}
