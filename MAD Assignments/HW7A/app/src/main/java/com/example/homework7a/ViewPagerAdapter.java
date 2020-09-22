package com.example.homework7a;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Trip> trips = new ArrayList<>();
    Fragment fragment;

    public ViewPagerAdapter(FragmentManager fm) {
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

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";

    }
}

