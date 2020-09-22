package com.example.noteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AddNote extends AppCompatActivity {

    Button post;
    EditText note_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        note_text = (EditText)findViewById(R.id.et_note_text_add_note);
        String text = note_text.getText().toString();

        post = (Button)findViewById(R.id.btn_post_addnote);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);
                String token = pref.getString("token", "");

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("text", note_text.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .addHeader("x-access-token", token)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post")
                        .post(formBody).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("Add Note OnResponse", "Message Received");

                    }
                });

                finish();
            }
        });


    }
}
