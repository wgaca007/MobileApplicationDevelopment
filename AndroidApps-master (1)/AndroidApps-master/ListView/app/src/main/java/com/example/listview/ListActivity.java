package com.example.listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    MyAdapter adapter;
    ListView listView;
    ArrayList<Email>emails = new ArrayList<>();
    String[] items = {"Home", "Quit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(getIntent() != null && getIntent().getExtras() != null){

            emails = getIntent().getParcelableArrayListExtra("list");

            listView = findViewById(R.id.sortedlistview);
            adapter = new MyAdapter(this, R.layout.email_item, emails);
            listView.setAdapter(adapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("hello");
            builder.setItems(items, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        }

        findViewById(R.id.taketothird).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("com.example.listview.intent.action.VIEW");
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(i);
            }
        });

    }
}
