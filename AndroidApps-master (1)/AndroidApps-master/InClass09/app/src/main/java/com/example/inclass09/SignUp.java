package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUp extends AppCompatActivity {

    EditText fname, lname, email, choosepassword, repeatpassword;

    Request request;
    OkHttpClient client;
    SharedPreferences sharedPreferences;
    Context ctx;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        client = new OkHttpClient();

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.emailidsignup);
        choosepassword = findViewById(R.id.choosepassword);
        repeatpassword = findViewById(R.id.repeatpassword);

        ctx = getApplicationContext();
        sharedPreferences = ctx.getSharedPreferences("email_preferences", MODE_PRIVATE);

        (findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fname.getText().toString().trim().length() == 0){
                    fname.setError("Set the first name");
                    return;
                }
                else if(lname.getText().toString().trim().length() == 0){
                    lname.setError("Set the last name");
                    return;
                }
                else if(email.getText().toString().trim().length() == 0){
                    email.setError("Email cannot be empty");
                    return;
                }
                else if(choosepassword.getText().toString().trim().length() == 0){
                    choosepassword.setError("Choose Password cannot be empty");
                    return;
                }
                else if(repeatpassword.getText().toString().trim().length() == 0){
                    repeatpassword.setError("Repeat password cannot be empty");
                    return;
                }
                else if(!choosepassword.getText().toString().equals(repeatpassword.getText().toString())){
                    Toast.makeText(SignUp.this, "Repeated Passwords should be same", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("email", email.getText().toString())
                        .add("password", choosepassword.getText().toString())
                        .add("fname", fname.getText().toString())
                        .add("lname", lname.getText().toString())
                        .build();

                request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        SignUp.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUp.this, "Sign up was not successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("success");

                        try (ResponseBody responseBody = response.body()) {

                            String jsonData = responseBody.string();
                            final JSONObject Jobject = new JSONObject(jsonData);

                            if (Jobject.getString("status").equals("error")) {
                                SignUp.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(SignUp.this, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else {
                                SignUp.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                            Toast.makeText(SignUp.this, "User has been created", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(MainActivity.TOKEN, Jobject.getString("token"));
                                editor.putString(MainActivity.USERNAME, Jobject.getString("user_fname") + " " + Jobject.getString("user_lname"));
                                editor.apply();

                                i = new Intent(SignUp.this, Inbox.class);
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
    }
}
