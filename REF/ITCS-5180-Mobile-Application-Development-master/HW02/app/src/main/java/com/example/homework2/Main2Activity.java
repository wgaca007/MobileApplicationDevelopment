package com.example.homework2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Passwords
        ArrayList passwordsThreads = new ArrayList<String>();
        ArrayList passwordsAsync = new ArrayList<String>();

        //Scroll Views
        ScrollView scrollViewThreads = (ScrollView) findViewById(R.id.svPswdThread);
        ScrollView scrollViewAsync = (ScrollView) findViewById(R.id.svPswdAsync);

        //Layout
        LinearLayout threadLayout = (LinearLayout)findViewById(R.id.threadlayout);
        LinearLayout asyncLayout = (LinearLayout) findViewById(R.id.asynclayout);

        //Layout Inflator
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;

        //Button to Finish
        final Button finish = (Button) findViewById(R.id.buttonFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.seekBarPC1.setProgress(1);
                MainActivity.seekBarPC2.setProgress(1);
                MainActivity.seekBarPL2.setProgress(1);
                MainActivity.seekBarPL1.setProgress(1);

                MainActivity.textViewPC1.setText("1");
                MainActivity.textViewPC2.setText("1");

                MainActivity.textViewPL1.setText("7");
                MainActivity.textViewPL2.setText("7");

                finish();

            }
        });


        if(getIntent() != null && getIntent().getExtras() != null){
            passwordsThreads = (ArrayList) getIntent().getExtras().getSerializable(MainActivity.PSWD_VALS);
            Log.d("passwords", String.valueOf(passwordsThreads));

            for (int idx = 0; idx < passwordsThreads.size(); idx++){

                view = layoutInflater.inflate(R.layout.text_layout, threadLayout, false);

                TextView textView = (TextView)view.findViewById(R.id.text);
                textView.setText((String) passwordsThreads.get(idx));

                threadLayout.addView(textView);
            }

            passwordsAsync = (ArrayList) getIntent().getStringArrayListExtra("Key");
            for (int idx = 0; idx < passwordsAsync.size(); idx++){

                view = layoutInflater.inflate(R.layout.text_layout, asyncLayout, false);

                TextView textView = (TextView)view.findViewById(R.id.text);
                textView.setText((String) passwordsAsync.get(idx));

                asyncLayout.addView(textView);
            }

        }
    }
}