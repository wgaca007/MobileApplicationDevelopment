package com.example.homework07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TripPagerAdapter extends FragmentStatePagerAdapter {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Trip> trips = new ArrayList<>();
    Fragment fragment;
    String tripid;

    public TripPagerAdapter(FragmentManager fm, String tripid) {
        super(fm);
        this.tripid = tripid;
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
            fragment = new UsersFragment(tripid);
        else if(i == 1)
            fragment = new UsersFragment(tripid);
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
