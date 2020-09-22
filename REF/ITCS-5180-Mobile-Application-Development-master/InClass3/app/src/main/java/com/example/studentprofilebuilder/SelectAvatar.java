package com.example.studentprofilebuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Avatar");

        findViewById(R.id.imageView_Female1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent female1 = new Intent();
                int image_number = 1;
                female1.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, female1);
                finish();
            }
        });

        findViewById(R.id.imageView_Female2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent female2 = new Intent();
                int image_number = 2;
                female2.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, female2);
                finish();
            }
        });

        findViewById(R.id.imageView_Female3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent female3 = new Intent();
                int image_number = 3;
                female3.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, female3);
                finish();
            }
        });

        findViewById(R.id.imageView_Male1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent male1 = new Intent();
                int image_number = 4;
                male1.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, male1);
                finish();
            }
        });

        findViewById(R.id.imageView_Male2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent male2 = new Intent();
                int image_number = 5;
                male2.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, male2);
                finish();
            }
        });

        findViewById(R.id.imageView_Male3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent male3 = new Intent();
                int image_number = 6;
                male3.putExtra(MainActivity.VALUE_KEY, image_number);
                setResult(RESULT_OK, male3);
                finish();
            }
        });
    }

}
