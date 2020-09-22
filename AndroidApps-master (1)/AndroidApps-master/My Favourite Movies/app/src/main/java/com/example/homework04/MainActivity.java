package com.example.homework04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
Assignment : # HomeWork 04
Group: Groups1 41
NAME: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    private ArrayList<String> movienameslist = new ArrayList<String>();
    private ArrayList<Movie> tempmovieArrayList = new ArrayList<Movie>();
    private int movieindex = -1;
    private Map<Integer, Class> buttonsmap;

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    public static final String MOVIE_OBJ = "Movie";
    public static final String REQUEST_CODE = "requestcode";
    public static final int ADD_MOVIE = 100;
    public static final int EDIT_MOVIE = 101;
    public static final String MOVIE_BY_WHAT = "moviebywhat";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsmap = new HashMap<Integer, Class>(){{
            put(R.id.addmovie, AddMovieActivity.class);
        }};

        findViewById(R.id.addmovie).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.deletemovie).setOnClickListener(this);
        findViewById(R.id.listbyyear).setOnClickListener(this);
        findViewById(R.id.listbyrating).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getExtras() != null) {
            Movie movieobj = data.getParcelableExtra(MOVIE_OBJ);

            if (requestCode == ADD_MOVIE && resultCode == RESULT_OK) {
                movieArrayList.add(movieobj);
                movienameslist.add(movieobj.getName());
            } else if (requestCode == EDIT_MOVIE && resultCode == RESULT_OK) {
                movieArrayList.set(movieindex, movieobj);
                movienameslist.set(movieindex, movieobj.getName());
                movieindex = -1;
            }
        }
        else {
            movieindex = -1;
        }
    }

    @Override
    public void onClick(View v) {

        final int clickedbutton = v.getId();

        if(clickedbutton == R.id.edit || clickedbutton == R.id.deletemovie){
            if(movieArrayList.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Movie");
                builder.setItems(movienameslist.toArray(new CharSequence[movienameslist.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(clickedbutton == R.id.edit) {
                            Intent respectiveintent = new Intent(MainActivity.this, AddMovieActivity.class); //Reusing the Activity
                            respectiveintent.putExtra(MainActivity.MOVIE_OBJ, movieArrayList.get(which));
                            movieindex = which;
                            startActivityForResult(respectiveintent, EDIT_MOVIE);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "The Movie Deleted : " + movienameslist.get(which), Toast.LENGTH_SHORT).show();
                            movieArrayList.remove(which);
                            movienameslist.remove(which);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            else {
                Toast.makeText(MainActivity.this, clickedbutton == R.id.edit ? "No Movie to Edit":"No Movie to Delete", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if(clickedbutton == R.id.listbyyear || clickedbutton == R.id.listbyrating){
            if(movieArrayList.size() > 0) {
                Intent i = new Intent("com.example.homework04.intent.action.VIEW");
                i.addCategory(Intent.CATEGORY_DEFAULT);
                tempmovieArrayList.clear();
                for (Movie m : movieArrayList) {
                    tempmovieArrayList.add(m);
                }
                if (clickedbutton == R.id.listbyyear) {
                    Collections.sort(tempmovieArrayList, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o1.getYear() - o2.getYear();
                        }
                    });
                    i.putExtra(MainActivity.MOVIE_BY_WHAT, "moviebyyear");
                } else {
                    Collections.sort(tempmovieArrayList, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            return o2.getRating() - o1.getRating();
                        }
                    });
                    i.putExtra(MainActivity.MOVIE_BY_WHAT, "moviebyrating");
                }
                i.putParcelableArrayListExtra(MainActivity.MOVIE_OBJ, tempmovieArrayList);
                startActivity(i);
            }
            else {
                Toast.makeText(MainActivity.this, "No Movies to Show", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            Intent respectiveintent = new Intent(this, buttonsmap.get(clickedbutton));
            startActivityForResult(respectiveintent, ADD_MOVIE);
        }
    }

}
