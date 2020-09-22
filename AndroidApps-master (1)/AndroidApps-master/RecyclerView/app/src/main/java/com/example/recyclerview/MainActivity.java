package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.itemClick {

    RecyclerView myrecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText name;

    ArrayList<String> s = new ArrayList<String>(){{
        add("one");
        add("two");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);

        myrecyclerview = findViewById(R.id.myrecyclerview);

        layoutManager = new LinearLayoutManager(MainActivity.this);
        myrecyclerview.setLayoutManager(layoutManager);

        myrecyclerview.setHasFixedSize(true);

        final MyAdapter myadapter = new MyAdapter(s, this);

        myrecyclerview.setAdapter(myadapter);


        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.add(name.getText().toString());
                myadapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.remove(s.size()-1);
                myadapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Log.d("Clicked position :", position+"");
    }
}
