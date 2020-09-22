package com.example.googleapiapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Loc> locations = null;
    private Marker mStart;
    private Marker mEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String json = null;
        try {
            InputStream is = getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            //Log.d("demo", json + "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Location loc = gson.fromJson(json, Location.class);

        locations = loc.getListPoints();

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

        for(Loc mylocation : locations){
            LatLng my_val = new LatLng(Double.parseDouble(mylocation.getLatitude()), Double.parseDouble(mylocation.getLongitude()));
            latLngArrayList.add(my_val);
        }

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .addAll(latLngArrayList)
                .width(5)
                .color(Color.RED));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mStart = mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(0)).title("Start Location"));
                mStart.showInfoWindow();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(int i=0;i<latLngArrayList.size();i++) {
                    builder.include(latLngArrayList.get(i));
                    Log.d("demo", "Value " + i + ": " + latLngArrayList.get(i).toString());// + " Second: " + latLngArrayList.get(1).toString());
                    //Log.d("demo2", "Last: " + latLngArrayList.get(latLngArrayList.size() - 1).toString() + " SecondLast: " + latLngArrayList.get(latLngArrayList.size() - 2).toString());

                }

                Log.d("demo2", "Size: " + latLngArrayList.size());
                LatLngBounds bo = builder.build();
                CameraUpdate cu2 = CameraUpdateFactory.newLatLngBounds(bo,50);
                mEnd = mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(latLngArrayList.size()-1)).title("End Location"));
                mEnd.showInfoWindow();

                mMap.moveCamera(cu2);
            }
        });
    }
}
