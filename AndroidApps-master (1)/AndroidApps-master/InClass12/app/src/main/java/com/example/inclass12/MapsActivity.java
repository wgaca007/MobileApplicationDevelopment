package com.example.inclass12;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
Name: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
Assignment: INClass12
Group: Groups1 41
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> latlnglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String jsonstring = loadJSONFromRaw(this);

        try {
            JSONObject jsonObject = new JSONObject(jsonstring);
            JSONArray jsonArray = jsonObject.getJSONArray("points");

            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject latlngobject = jsonArray.getJSONObject(i);
                LatLng l = new LatLng(latlngobject.getDouble("latitude"), latlngobject.getDouble("longitude"));
                latlnglist.add(l);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //Your code where exception occurs goes here...

                // Add a marker in Sydney and move the camera
                LatLng sydney = latlnglist.get(0);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Start Location"));
                mMap.addMarker(new MarkerOptions().position(latlnglist.get(latlnglist.size()-1)).title("End Location"));


                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(latlnglist);
                polylineOptions.clickable(false);


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                List<LatLng> arr = polylineOptions.getPoints();
                for(int i = 0; i < arr.size();i++){
                    builder.include(arr.get(i));
                }
                LatLngBounds bounds = builder.build();
                int padding = 40; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.addPolyline(polylineOptions);
                mMap.animateCamera(cu);
                //mMap.moveCamera();
                //mMap.setLatLngBoundsForCameraTarget();
            }
        });




    }

    public String loadJSONFromRaw(Context context) {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.trip);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
