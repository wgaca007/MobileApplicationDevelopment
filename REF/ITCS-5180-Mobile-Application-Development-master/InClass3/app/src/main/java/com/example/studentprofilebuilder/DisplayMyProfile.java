package com.example.studentprofilebuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Display My Profile");
        Intent i = getIntent();
        Student obj = (Student)i.getSerializableExtra("Student Object");

        TextView name_vw = (TextView) findViewById(R.id.textView_DisplayName_Value);
        name_vw.setText(obj.getFirstName() + " " + obj.getLastName());

        TextView studentid_vw = (TextView) findViewById(R.id.textView_DisplayStudentID_Value);
        String id = Integer.toString(obj.getId());
        studentid_vw.setText(id);

        TextView department_vw = (TextView) findViewById(R.id.textView_DisplayDepartment_Value);
        department_vw.setText(obj.getDepartment());

        ImageView avatar_vw = (ImageView) findViewById(R.id.imageView_FinalAvatar);
        if(obj.getImageID() == 1){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_f_1));
        }else if(obj.getImageID() == 2){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_f_2));
        }else if(obj.getImageID() == 3){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_f_3));
        }else if(obj.getImageID() == 4){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_m_1));
        }else if(obj.getImageID() == 5){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_m_2));
        }else if(obj.getImageID() == 6){
            ImageView img = (ImageView)findViewById(R.id.imageView_FinalAvatar);
            img.setImageDrawable(getDrawable(R.drawable.avatar_m_3));
        }

        findViewById(R.id.button_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
