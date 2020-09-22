package com.example.homework06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

/*
Group id: GROUPS 1 41
Names : AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
Assignment: Homework06
 */

public class MainActivity extends AppCompatActivity implements MyProfile.OnFragmentInteractionListener, SelectAvtar.OnFragmentInteractionListener, DisplayMyProfile.OnFragmentInteractionListener{

    MyProfile myprofile;
    SelectAvtar selectAvtar;
    DisplayMyProfile displayMyProfile;

    StudentInfo studentInfo;
    Gson gson = new Gson();

    String studentInfojsonstring;

    Context ctx;
    SharedPreferences sharedPreferences;

    int selectedimageresource;

    static Boolean isSharedPreferenceAvailabe = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myprofile = new MyProfile();


        selectAvtar = new SelectAvtar();
        displayMyProfile= new DisplayMyProfile();

        ctx = getApplicationContext();
        sharedPreferences = ctx.getSharedPreferences("studentinfo", MODE_PRIVATE);
        if(sharedPreferences.getString("studentinfojsonstring", "").equals("")){
            getSupportFragmentManager().beginTransaction().add(R.id.container, myprofile,"myprofile").commit();
        }
        else{
            isSharedPreferenceAvailabe = true;
            displayMyProfile.setcomponents(sharedPreferences);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, displayMyProfile, "displaymyprofile").commit();
        }

    }


    @Override
    public void onFragmentInteraction(View v) {
        switch(v.getId()){
            case R.id.selectavtar:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SelectAvtar(),"selectavtar").addToBackStack(null).commit();
                break;
            case R.id.f1:
            case R.id.m1:
            case R.id.f2:
            case R.id.m2:
            case R.id.f3:
            case R.id.m3:
                    ImageView ig = (ImageView) v;
                    myprofile = (MyProfile) getSupportFragmentManager().findFragmentByTag("myprofile");
                    myprofile.selectedimageresource = (String) ig.getTag();
                    getSupportFragmentManager().popBackStack();
                    break;

        }

    }

    @Override
    public void setInfo(StudentInfo studentInfo) {

            studentInfojsonstring = gson.toJson(studentInfo);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("studentinfojsonstring", studentInfojsonstring);
            editor.apply();

            displayMyProfile.setcomponents(sharedPreferences);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, displayMyProfile, "displaymyprofile").addToBackStack(null).commit();

    }

    @Override
    public void EditProfile() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            myprofile.setcomponents(sharedPreferences);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myprofile, "myprofile").commit();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
