package com.example.sampleapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    String name = "Batman Begins hello sample";
    Spinner spinner;
    ArrayList<String> result = new ArrayList<>(Arrays.asList("Batman","Begins","Now","Sample"));
    ListView listView;
    RecyclerView myrecyclerview;

    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        myrecyclerview = findViewById(R.id.myrecyclerview);

        //String[] result = name.split(" ");

        for(int i = 0; i < result.size(); i++){
            sb.append(result.get(i)+" ");
        }

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("pick a movie");

        builder.setItems(result, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("demo", which+"");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/

        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> myAdpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result){
            @Override
            public boolean isEnabled(int position) {
                //super.isEnabled(position);

                if(position == 0)return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);

                return view;

            }
        };


        myAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdpter);

        //listView.setAdapter(new CustomAdapter(this, R.layout.sample_item, result));

        myrecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        myrecyclerview.setAdapter(new RecycleAdapter(result));



    }
}
