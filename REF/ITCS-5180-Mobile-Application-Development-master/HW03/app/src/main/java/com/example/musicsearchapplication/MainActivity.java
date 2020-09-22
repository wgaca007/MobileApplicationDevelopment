package com.example.musicsearchapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements GetSongs.IData {

    static String MUSIC_TRACK_KEY = "TRACK";
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("iTunes Music Search");

        //----Seek Bar Handling---------------------------------------------

        TextView limitValue = findViewById(R.id.textView_LimitValue);
        limitValue.setText("10");

        SeekBar limitSeekBar = findViewById(R.id.seekBar_Limit);
        limitSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 10) {
                    SeekBar limitSeekBar = findViewById(R.id.seekBar_Limit);
                    limitSeekBar.setProgress(10);
                    TextView limitValue = findViewById(R.id.textView_LimitValue);
                    limitValue.setText("" + 10);
                } else {
                    TextView limitValue = findViewById(R.id.textView_LimitValue);
                    limitValue.setText("" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //----Seek Bar Handling Ends---------------------------------------------

        //----Button Reset Handling---------------------------------------------

        Button reset = findViewById(R.id.button_Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchbar = findViewById(R.id.editText_SearchBar);
                searchbar.setText("");

                SeekBar limitSeekBar = findViewById(R.id.seekBar_Limit);
                limitSeekBar.setProgress(10);

                Switch priceDate = findViewById(R.id.switch_PriceDate);
                priceDate.setChecked(true);

                ListView result = findViewById(R.id.ListView_Result);
                result.setAdapter(null);
            }
        });

        //----Button Reset Handling Ends---------------------------------------------

        //----Button Search Handling---------------------------------------------

        Button search = findViewById(R.id.button_Search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);


                EditText searchbar = findViewById(R.id.editText_SearchBar);
                String searchvalue = searchbar.getText().toString();

                SeekBar limitSeekBar = findViewById(R.id.seekBar_Limit);

                if (!searchvalue.isEmpty()) {
                    Log.d("demo", "https://itunes.apple.com/search?term=\"" + searchvalue + "\"&limit=" + limitSeekBar.getProgress());
                    new GetSongs(MainActivity.this).execute("https://itunes.apple.com/search?term=\"" + searchvalue + "\"&limit=" + limitSeekBar.getProgress());
                } else
                    Toast.makeText(MainActivity.this, "Search Bar empty", Toast.LENGTH_LONG).show();
            }
        });

        //----Button Search Handling Ends---------------------------------------------

        Switch swt = findViewById(R.id.switch_PriceDate);
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false)
                    flag = 0;
                else
                    flag = 1;
            }
        });
    }


    //----Handling Results from GetSongs---------------------------------------------
    @Override
    public void handleData(final ArrayList<TrackDetails> data) {
        if(flag == 0)
            Collections.sort(data, TrackDetails.PriceComparator);
        else
            Collections.sort(data, TrackDetails.DateComparator);

        ListView listView = findViewById(R.id.ListView_Result);
        TackDetailAdapter adapter = new TackDetailAdapter(this, R.layout.content_track_detail_display, data);
        listView.setAdapter(adapter);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, display_activity.class);
                intent.putExtra(MUSIC_TRACK_KEY, data.get(position));
                startActivity(intent);
            }
        });

    }
    //----Handling Results from GetSongs Ends---------------------------------------------
}