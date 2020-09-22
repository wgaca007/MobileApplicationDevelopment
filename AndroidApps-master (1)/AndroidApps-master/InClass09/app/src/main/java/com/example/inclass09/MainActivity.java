package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
Group: Groups1 41
Name: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA,
Assignment: InClass09
 */

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    Request request;

    EditText emailid, password;

    Context ctx;
    SharedPreferences sharedPreferences;
    Gson gson;

    static String TOKEN = "userlogintoken";
    static String USERNAME = "username";
    static String MESSAGEINNFO = "messageinfo";

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailid = findViewById(R.id.emaillogin);
        password = findViewById(R.id.password);

         client = new OkHttpClient();

        ctx = getApplicationContext();
        sharedPreferences = ctx.getSharedPreferences("email_preferences", MODE_PRIVATE);

        if(sharedPreferences.contains(TOKEN) && !sharedPreferences.getString(TOKEN, "").equals("")){
            i = new Intent(this, Inbox.class);
            startActivity(i);
            finish();
        }

         findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 RequestBody formBody = new FormBody.Builder()
                         .add("email", emailid.getText().toString())
                         .add("password", password.getText().toString())
                         .build();

                 request = new Request.Builder()
                         .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                         .post(formBody)
                         .build();

                 client.newCall(request).enqueue(new Callback() {
                     @Override public void onFailure(Call call, IOException e) {
                         e.printStackTrace();
                         MainActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(MainActivity.this, "Login was not successful", Toast.LENGTH_SHORT).show();
                             }
                         });
                     }

                     @Override public void onResponse(Call call, Response response) throws IOException {
                         try (ResponseBody responseBody = response.body()) {



                             String jsonData = responseBody.string();
                             final JSONObject Jobject = new JSONObject(jsonData);

                             if(Jobject.getString("status").equals("error")){
                                 MainActivity.this.runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             Toast.makeText(MainActivity.this, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 });
                             }
                             else if(Jobject.getString("status").equals("ok")) {

                                 SharedPreferences.Editor editor = sharedPreferences.edit();
                                 editor.putString(TOKEN, Jobject.getString("token"));
                                 editor.putString(USERNAME, Jobject.getString("user_fname") + " " + Jobject.getString("user_lname"));
                                 editor.apply();

                                 i = new Intent(MainActivity.this, Inbox.class);
                                 startActivity(i);
                                 finish();
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 });
             }
         });

         findViewById(R.id.signupmailer).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 i = new Intent(MainActivity.this, SignUp.class);
                 startActivity(i);
                 finish();
             }
         });

    }
}
