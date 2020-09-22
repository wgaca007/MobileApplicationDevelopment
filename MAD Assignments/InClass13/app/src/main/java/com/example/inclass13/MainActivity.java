package com.example.inclass13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button newTripButton;
    private RecyclerView recyclerView;
    public static final int REQUEST_CODE=1;
    TripRecyclerViewAdapter adapter;
    ArrayList<String> recyclerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newTripButton = findViewById(R.id.newTripButton);
        recyclerView = findViewById(R.id.recyclerView);
        newTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(i);
            }
        });



        Bundle extras = getIntent().getExtras();
            String city = extras.getString("CITY_NAME");
            String trip = extras.getString("TRIP_NAME");
        recyclerList.clear();
            recyclerList.add(city);
            recyclerList.add(trip);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TripRecyclerViewAdapter(this, recyclerList);
            recyclerView.setAdapter(adapter);



    }
}
