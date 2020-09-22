package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.noteapp.Login.LoginToken;

public class SignUp extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    EditText et_first_name;
    EditText et_last_name;
    EditText et_email_su;
    EditText et_pswd_su;
    EditText et_confirm_pswd_su;

    Button btn_signup_su;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_first_name = (EditText) findViewById(R.id.et_first_name_signup);
        et_last_name = (EditText) findViewById(R.id.et_last_name_signup);
        et_pswd_su = (EditText) findViewById(R.id.et_pswd_signup);
        et_email_su = (EditText) findViewById(R.id.et_email_signup);
        et_confirm_pswd_su = (EditText) findViewById(R.id.et_confirm_pswd_signup);

        sharedpreferences = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);

        btn_signup_su = (Button) findViewById(R.id.btn_signup_signup);
        btn_signup_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validate =
                        validate(et_first_name.getText().toString(), et_last_name.getText().toString(),
                                et_email_su.getText().toString(), et_pswd_su.getText().toString(),
                                et_confirm_pswd_su.getText().toString());

                if (validate) {
                    if (et_pswd_su.getText().toString().matches(et_confirm_pswd_su.getText().toString())) {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody formBody = new FormBody.Builder()
                                .add("name", et_first_name.getText().toString())
                                .add("email", et_email_su.getText().toString())
                                .add("password", et_pswd_su.getText().toString())
                                .build();

                        Request request = new Request.Builder()
                                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register")
                                .post(formBody).build();

                        client.newCall(request).enqueue(new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                try {

                                    final LoginUtil loginUtil = AllParsers.Parser.LoginSignupJsonParser.parseLogin(response.body().string());

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("auth", true);
                                    editor.putString("token", loginUtil.getToken());
                                    editor.commit();


                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUp.this, "User has been created", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                    Log.d("before intent", "done");
                                    Intent i = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(i);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {
                        Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            public boolean validate(String firstName,String lastName,String email,String password,String repeatpassword){
                boolean flag = true;
                StringBuilder error= new StringBuilder();
                if("".equalsIgnoreCase(firstName)){
                    error.append("firstName");
                    flag = false;
                } if("".equalsIgnoreCase(lastName)){
                    error.append(", lastName");
                    flag = false;
                } if("".equalsIgnoreCase(email)){
                    error.append(", email");
                    flag = false;
                }
                if("".equalsIgnoreCase(password)){
                    error.append(", password");
                    flag = false;
                } if("".equalsIgnoreCase(repeatpassword)){
                    error.append(", repeatpassword");
                    flag = false;
                }
                if(!flag)
                    Toast.makeText(SignUp.this,error+" cannot be empty", Toast.LENGTH_SHORT).show();
                return flag;
            }
        });
    }
}
