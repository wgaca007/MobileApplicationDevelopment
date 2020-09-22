package com.example.homework7a;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TripPagerAdapter extends FragmentStatePagerAdapter {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Trip> trips = new ArrayList<>();
    Fragment fragment;

    public TripPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
            fragment = new TripsFragment();
        else if(i == 1)
            fragment = new UsersFragment();
        else
            fragment = new TripsFragment();

        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(TripsFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "USERS";
        else if(position == 1)
            return "PLACES";
        else
            return "MAP";

    }
}
