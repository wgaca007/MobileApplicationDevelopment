package com.example.recipepuppy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchHandlerInterface, DisplayFragment.onFinishDisplayIterface{

//    ArrayList<Recipe> recipes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new SearchFragment(), "tag_search_fragment").commit();

    }

    @Override
    public void finshButtonPressed() {

        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new SearchFragment();
        transaction1.replace(R.id.container, fragment);
        transaction1.commit();

    }

    @Override
    public void onGetRecipe(ArrayList<Recipe> myRecipe) {
        Log.d("MainActivity recipe", myRecipe.toString());
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new DisplayFragment();
        transaction1.replace(R.id.container, fragment);
        transaction1.commit();
        ((DisplayFragment) fragment).onDataReceived(myRecipe);


    }
}
