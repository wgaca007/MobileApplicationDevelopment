package com.example.homework04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AddMovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Spinner spinner;
    TextView ratingvalue;
    ArrayList<String> items; ;
    SeekBar seekbar;
    int progressval = 0;
    int title = 100;
    int id;

    EditText name,description,year,imdb;

    Button addmovie;
    Movie editmovieobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        items = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.genre)));

        spinner = findViewById(R.id.spinner);
        ratingvalue = findViewById(R.id.ratingvalue);
        seekbar = findViewById(R.id.seekBar);
        name = findViewById(R.id.name);
        description = findViewById(R.id.descriptiontext);
        year = findViewById(R.id.year);
        addmovie = findViewById(R.id.addmovie);
        imdb = findViewById(R.id.imdb);
        //year = ((TextView)findViewById(R.id.year)).getText();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items.toArray(new String[items.size()])){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;

                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if(getIntent() != null && getIntent().getExtras() != null) {

            String addedit = getIntent().getStringExtra(MainActivity.ADD);
            setTitle(addedit.equals("ADD") ? "Add Movie" : "Edit Movie");
            addmovie.setText(addedit.equals("ADD") ? "Add Movie": "Save Changes");
            id = getIntent().getIntExtra(MainActivity.MOVIE_OBJ, 0);

            if(addedit.equals("EDIT")) {
                MainActivity.db.collection("Movies").document(String.valueOf(id))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("EditData", "DocumentSnapshot data: " + document.getData());
                                editmovieobj = document.toObject(Movie.class);
                                if (editmovieobj != null) {
                                    name.setText(editmovieobj.getName());
                                    description.setText(editmovieobj.getDescription());
                                    spinner.setSelection(items.indexOf(editmovieobj.getGenre()) != -1 ? items.indexOf(editmovieobj.getGenre()) : 0);
                                    ratingvalue.setText(String.valueOf(editmovieobj.getRating()));
                                    seekbar.setProgress(editmovieobj.getRating());
                                    year.setText(String.valueOf(editmovieobj.getYear()));
                                    imdb.setText(editmovieobj.getImdb());
                                }
                            } else {
                                Log.d("Error", "Movie does not exist");
                                // Toast.makeText(AddMovieActivity.this, " Movie Does Not exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddMovieActivity.this, "Failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                            Log.d("Error", "get failed with ", task.getException());
                        }
                    }
                });
            }

        }

        ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressval = progress;
                ratingvalue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addmovie.setOnClickListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onClick(View v) {

        if(name.getText().toString().equals("")){
            name.setError("Set a Valid Name");
        }
        else if(description.getText().toString().equals("")) {
            Toast.makeText(this, "Select a Valid Description", Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select a Genre", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(year.getText().toString()) < 1800 || Integer.parseInt(year.getText().toString()) > 2019){
            year.setError("Set a Valid Year");
        }
        else if(imdb.getText().toString().equals("")){
            Toast.makeText(this, "Set the IMDB URL", Toast.LENGTH_SHORT).show();
        }
        else {
            /*Intent intent = new Intent();
            intent.putExtra(MainActivity.MOVIE_OBJ, new Movie(name.getText().toString(),
                    description.getText().toString(), spinner.getSelectedItem().toString(), imdb.getText().toString(), seekbar.getProgress(), Integer.parseInt(this.year.getText().toString())));
            setResult(RESULT_OK, intent);
            finish();*/
           String add1 = getIntent().getStringExtra(MainActivity.ADD);
            //id=++MainActivity.index;
            id = add1.equals("ADD") ? ++MainActivity.index : id;
            Movie movie = new Movie(name.getText().toString(),
                    description.getText().toString(), spinner.getSelectedItem().toString(), imdb.getText().toString(), seekbar.getProgress(), Integer.parseInt(this.year.getText().toString()));

            MainActivity.db.collection("Movies").document(String.valueOf(id))
                    .set(movie)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Info", "DocumentSnapshot successfully written!");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error writing document", e);
                        }
                    });
        }
    }
}
