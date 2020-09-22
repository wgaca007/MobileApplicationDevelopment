package com.example.studentprofilebuilder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE = 100;
    public static final String VALUE_KEY = "img";
    public static int IMAGE_NUMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Profile");


        //This is for Selecting the Avatar
        findViewById(R.id.imageView_MyAvatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfile_intent = new Intent(MainActivity.this, SelectAvatar.class);
                startActivityForResult(myProfile_intent, REQ_CODE);
            }
        });

        //This is for checking and saving the data
        findViewById(R.id.button_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fn = (EditText) findViewById(R.id.editText_FirstName);
                EditText ln = (EditText) findViewById(R.id.editText_LastName);
                EditText sid = (EditText) findViewById(R.id.editText_StudentID);
                ImageView my_avatar = (ImageView) findViewById(R.id.imageView_MyAvatar);

                RadioGroup rdo_grp = (RadioGroup) findViewById(R.id.radioGroup_Department);
                int selectedID = rdo_grp.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedID);

                String fn_text = fn.getText().toString().trim();
                String ln_text = ln.getText().toString().trim();
                String sid_text = sid.getText().toString().trim();
                String Department;
                if (rdo_grp.getCheckedRadioButtonId() != -1) {
                    Department = radioButton.getText().toString();
                }
                else
                    Department = "";

                if (fn_text != null && ln_text != null && sid_text != null && Department != null &&
                        !fn_text.equals("") && !ln_text.equals("") && !sid_text.equals("") && !Department.equals("") && IMAGE_NUMBER != 0) {
                    int sid_int = Integer.parseInt(sid_text);
                    Student obj = new Student(sid_int, fn_text, ln_text, IMAGE_NUMBER, Department);
                    Intent i = new Intent(MainActivity.this, DisplayMyProfile.class);
                    i.putExtra("Student Object", obj);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Missing Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //This is for Getting Result from Select Avatar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                int img_number = data.getExtras().getInt(VALUE_KEY);
                if (img_number == 1) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_f_1));
                    IMAGE_NUMBER = 1;
                } else if (img_number == 2) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_f_2));
                    IMAGE_NUMBER = 2;
                } else if (img_number == 3) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_f_3));
                    IMAGE_NUMBER = 3;
                } else if (img_number == 4) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_m_1));
                    IMAGE_NUMBER = 4;
                } else if (img_number == 5) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_m_2));
                    IMAGE_NUMBER = 5;
                } else if (img_number == 6) {
                    ImageView img = (ImageView) findViewById(R.id.imageView_MyAvatar);
                    img.setImageDrawable(getDrawable(R.drawable.avatar_m_3));
                    IMAGE_NUMBER = 6;
                }
            }
        }
    }
}
