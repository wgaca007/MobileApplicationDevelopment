package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.noteapp.Login.LoginToken;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageButton logout;

    Button add_note;

    ArrayList<NotesUtil> my_notes = new ArrayList<>();

    TextView tv_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = (ImageButton)findViewById(R.id.btn_logout_main);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                Intent intent = new Intent(MainActivity.this, Login.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tv_user_name = (TextView)findViewById(R.id.tv_user_name_main);
        SharedPreferences pref = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);
        String token = pref.getString("token", "");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("x-access-token", token)
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    final MeUtil meUtil = AllParsers.Parser.MeParser.parseMe(response.body().string());
                    tv_user_name.setText(meUtil.user_name);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        add_note = (Button)findViewById(R.id.btn_add_main);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddNote.class);
                startActivity(intent);
            }
        });
        createRecyclerView();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        createRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);
        Boolean check = pref.getBoolean("auth", false);
        if(!check){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    public void createRecyclerView(){
        SharedPreferences pref = getSharedPreferences(LoginToken, Context.MODE_PRIVATE);
        String token = pref.getString("token", "");
        OkHttpClient client = new OkHttpClient();
        Request get_notes_request = new Request.Builder()
                .addHeader("x-access-token", token)
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall").build();

        client.newCall(get_notes_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    my_notes = AllParsers.Parser.NoteParser.parseNotes(response.body().string());
                    Log.d("getALl", my_notes.toString());
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);

                            DisplayNotesAdapter.AdapterInterface adapterInterface = new DisplayNotesAdapter.AdapterInterface() {
                                @Override
                                public void onDelete(int index) {

                                    Request request = new Request.Builder()
                                            .addHeader("x-access-token", token)
                                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                            .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+my_notes.get(index).getNote_id()).build();

                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Log.d("Delete Respone", "Success");
                                            runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {

                                                    createRecyclerView();

                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onDisplay(int index) {
                                    Log.d("display", my_notes.get(index).toString());
                                    Intent intent = new Intent(MainActivity.this, DisplayNote.class);
                                    intent.putExtra("Key", my_notes.get(index));
                                    startActivity(intent);

                                }
                            };

                            RecyclerView.Adapter adapter = new DisplayNotesAdapter(my_notes,
                                    adapterInterface,
                                    MainActivity.this);

                            recyclerView.setAdapter(adapter);

                        }
                    });



                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }
}
