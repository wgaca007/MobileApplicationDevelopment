package com.example.inclass2a;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("BMI Calculator");

    }

    public void calculateBMI(View view) {
        EditText weight = (EditText) findViewById(R.id.editText_Weight);
        EditText height_feet = (EditText) findViewById(R.id.editText_Height_feet);
        EditText height_inches = (EditText) findViewById(R.id.editText_Height_inches);

        TextView display = (TextView) findViewById(R.id.textView_DisplayBMI);
        TextView display_Status = (TextView) findViewById(R.id.textView_DisplayStatus);

        float BodyMassIndex;

        if (!weight.getText().toString().equals("") && !height_feet.getText().toString().equals("") && !height_inches.getText().toString().equals("")) {
            BodyMassIndex = BMICalculator(weight.getText().toString(), height_feet.getText().toString(), height_feet.getText().toString());

            display.setText("Calculated BMI is: " + BodyMassIndex);
            if (BodyMassIndex <= 18.5) {
                display_Status.setText("You are underweight");
            } else if (BodyMassIndex > 18.5 && BodyMassIndex <= 24.9) {
                display_Status.setText("You have Normal weight");
            } else if (BodyMassIndex >= 25 && BodyMassIndex < 29.9) {
                display_Status.setText("You are Overweight");
            } else {
                display_Status.setText("You are Obese");
            }
            Toast toast = Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (weight.getText().toString().equals("")) {
                display.setText("Invalid Input: Weight field is empty");
            } else if (height_feet.getText().toString().equals("") || height_inches.getText().toString().equals("")) {
                display.setText("Invalid Input: Height field is empty");
            }
        }
    }

    public float BMICalculator(String weight, String height_feet, String height_inches) {
        float weightInPound = Float.parseFloat(weight);
        float heightInFeet = Float.parseFloat(height_feet);
        float inches = Float.parseFloat(height_inches);
        float heightInInches, BMI;

        heightInInches = heightInFeet * 12 + inches;

        BMI = ((weightInPound / (heightInInches * heightInInches)) * 703);
        return BMI;
    }
}
