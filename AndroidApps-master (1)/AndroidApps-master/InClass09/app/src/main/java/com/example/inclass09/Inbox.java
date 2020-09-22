package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

public class Inbox extends AppCompatActivity implements MyAdapter.ItemClickListener{

    Context ctx;
    SharedPreferences sharedPreferences;
    Gson gson;

    Request request;
    OkHttpClient client;

    ArrayList<MessageInfo> messagelist =  new ArrayList<>();
    RecyclerView myrecyclerview;
    RecyclerView.LayoutManager layoutManager;

    String token;

    Intent i;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ctx = getApplicationContext();
        client = new OkHttpClient();
        sharedPreferences = ctx.getSharedPreferences("email_preferences", MODE_PRIVATE);

        myrecyclerview = findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(Inbox.this);
        myrecyclerview.setLayoutManager(layoutManager);



        findViewById(R.id.createemail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Inbox.this, NewEmail.class);
                startActivity(i);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(MainActivity.TOKEN);
                editor.apply();

                i = new Intent(Inbox.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        if(sharedPreferences.contains(MainActivity.TOKEN) && (!sharedPreferences.getString(MainActivity.TOKEN, "").equals(""))){
            token = sharedPreferences.getString(MainActivity.TOKEN, "");
            String username = sharedPreferences.getString(MainActivity.USERNAME, "");
            ((TextView)findViewById(R.id.username)).setText(username);

            request = new Request.Builder()
                    .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                    .header("Authorization", "BEARER " + token)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Inbox.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Inbox.this, "Inbox Loading Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override public void onResponse(Call call, Response response) throws IOException {

                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                        String jsonData = responseBody.string();
                        final JSONObject Jobject = new JSONObject(jsonData);

                        if(Jobject.getString("status").equals("error")){
                            Inbox.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(Inbox.this, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        else if(Jobject.getString("status").equals("ok")) {
                            JSONArray messageinfoarray = Jobject.getJSONArray("messages");

                            for(int i=0;i<messageinfoarray.length();i++){
                                messagelist.add(new MessageInfo(messageinfoarray.getJSONObject(i).getString("id"), messageinfoarray.getJSONObject(i).getString("sender_fname")+ " " +
                                        messageinfoarray.getJSONObject(i).getString("sender_lname"), messageinfoarray.getJSONObject(i).getString("subject"), messageinfoarray.getJSONObject(i).getString("message"), messageinfoarray.getJSONObject(i).getString("created_at")));
                            }

                            Inbox.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter = new MyAdapter(messagelist, Inbox.this);
                                    myrecyclerview.setAdapter(myAdapter);
                                }
                            });


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    @Override
    public void onDeleteClick(final int position) {
        MessageInfo messageInfo = messagelist.get(position);

        request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/" + messageInfo.id)
                .header("Authorization", "BEARER " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Inbox.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Inbox.this, "Inbox Loading Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {

                    String jsonData = responseBody.string();
                    final JSONObject Jobject = new JSONObject(jsonData);

                    if(Jobject.getString("status").equals("error")){
                        Inbox.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(Inbox.this, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else if(Jobject.getString("status").equals("ok")) {

                        Inbox.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messagelist.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    @Override
    public void onDisplayClick(int position) {
        MessageInfo messageInfo = messagelist.get(position);

        i = new Intent(Inbox.this, DisplayMail.class);
        i.putExtra(MainActivity.MESSAGEINNFO, messageInfo);
        startActivity(i);
    }
}
