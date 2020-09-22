package com.example.movieapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button addMovie,editMovie,deleteMovie,yearMovie,ratingMovie;
    public static String ADD_KEY = "key";
    public ArrayList<Movie> moviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMovie = findViewById(R.id.addMovie);
        editMovie = findViewById(R.id.editMovie);
        deleteMovie = findViewById(R.id.deleteMovie);
        yearMovie = findViewById(R.id.yearMovie);
        ratingMovie = findViewById(R.id.ratingMovie);

        moviesList.add(new Movie("Rio","When Blu, a domesticated macaw from small-town Minnesota, meets the fiercely independent Jewel, he takes off on an adventure to Rio de Janeiro with the bird of his dreams.","https://www.imdb.com/title/tt1436562/",1,2011,4));
        moviesList.add(new Movie("The God Father","The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.","https://www.imdb.com/title/tt0068646/?ref_=nv_sr_1?ref_=nv_sr_1",0,1972,5));
        moviesList.add(new Movie("Grown Ups","After their high school basketball coach passes away, five good friends and former teammates reunite for a Fourth of July holiday weekend.","https://www.imdb.com/title/tt1375670/?ref_=nv_sr_1?ref_=nv_sr_1",2,2010,4));
        moviesList.add(new Movie("Pele: Birth of a Legend","le's meteoric rise from the slums of Sao Paulo to leading Brazil to its first World Cup victory at the age of 17 is chronicled in this biographical drama.","imdb.com/title/tt0995868/?ref_=nv_sr_1?ref_=nv_sr_1",3,2016,3));
        moviesList.add(new Movie("The Lion King","After the murder of his father, a young lion prince flees his kingdom only to learn the true meaning of responsibility and bravery.","https://www.imdb.com/title/tt6105098/?ref_=nv_sr_1?ref_=nv_sr_1",4,2019,4));
        moviesList.add(new Movie("IT","In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.","https://www.imdb.com/title/tt1396484/?ref_=nv_sr_2?ref_=nv_sr_2",5,2017,4));
        moviesList.add(new Movie("Joker","A gritty character study of Arthur Fleck, a man disregarded by society.","https://www.imdb.com/title/tt7286456/?ref_=nv_sr_1?ref_=nv_sr_1",6,2019,5));
        moviesList.add(new Movie("John Wick","An ex-hit-man comes out of retirement to track down the gangsters that killed his dog and took everything from him.","https://www.imdb.com/title/tt2911666/?ref_=nv_sr_2?ref_=nv_sr_2",7,2014,5));

        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddMovie.class);
                startActivityForResult(intent,1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            if(data.getExtras()!=null){
                moviesList.add((Movie) data.getExtras().get("key"));
                Log.d("demo","Movie added");
                Toast.makeText(this, "Movie Added", Toast.LENGTH_LONG).show();
            }
        }
    }
}
