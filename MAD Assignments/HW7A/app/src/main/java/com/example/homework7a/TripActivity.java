package com.example.homework7a;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.homework7a.dummy.DummyContent;
import com.google.android.material.tabs.TabLayout;

public class TripActivity extends AppCompatActivity implements TripsFragment.OnListFragmentInteractionListener, UsersFragment.OnListFragmentInteractionListener{

    ViewPager trippager;
    TripPagerAdapter tripPagerAdapter;
    Intent i;

    String chatroomid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);


        trippager = findViewById(R.id.trippager);
        tripPagerAdapter = new TripPagerAdapter(getSupportFragmentManager());
        trippager.setAdapter(tripPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(trippager);

        if(getIntent() != null && getIntent().getExtras() != null){
            chatroomid = ((Trip)getIntent().getSerializableExtra("selectedtrip")).chatroomid;
        }

    }

    @Override
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
    }

    @Override
    public void onListFragmentInteraction(Trip item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
