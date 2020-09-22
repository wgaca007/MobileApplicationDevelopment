package com.example.expenseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Expense> mDescribable = new ArrayList<Expense>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    static String MUSIC_TRACK_KEY = "TRACK";
    double total_expense = 0;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("demo", "Menu selected: " + MainActivity.mDescribable.size());
        ArrayList<Expense> localexpense = new ArrayList<Expense>();
        localexpense = MainActivity.mDescribable;
        switch (item.getItemId()) {
            case R.id.cost:
                if (localexpense.size() > 0) {
                    Collections.sort(localexpense, Expense.CostComparator);
                    mAdapter = new DisplayExpenseAdatpter(localexpense);
                    recyclerView.setAdapter(mAdapter);
                }
                return true;
            case R.id.date:
                if (localexpense.size() > 0) {
                    Collections.sort(localexpense, Expense.DateComparator);
                    mAdapter = new DisplayExpenseAdatpter(localexpense);
                    recyclerView.setAdapter(mAdapter);
                }
                return true;
            case R.id.reset:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Delete All");
                alert.setMessage("Are you sure you want to delete?");
                final ArrayList<Expense> finalLocalexpense = new ArrayList<Expense>();
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        mDatabase.child("users").removeValue();
                        total_expense = 0;
                        TextView textView = findViewById(R.id.tv_display_total);
                        textView.setText("$" + total_expense);


                        mAdapter = new DisplayExpenseAdatpter(finalLocalexpense);
                        recyclerView.setAdapter(mAdapter);

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("demo", "im here");
        final ArrayList<Expense> mDescribable = new ArrayList<Expense>();
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("users/");

        recyclerView = findViewById(R.id.rv_expense);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.mDescribable.clear();
                total_expense = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Expense myexpense = ds.getValue(Expense.class);
                    MainActivity.mDescribable.add(myexpense);
                    total_expense = total_expense + myexpense.getExpenseCost();
                }
                Log.d("demo", "I'm here tooo");
                if (MainActivity.mDescribable.size() > 0) {
                    Collections.sort(MainActivity.mDescribable, Expense.DateComparator);
                    mAdapter = new DisplayExpenseAdatpter(MainActivity.mDescribable);
                    recyclerView.setAdapter(mAdapter);
                    TextView textView = findViewById(R.id.tv_display_total);
                    textView.setText("$" + total_expense);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton bt = findViewById(R.id.floatingActionButton4);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddExpense.class);
                startActivity(intent);
            }
        });

        TextView textView = findViewById(R.id.tv_display_total);
        textView.setText("$" + total_expense);

    }
}
