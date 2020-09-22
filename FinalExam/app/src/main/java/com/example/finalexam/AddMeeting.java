package com.example.finalexam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import io.grpc.InternalNotifyOnServerBuild;

public class AddMeeting extends AppCompatActivity implements View.OnClickListener {

    private EditText titleName;
    private Button addPlace, setDate, setTime, save;
    public static int REQUEST_CODE = 1;
    public static int hour;
    public static int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        titleName = findViewById(R.id.titleName);
        addPlace = findViewById(R.id.addPlace);
        setDate = findViewById(R.id.setDate);
        setTime = findViewById(R.id.setTime);
        save = findViewById(R.id.save);
        addPlace.setOnClickListener(this);
        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if(REQUEST_CODE=1  resultCode == RESULT_OK){


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addPlace) {
            Intent i = new Intent(AddMeeting.this, AddPlace.class);
            startActivityForResult(i, REQUEST_CODE);
        }
        if (view.getId() == R.id.setDate) {
//            showTimePickerDialog();

            showDatePickerDialog(view);


        }

        if (view.getId() == R.id.setTime) {
            showTimePickerDialog(view);

        }
        if (view.getId() == R.id.save) {

        }
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           Log.d("demo",""+hourOfDay);
           hour = view.getHour();
           min=view.getMinute();
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
}
