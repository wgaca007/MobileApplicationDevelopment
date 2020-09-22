package com.example.threading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button buttonStart, buttonStop;
    private TextView textView;

    private Boolean mstop;
    private int count = 0;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG,"Thread id:" + Thread.currentThread().getId());

        buttonStart = findViewById(R.id.startthread);
        buttonStop = findViewById(R.id.stopthread);
        textView= findViewById(R.id.counter);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        handler = new Handler(getApplicationContext().getMainLooper());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.startthread:
                mstop = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(mstop){
                            try {
                                Thread.sleep(1000);
                                count++;
                            }
                            catch (InterruptedException e){
                                Log.i(TAG, e.getMessage());
                            }
                            Log.i(TAG, "Thread running while loop: " + Thread.currentThread().getId() + ": count value:" + count);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(" " + count);
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.stopthread:
                mstop = false;
                break;
        }
    }
}
