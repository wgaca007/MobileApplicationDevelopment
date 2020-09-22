package com.example.starter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Add, Sub;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Add = findViewById(R.id.add);
        Sub = findViewById(R.id.subtract);
        result = findViewById(R.id.result);

        Add.setOnClickListener(this);
        Sub.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                result.setText(data.getExtras().getInt("RESULT"));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add) {
            Intent i = new Intent(MainActivity.this, Addition.class);
            startActivityForResult(i, 100);
        }
        else {
            Intent i = new Intent(MainActivity.this, Subtract.class);
            startActivityForResult(i, 100);
        }
    }
}
