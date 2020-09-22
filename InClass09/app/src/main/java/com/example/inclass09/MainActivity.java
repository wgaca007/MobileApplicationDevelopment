package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class MainActivity extends AppCompatActivity {

public final static String MyValue="MyToken";
    EditText emailLogin,passwordLogin;
    Button loginButton,signupButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailLogin=findViewById(R.id.emailLogin);
        passwordLogin=findViewById(R.id.passwordLogin);
        loginButton=findViewById(R.id.loginButton);
        signupButton=findViewById(R.id.signupButton);
        sharedPreferences = getSharedPreferences(MyValue, Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(!"".equalsIgnoreCase(emailLogin.getText().toString()) && !"".equalsIgnoreCase(passwordLogin.getText().toString())){
    OkHttpClient client =  new OkHttpClient();
    RequestBody body = new FormBody.Builder()
            .add("email",emailLogin.getText().toString())
            .add("password",passwordLogin.getText().toString())
            .build();
    Request request = new Request.Builder().url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login").post(body).build();

    client.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            final LoginResponse loginResponse = SignupLoginUtil.JsonParser.parseInfo(response.body().string());
            if("ok".equalsIgnoreCase(loginResponse.getStatus().toString())){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",loginResponse.getToken());
                editor.putString("user_name",loginResponse.getUser_fname()+""+loginResponse.getUser_lname());
                editor.commit();
                Log.d("demo",""+loginResponse.getUser_fname());
                Toast.makeText(MainActivity.this,"Welcome " + loginResponse.getUser_fname()+" "+loginResponse.getUser_lname(),Toast.LENGTH_SHORT).show();
Intent i = new Intent(MainActivity.this,MailActivity.class);
startActivity(i);
finishAffinity();

            }
         else {
                    Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
});

}
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin.setText("");
                passwordLogin.setText("");
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);

            }
        });
    }
}
