package com.example.midtermtemplate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetDataAsync.setDataInterface{

    ProgressDialog progressDialog;
    int limit = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isConnected()){
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_SHORT);
        }

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        final EditText editTextSearch = (EditText)findViewById(R.id.editText);
        final ListView listView = findViewById(R.id.listView);
        final TextView setLimit = (TextView) findViewById(R.id.tvLimit);
        final Button search = (Button)findViewById(R.id.buttonSearch);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);


//        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
//
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if (checkedId == R.id.radioButtonTrack){
//                    String query = editTextSearch.getText().toString();
//                    //https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q=Justin&page_size=5
//                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
//                            "&s_track_rating=desc";
//                    new GetDataAsync(MainActivity.this).execute(my_api_url);
//
//                }
//                else{
//                    String query = editTextSearch.getText().toString();
//                    //https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q=Justin&page_size=5
//                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
//                            "&s_artist_rating=desc";
//                    new GetDataAsync(MainActivity.this).execute(my_api_url);
//
//                }
//
//            }
//        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                limit = progress+5;
                setLimit.setText("Limit: "+limit);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.radioButtonTrack) {
                    Log.d("demo", "Track Sort");
                    String query = editTextSearch.getText().toString();
                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
                            "&s_track_rating=desc";
                    new GetDataAsync(MainActivity.this).execute(my_api_url);
                }

                else {
                    Log.d("demo", "Artist Sort");
                    String query = editTextSearch.getText().toString();

                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
                            "&s_artist_rating=desc";
                    new GetDataAsync(MainActivity.this).execute(my_api_url);
                }
            }
        });

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

    @Override
    public void startProcessing() {


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();

    }

    @Override
    public void finishProcessing() {
        progressDialog.dismiss();
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final EditText editTextSearch = (EditText)findViewById(R.id.editText);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonTrack){
                    String query = editTextSearch.getText().toString();
                    //https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q=Justin&page_size=5
                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
                            "&s_track_rating=desc";
                    new GetDataAsync(MainActivity.this).execute(my_api_url);

                }
                else{
                    String query = editTextSearch.getText().toString();
                    //https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q=Justin&page_size=5
                    String my_api_url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=f126b582c17d10329daf15284bc0543f&q="+query+"&page_size="+limit+
                            "&s_artist_rating=desc";
                    new GetDataAsync(MainActivity.this).execute(my_api_url);

                }
            }
        });

    }

    @Override
    public void setData(final ArrayList<Track> myResult) {

        ListView listView = findViewById(R.id.listView);
        TrackAdapter adapter = new TrackAdapter(this, R.layout.song_row, myResult);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Values", "Clicked item "+ position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(myResult.get(position).trackUrl.toString()));
                startActivity(intent);
            }
        });

    }
}
