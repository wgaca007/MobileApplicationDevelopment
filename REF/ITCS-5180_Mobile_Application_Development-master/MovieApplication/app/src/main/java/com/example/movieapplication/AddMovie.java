package com.example.movieapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import static com.example.movieapplication.MainActivity.ADD_KEY;

public class AddMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        final EditText nameView = findViewById(R.id.nameView);
        final EditText descView = findViewById(R.id.descView);
        final Spinner genreSpinner = findViewById(R.id.genreSpinner);
        final SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView ratingView = findViewById(R.id.ratingView);
        final EditText yearView = findViewById(R.id.yearView);
        final EditText imdbView = findViewById(R.id.imdbView);
        Button addButton = findViewById(R.id.addButton);
        ratingView.setText("0");


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ratingView.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameView.getText().toString().matches("") | nameView.getText().toString().length() > 50 |
                descView.getText().toString().length() > 1000 | descView.getText().toString().matches("") |
                yearView.getText().toString().matches("") | imdbView.getText().toString().matches("") |
                        Integer.parseInt(yearView.getText().toString()) > 2019 | !URLUtil.isValidUrl(imdbView.getText().toString())){

                    Toast.makeText(AddMovie.this, "check the values you have entered", Toast.LENGTH_LONG).show();
                }else{
                    Log.d("spinner Value",String.valueOf(genreSpinner.getSelectedItemId()));
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    Movie m = new Movie(nameView.getText().toString(),descView.getText().toString(),imdbView.getText().toString(), (int) genreSpinner.getSelectedItemId(),Integer.parseInt(yearView.getText().toString()),seekBar.getProgress());
                    i.putExtra("key", (Serializable) m);
                    setResult(1,i);
                    Log.d("demo","result set");
                    finish();
                }
            }
        });
    }
}
