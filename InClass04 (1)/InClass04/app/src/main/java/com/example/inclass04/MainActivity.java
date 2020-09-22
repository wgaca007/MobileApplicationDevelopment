package com.example.inclass04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * a. InClass04
 * b. File Name.: InClass04
 * c. Full name of students of Groups1 41.: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SeekBar seekbar;
    private Button generate;
    private LinearLayout complexitylayout;
    private TextView complexity,min,max,avg;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generate = findViewById(R.id.generate);
        seekbar = findViewById(R.id.seekBar);

        complexitylayout = findViewById(R.id.comp);
        complexity = complexitylayout.findViewById(R.id.compval);
        complexity.setText(""+"0"+"   Times");

        pb = findViewById(R.id.progressBar);

        min = findViewById(R.id.minimum).findViewById(R.id.minval);
        max = findViewById(R.id.maximum).findViewById(R.id.maxval);
        avg = findViewById(R.id.average).findViewById(R.id.avgval);

        generate.setOnClickListener(this);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                complexity.setText(""+i+"   Times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(seekbar.getProgress() == 0){
            min.setText("");
            max.setText("");
            avg.setText("");
            return;
        }
        new Async().execute(seekbar.getProgress());
    }

    protected class Async extends AsyncTask<Integer,String, ArrayList<Double>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Double> numarray) {
            pb.setVisibility(View.INVISIBLE);
            min.setText(""+numarray.get(0));
            max.setText(""+numarray.get(1));
            avg.setText(""+numarray.get(2));
        }

        @Override
        protected ArrayList<Double> doInBackground(Integer... integers) {
            ArrayList<Double> numarray = HeavyWork.getArrayNumbers(integers[0]);
            Double sum = 0.0, avg, min = numarray.get(0), max = numarray.get(0);
            for(Double s : numarray){
                sum += s;
            }
            for(Double s : numarray){
                if(s < min) min = s;
                if(s > max) max = s;
            }

            avg = sum/numarray.size();

            return new ArrayList<>(Arrays.asList(min,max,avg));
        }
    }
}
