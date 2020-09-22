package com.example.trip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    static String MUSIC_TRACK_KEY = "TRACK";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    final static ArrayList<Trips> mDescribable = new ArrayList<Trips>();

    //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_Close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.button_CreateTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateTrip.class);
                //intent.putExtra(MainActivity.MUSIC_TRACK_KEY, userid);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_YourTrips);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("trips/");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.mDescribable.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Trips mytrip = ds.getValue(Trips.class);
                    MainActivity.mDescribable.add(mytrip);
                }
                if (MainActivity.mDescribable.size() > 0) {
                    Log.d("demo", "Trip Name: " + mDescribable.get(0).getTripName());
                    mAdapter = new TripAdapter2(MainActivity.mDescribable);
                    recyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
