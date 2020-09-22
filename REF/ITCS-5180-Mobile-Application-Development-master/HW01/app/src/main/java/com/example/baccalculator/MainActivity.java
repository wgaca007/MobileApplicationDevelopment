package com.example.baccalculator;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    float BACLevel = 0, gender_constant = 0, weight = 0, liquid_ounce = 0, gender = 0, denominator = 0, flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

        setTitle("BAC Calculator");

        SeekBar AlcoholPer1 = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
        AlcoholPer1.setEnabled(false);

        findViewById(R.id.button_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUserName = (EditText) findViewById(R.id.editText_Weight);
                String strUserName = txtUserName.getText().toString();
                if (strUserName.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your weight ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    weight = Float.parseFloat(strUserName);
                    ToggleButton Gender = (ToggleButton) findViewById(R.id.toggleButton_Gender);
                    if (Gender.isChecked() == true) {
                        gender_constant = (float) 0.55;
                        gender = 1;
                        Log.d("gender_constant", Float.toString(gender_constant));
                    } else {
                        gender_constant = (float) 0.68;
                        gender = 2;
                        Log.d("gender_constant", Float.toString(gender_constant));
                    }
                    if (flag == 1) {
                        denominator = weight * gender_constant;
                        flag = 2;
                    }
                    if (denominator == weight * gender_constant) {
                        denominator = weight * gender_constant;
                        Log.d("Denominator changes", "Denominator same");
                    } else {
                        Log.d("Denominator changes", "Denominator changed");
                        BACLevel = (BACLevel * denominator) / (weight * gender_constant);
                        denominator = weight * gender_constant;
                        setLevelAndStatus(BACLevel);
                    }

                    Button AddDrink = (Button) findViewById(R.id.button_AddDrink);
                    AddDrink.setEnabled(true);

                    SeekBar AlcoholPer2 = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
                    AlcoholPer2.setEnabled(true);

                    RadioButton oz1 = (RadioButton) findViewById(R.id.radioButton_1oz);
                    oz1.setEnabled(true);
                    RadioButton oz5 = (RadioButton) findViewById(R.id.radioButton_5oz);
                    oz5.setEnabled(true);
                    RadioButton oz12 = (RadioButton) findViewById(R.id.radioButton_12oz);
                    oz12.setEnabled(true);
                }
            }
        });

        final SeekBar AlcoholPer = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
        final int yourStep = 5;

        AlcoholPer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / yourStep)) * yourStep;
                seekBar.setProgress(progress);
                TextView textView = (TextView) findViewById(R.id.textView_DisplayAlcohol);
                textView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.button_AddDrink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup drink_Size = (RadioGroup) findViewById(R.id.radioGroup);
                int selectedId = drink_Size.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                int ounce = 0, per = 0;

                if (radioButton.getText().equals("1 oz"))
                    ounce = 1;
                else if (radioButton.getText().equals("5 oz"))
                    ounce = 5;
                else
                    ounce = 12;

                SeekBar AlcoPer = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
                per = AlcoPer.getProgress();

                liquid_ounce = ounce * per * (float) 0.01;
                BACLevel = BACLevel + calculateBAC(liquid_ounce, denominator);

                setLevelAndStatus(BACLevel);
            }
        });

        findViewById(R.id.button_Reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BACLevel = 0;
                gender_constant = 0;
                weight = 0;
                liquid_ounce = 0;
                denominator = 0;
                flag = 1;

                ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
                progress.setProgress(0);

                Button AddDrink = (Button) findViewById(R.id.button_AddDrink);
                AddDrink.setEnabled(false);

                SeekBar AlcoholPer2 = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
                AlcoholPer2.setEnabled(false);
                AlcoholPer.setProgress(5);

                RadioButton oz1 = (RadioButton) findViewById(R.id.radioButton_1oz);
                oz1.setEnabled(false);
                oz1.setChecked(true);
                RadioButton oz5 = (RadioButton) findViewById(R.id.radioButton_5oz);
                oz5.setEnabled(false);
                RadioButton oz12 = (RadioButton) findViewById(R.id.radioButton_12oz);
                oz12.setEnabled(false);

                Button save = (Button) findViewById(R.id.button_Save);
                save.setEnabled(true);

                EditText wt = (EditText) findViewById(R.id.editText_Weight);
                wt.setText("");

                TextView tw = (TextView) findViewById(R.id.textView_BACLevelValue);
                tw.setText("");

                TextView tw2 = (TextView) findViewById(R.id.textView_Status);
                tw2.setText("");
            }
        });

    }

    public float calculateBAC(float liquid_ounce, float denominator) {
        return (liquid_ounce * (float) 6.24 / (denominator));
    }

    public void setLevelAndStatus(float BACLevel) {
        TextView levelValue = findViewById(R.id.textView_BACLevelValue);
        levelValue.setText(String.format("%.2f", BACLevel));

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(25);
        progress.setProgress((int) (BACLevel * 100));
        Log.d("Progress bar", "BACLevel value: " + Float.toString(BACLevel));
        Log.d("Progress bar", "Progress bar value: " + Integer.toString((int)(BACLevel * 100)));

        TextView status = findViewById(R.id.textView_Status);
        if (BACLevel <= 0.08) {
            status.setText("You're safe");
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (BACLevel > 0.08 && BACLevel < 0.20) {
            status.setText("Be careful...");
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        } else if (BACLevel >= 0.20 && BACLevel < 0.25) {
            status.setText("Over the limit");
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else if (BACLevel >= 0.25) {
            status.setText("Over the limit");
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            Toast.makeText(getApplicationContext(), "No more drinks for you ", Toast.LENGTH_SHORT).show();

            Button AddDrink = (Button) findViewById(R.id.button_AddDrink);
            AddDrink.setEnabled(false);
            Button save = (Button) findViewById(R.id.button_Save);
            save.setEnabled(false);

            SeekBar AlcoholPer2 = (SeekBar) findViewById(R.id.seekBar_AlcoholPer);
            AlcoholPer2.setEnabled(false);

            RadioButton oz1 = (RadioButton) findViewById(R.id.radioButton_1oz);
            oz1.setEnabled(false);
            RadioButton oz5 = (RadioButton) findViewById(R.id.radioButton_5oz);
            oz5.setEnabled(false);
            RadioButton oz12 = (RadioButton) findViewById(R.id.radioButton_12oz);
            oz12.setEnabled(false);
        }
    }
}
