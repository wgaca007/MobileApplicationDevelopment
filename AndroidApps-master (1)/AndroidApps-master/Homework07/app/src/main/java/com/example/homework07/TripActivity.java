package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class TripActivity extends AppCompatActivity implements TripsFragment.OnListFragmentInteractionListener, UsersFragment.OnListFragmentInteractionListener{

    ViewPager trippager;
    TripPagerAdapter tripPagerAdapter;
    Intent i;

    String chatroomid;
    String tripid;

    Trip trip;
    ImageView tripcoverphoto;
    TextView title, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);


        if(getIntent() != null && getIntent().getExtras() != null){
            trip = ((Trip)getIntent().getSerializableExtra("selectedtrip"));
        }

        tripcoverphoto = findViewById(R.id.tripcardview).findViewById(R.id.tripcoverphoto);
        title = findViewById(R.id.tripcardview).findViewById(R.id.tripitemtitle);
        location = findViewById(R.id.tripcardview).findViewById(R.id.location);

        chatroomid = trip.chatroomid;
        tripid = trip.tripid;

        Picasso.get().load(trip.getCoverphotourl()).into(tripcoverphoto);
        title.setText(trip.title);
        location.setText(trip.location);

        trippager = findViewById(R.id.trippager);
        tripPagerAdapter = new TripPagerAdapter(getSupportFragmentManager(), tripid);
        trippager.setAdapter(tripPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(trippager);

        findViewById(R.id.chatroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(TripActivity.this, ChatActivity.class);
                i.putExtra("chatroomid", chatroomid);
                startActivity(i);
            }
        });

        setTitle("Trip Room");

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trip_items, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getTitle().toString()){
            case "chatroom":
                i = new Intent(TripActivity.this, ChatActivity.class);
                i.putExtra("chatroomid", chatroomid);
                startActivity(i);
        }

        return true;
    }*/

    @Override
    public void onListFragmentInteraction(Trip item) {

    }

    @Override
    public void onListFragmentInteraction(User item) {

    }
}
