package com.example.starter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Subtract extends AppCompatActivity implements View.OnClickListener {

    private EditText number1, number2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtract);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
    }

    @Override
    public void onClick(View view) {
        int n1 = Integer.valueOf(number1.getText().toString());
        int n2 = Integer.valueOf(number2.getText().toString());

        Intent responseintent = new Intent();
        responseintent.putExtra("RESULT", n1-n2);
        setResult(RESULT_OK, responseintent);
        finish();
    }
}
