package com.example.trip;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TripMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Places> locations = null;
    City trip_city = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);

        if (getIntent() != null && getIntent().getExtras() != null) {
            final Trips tripDetails = (Trips) getIntent().getExtras().getSerializable(CreateTrip.MUSIC_LACK_KEY);
            locations = tripDetails.getTripPlaces();
            trip_city = tripDetails.getCity();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        for (Places mylocation : locations) {
            LatLng my_val = new LatLng(Double.parseDouble(mylocation.getLati()), Double.parseDouble(mylocation.getLogi()));
            mMap.addMarker(new MarkerOptions().position(my_val).title(mylocation.getPlaceName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(my_val));
            latLngArrayList.add(my_val);
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < latLngArrayList.size(); i++) {
                    builder.include(latLngArrayList.get(i));
                    Log.d("demo", "Value " + i + ": " + latLngArrayList.get(i).toString());// + " Second: " + latLngArrayList.get(1).toString());
                    //Log.d("demo2", "Last: " + latLngArrayList.get(latLngArrayList.size() - 1).toString() + " SecondLast: " + latLngArrayList.get(latLngArrayList.size() - 2).toString());

                }

                // Add a marker in Sydney and move the camera
                LatLng city = new LatLng(Double.parseDouble(trip_city.getLatitude()), Double.parseDouble(trip_city.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(city).title(trip_city.getCityName()));


                LatLngBounds bo = builder.build();
                CameraUpdate cu2 = CameraUpdateFactory.newLatLngBounds(bo, 150);
                mMap.moveCamera(cu2);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(city));
            }
        });
    }
}
