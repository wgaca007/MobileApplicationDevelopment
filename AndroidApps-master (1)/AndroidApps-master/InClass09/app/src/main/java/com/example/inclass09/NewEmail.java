package com.example.inclass09;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewEmail extends AppCompatActivity {

    Spinner spinner;

    OkHttpClient client;
    Request request;

    String token;

    Context ctx;
    SharedPreferences sharedPreferences;

    EditText subject, message;


    ArrayList<UserInfo> userslist = new ArrayList<UserInfo>(){{
        add(new UserInfo("User", "0"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);

        spinner = findViewById(R.id.spinner);

        subject = findViewById(R.id.editsubject);
        message = findViewById(R.id.messagecontent);

        ctx = getApplicationContext();
        client = new OkHttpClient();
        sharedPreferences = ctx.getSharedPreferences("email_preferences", MODE_PRIVATE);

        if(sharedPreferences.contains(MainActivity.TOKEN) && (!sharedPreferences.getString(MainActivity.TOKEN, "").equals(""))) {
            token = sharedPreferences.getString(MainActivity.TOKEN, "");
            request = new Request.Builder()
                    .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users")
                    .header("Authorization", "BEARER " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    NewEmail.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewEmail.this, "Inbox Loading Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override public void onResponse(Call call, Response response) throws IOException {

                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                        String jsonData = responseBody.string();
                        final JSONObject Jobject = new JSONObject(jsonData);

                        if(Jobject.getString("status").equals("error")){
                            NewEmail.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(NewEmail.this, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        else if(Jobject.getString("status").equals("ok")) {
                            JSONArray usersinfoarray = Jobject.getJSONArray("users");

                            for(int i=0;i<usersinfoarray.length();i++){
                                userslist.add(new UserInfo(usersinfoarray.getJSONObject(i).getString("fname") + " " + usersinfoarray.getJSONObject(i).getString("lname"), usersinfoarray.getJSONObject(i).getString("id")));
                            }

                            NewEmail.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createspinner(userslist);
                                }
                            });


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(spinner.getSelectedItemPosition() == 0){
                            Toast.makeText(NewEmail.this, "Please select the User", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        addEmail(token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.cancel_newemail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    void createspinner(ArrayList<UserInfo> userslist){

        ArrayAdapter<UserInfo> adapter = new ArrayAdapter<UserInfo>(this, android.R.layout.simple_spinner_item, userslist){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    void addEmail(String token) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("receiver_id", ((UserInfo)spinner.getSelectedItem()).id)
                .add("subject", subject.getText().toString())
                .add("message", message.getText().toString())
                .build();

        request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/add")
                .header("Authorization", "BEARER " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                NewEmail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewEmail.this, "Sending Email Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                NewEmail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewEmail.this, "Message is sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });


    }

}
