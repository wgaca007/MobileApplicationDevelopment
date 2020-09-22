package com.example.inclass10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ByYearOrRating extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    String selectedsorting;
    int index = 0;

    TextView genre, rating, year, imdb, description,title, heading;
    ImageButton first,prev,next,last;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_year_or_rating);

        heading = findViewById(R.id.title);
        title = findViewById(R.id.titleview).findViewById(R.id.titleval);
        description = findViewById(R.id.descriptiontext);
        description.setMovementMethod(new ScrollingMovementMethod());
        genre = findViewById(R.id.genreview).findViewById(R.id.genretext);
        rating = findViewById(R.id.ratingview).findViewById(R.id.ratingval);
        year = findViewById(R.id.yearview).findViewById(R.id.yearval);
        imdb = findViewById(R.id.imdbtext);

        first = findViewById(R.id.first);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        last = findViewById(R.id.last);
        finish = findViewById(R.id.finish);

        first.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        last.setOnClickListener(this);
        finish.setOnClickListener(this);

        if(getIntent() != null && getIntent().getExtras() != null){
            movieArrayList = getIntent().getParcelableArrayListExtra(MainActivity.MOVIE_OBJ);
            selectedsorting = getIntent().getStringExtra(MainActivity.MOVIE_BY_WHAT);


            if(selectedsorting.equals("moviebyyear")){
                setTitle("Movies By Year");
                heading.setText("Movies By Year");
            }
            else {
                setTitle("Movies By Rating");
                heading.setText("Movies By Rating");
            }
            setvalues(0);
        }
    }

    public void setvalues(int index) {

        title.setText(movieArrayList.get(index).getName());
        description.setText(movieArrayList.get(index).getDescription());
        genre.setText(movieArrayList.get(index).getGenre());
        rating.setText(movieArrayList.get(index).getRating() + " / 5");
        year.setText(""+movieArrayList.get(index).getYear());
        imdb.setText(movieArrayList.get(index).getImdb());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.first)
            setvalues(index=0);
        else if(v.getId() == R.id.prev)
            setvalues((--index) <= 0 ? (index=0) : index);
        else if(v.getId() == R.id.next)
            setvalues((++index) >= movieArrayList.size() ? (index=movieArrayList.size()-1) : index);
        else if(v.getId() == R.id.last)
            setvalues(index = movieArrayList.size() - 1);
        else
            finish();
    }
}
