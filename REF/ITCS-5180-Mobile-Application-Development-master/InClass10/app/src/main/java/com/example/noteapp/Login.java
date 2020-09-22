package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    public static final String LoginToken = "TOKEN" ;

    String token = "";

    SharedPreferences sharedpreferences;

    EditText et_email_login;
    EditText et_pswd_login;

    Button btn_login_login;
    Button btn_signup_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_email_login = (EditText)findViewById(R.id.et_login_email);
        et_pswd_login = (EditText)findViewById(R.id.et_login_psswd);

        sharedpreferences = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);

        btn_login_login = (Button)findViewById(R.id.button_login_login);
        btn_signup_login = (Button)findViewById(R.id.button_signup_login);
        btn_signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });


        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("email", et_email_login.getText().toString())
                        .add("password", et_pswd_login.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login")
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
                            editor.putBoolean("auth",true);
                            editor.putString("token", loginUtil.getToken());
                            editor.commit();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this,"User Logged In",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);

                        }

                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }
}
