package com.example.listview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Email> emails = new ArrayList<Email>();
    Random rand = new Random();
    MyAdapter adapter;

    ArrayList<Email>listtobesent = new ArrayList<Email>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.byid).setOnClickListener(this);
        findViewById(R.id.byname).setOnClickListener(this);


        for(int i = 0; i < 100; i++){
            if(i%3 == 1){
                emails.add(new Email(rand.nextInt(100), "A"));
            }
            else if(i%3 == 2){
                emails.add(new Email(rand.nextInt(100), "F"));
            }
            else {
                emails.add(new Email(rand.nextInt(100), "Z"));
            }

        }

        ListView listView = findViewById(R.id.listview);
        adapter = new MyAdapter(this, R.layout.email_item, emails);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listtobesent.add(emails.get(position));
                emails.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        PrettyTime p = new PrettyTime();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse("19-10-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Date : " + p.format(date));

        String dateString="2019-10-21 16:00:47";
        try {
            Date mydate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(dateString);
            System.out.println("Date created: " + p.format(mydate));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.byid){
            Collections.sort(listtobesent, new Comparator<Email>() {
                @Override
                public int compare(Email o1, Email o2) {
                    return o1.id-o2.id;
                }
            });
        }
        else{
            Collections.sort(listtobesent, new Comparator<Email>() {
                @Override
                public int compare(Email o1, Email o2) {
                    return o1.name.compareTo(o2.name);
                }
            });
        }

        Intent i = new Intent(MainActivity.this, ListActivity.class);
        i.putParcelableArrayListExtra("list", listtobesent);
        startActivity(i);
    }
}
