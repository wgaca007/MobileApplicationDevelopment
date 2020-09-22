
package com.example.finalexpense;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.finalexpense.AddExpense;
import com.example.finalexpense.EditExpense;
import com.example.finalexpense.ExpenseDetails;
import com.example.finalexpense.ExpenseInformation;
import com.example.finalexpense.R;
import com.example.finalexpense.ShowExpense;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddExpense.OnFragmentInteractionListener, ShowExpense.OnFragmentInteractionListener, ExpenseInformation.OnFragmentInteractionListener,
        EditExpense.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ShowExpense.newInstance();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void sendExpense(ExpenseDetails ed) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ShowExpense.newInstance();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}