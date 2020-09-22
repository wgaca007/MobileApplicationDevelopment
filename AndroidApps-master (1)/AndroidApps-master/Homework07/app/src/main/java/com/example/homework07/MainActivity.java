package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UsersFragment.OnListFragmentInteractionListener, TripsFragment.OnListFragmentInteractionListener{

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Intent i;


    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String TAG = "tag";

    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_24px);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_airport);

        //tabLayout.getTabAt(2).setIcon(R.drawable.ic_group);

/*        if(getIntent() != null && getIntent().getExtras() != null){
            userid = getIntent().getStringExtra("userid");
        }*/

        //i = new Intent(MainActivity.this, ChatActivity.class);
        //startActivity(i);
        setTitle("My Trip Account");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         switch(item.getTitle().toString()){
             case "addtrip":
                  i = new Intent(MainActivity.this, CreateTripActivity.class);
                  startActivity(i);
                  break;
             case "myprofile":
                 Intent i = new Intent(MainActivity.this,MyProfile.class);
                 startActivity(i);
                 break;
             case "logout":
                 FirebaseAuth.getInstance().signOut();
                 /*mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                     }
                 });*/
                 i = new Intent(MainActivity.this, LoginActivity.class);
                 startActivity(i);
                 finish();
                 break;
             case "Approve Requests":
                 db.collection("trips").whereEqualTo("creator", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                             //for(String userid : )
                             approveRequests(documentSnapshot);
                         Toast.makeText(MainActivity.this, "Approved Trip Requests", Toast.LENGTH_SHORT).show();
                         }

                 });
         }

         return true;
    }

    void approveRequests(QueryDocumentSnapshot documentSnapshot){
        ArrayList<String> users = (ArrayList<String>) documentSnapshot.get("triprequests");
        db.collection("trips").document(documentSnapshot.getId()).update("triprequests", new ArrayList<>());
        db.collection("trips").document(documentSnapshot.getId()).update("users", FieldValue.arrayUnion(users.toArray(new String[users.size()])));
        for(String user : users){
            db.collection("Users").document(user).update("trips", FieldValue.arrayUnion(documentSnapshot.getId()));
        }
    }


    @Override
    public void onListFragmentInteraction(Trip item) {

        i = new Intent(MainActivity.this, TripActivity.class);
        i.putExtra("selectedtrip", item);
        startActivity(i);

    }

    @Override
    public void onListFragmentInteraction(User item) {

    }
}
