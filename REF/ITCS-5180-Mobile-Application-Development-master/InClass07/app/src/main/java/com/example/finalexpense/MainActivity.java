package com.example.finalexpense;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddExpense.OnFragmentInteractionListener, ShowExpense.OnFragmentInteractionListener, ExpenseInformation.OnFragmentInteractionListener{

    ArrayList<ExpenseDetails> data = new ArrayList<ExpenseDetails>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ShowExpense.newInstance(data);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void sendExpense(ExpenseDetails ed) {
        data.add(ed);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ShowExpense.newInstance(data);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
