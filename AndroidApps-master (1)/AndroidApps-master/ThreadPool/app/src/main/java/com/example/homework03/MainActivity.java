//Group
package com.example.homework03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * a. Homework03
 * b. File Name.: Homework03
 * c. Full name of students of Groups1 41.: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SeekBar seekbar;
    private Button generate;
    private LinearLayout complexitylayout;
    private TextView complexity,min,max,avg;
    private ProgressBar pb;
    Double sum = 0.0, minimum, maximum, average;

    ExecutorService threadPool;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadPool = Executors.newFixedThreadPool(2);
        handler = new Handler(getApplicationContext().getMainLooper());

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

        pb.setVisibility(View.VISIBLE);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Double> numarray = HeavyWork.getArrayNumbers(seekbar.getProgress());
                sum = 0.0; minimum = numarray.get(0); maximum = numarray.get(0);
                for(Double s : numarray){
                    sum += s;
                }
                for(Double s : numarray){
                    if(s < minimum) minimum = s;
                    if(s > maximum) maximum = s;
                }

                average = sum/numarray.size();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        min.setText(""+minimum);
                        max.setText(""+maximum);
                        avg.setText(""+average);
                    }
                });
            }
        });
    }

}
