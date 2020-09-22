package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_IMAGE = "";

    RadioGroup rb_gender;
    RadioButton rb_female;
    RadioButton rb_male;
    ImageView iv_gender;
    EditText fname;
    EditText lname;
    private String gender;

    static String FirstName = "FNAME";
    static String LastName = "LNAME";
    static String GenderSelected = "GENDER";
    static String USER_KEY = "USER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rb_gender = findViewById(R.id.rb_gender);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        iv_gender = findViewById(R.id.imageView);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);



        rb_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_female:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.female));
                        gender = "female";
                        break;
                    case R.id.rb_male:
                        iv_gender.setImageDrawable(getDrawable(R.drawable.male));
                        gender = "male";
                        break;
                    default:
                        break;
                }
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick (View view){
                Intent intent =new Intent(MainActivity.this, DisplayActivity.class);
                /*intent.putExtra(FirstName, fname.getText().toString());
                intent.putExtra(LastName, lname.getText().toString());
                intent.putExtra(GenderSelected, gender);*/
                intent.putExtra(USER_KEY, new User(fname.getText().toString(), lname.getText().toString(), gender));
                startActivity(intent);

            }
        });

    }


}
