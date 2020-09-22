package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {

    LinearLayout horizontal;
    TextView tv_fullname;
    ImageView iv_genderSelected;
    String fullname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        horizontal = findViewById(R.id.linearLayout);
        tv_fullname = horizontal.findViewById(R.id.fullname);
        iv_genderSelected = findViewById(R.id.iv_genderSelected);

        User user = (User)getIntent().getExtras().getSerializable(MainActivity.USER_KEY);

        if (getIntent() != null && getIntent().getExtras() != null) {

            if(user.genderSelected.matches("female")){
                iv_genderSelected.setImageDrawable(getDrawable(R.drawable.female));
            }
            else {
                iv_genderSelected.setImageDrawable(getDrawable(R.drawable.male));
            }

            fullname = user.Fname + " "+ user.Lname;
        }
        tv_fullname.setText(fullname);
    }
}