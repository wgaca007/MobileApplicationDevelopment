package com.example.homework7a;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.homework7a.dummy.DummyContent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements UsersFragment.OnListFragmentInteractionListener, TripsFragment.OnListFragmentInteractionListener{

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Intent i;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String userid;

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, options);
        firebaseAuth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finishAffinity();
                }
            }
        };

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_24px);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_airport);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_group);

        if(getIntent() != null && getIntent().getExtras() != null){
            userid = getIntent().getStringExtra("userid");
        }

        //i = new Intent(MainActivity.this, ChatActivity.class);
        //startActivity(i);

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
             case "logout":
                 FirebaseAuth.getInstance().signOut();
                 mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(MainActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                     }
                 });
                 finish();
                 break;
             case "myprofile":
                 Intent i = new Intent(MainActivity.this,MyProfile.class);
                 startActivity(i);
         }

         return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(Trip item) {

        i = new Intent(MainActivity.this, TripActivity.class);
        i.putExtra("selectedtrip", item);
        startActivity(i);

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

}
