package com.example.homework04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
Assignment : # In Class10

NAME: RAVI THEJA GOALLA
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    private ArrayList<String> movienameslist = new ArrayList<String>();
    private ArrayList<Movie> tempmovieArrayList = new ArrayList<Movie>();
    private int movieindex = -1;
     int count;
     static int index=0;
    public static final String ADD = "add";
    private Map<Integer, Class> buttonsmap;
    public LinkedHashMap<Integer, Integer> mapping = new LinkedHashMap<>();
    private DocumentReference docRef;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

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

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getExtras() != null) {
            final Movie movieobj = data.getParcelableExtra(MOVIE_OBJ);

            if (requestCode == ADD_MOVIE && resultCode == RESULT_OK) {
                count=count+1;
                DocumentReference docRef1 = FirebaseFirestore.getInstance().collection("Movies").document();
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            count=Integer.valueOf(String.valueOf(task.getResult().getId().indexOf(1)));
                            count=count+1;

                            docRef.set(movieobj.toHashMap());
                            Toast.makeText(MainActivity.this, "Movie Added", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("demo", "get failed with ", task.getException());
                        }
                    }
                });
            } else if (requestCode == EDIT_MOVIE && resultCode == RESULT_OK) {
                docRef = FirebaseFirestore.getInstance().collection("Movies").document(""+mapping.get(0));
                docRef.update(movieobj.toHashMap()).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(MainActivity.this, "Movie Edited", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Movie Update failed", Toast.LENGTH_SHORT).show();
                    }
                });
                movieArrayList.clear();
                movienameslist.clear();

               /* movieArrayList.set(movieindex, movieobj);
                movienameslist.set(movieindex, movieobj.getName());
                movieindex = -1;
            }
        }
        else {
            movieindex = -1;
        }
    }*/

    @Override
    public void onClick(View v) {

        final int clickedbutton = v.getId();
        mapping.clear();
        count=-1;

        if(clickedbutton == R.id.edit || clickedbutton == R.id.deletemovie){
            movieArrayList.clear();
            movienameslist.clear();
            db.collection("Movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mapping.put(++count, Integer.valueOf(document.getId()));
                                movienameslist.add(document.getString("name"));
                            }
                        if(mapping .size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Pick a Movie");
                            builder.setItems(movienameslist.toArray(new CharSequence[movienameslist.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final int Sindex=which;
                                    if(clickedbutton == R.id.edit) {
                                        Intent respectiveintent = new Intent(MainActivity.this, AddMovieActivity.class); //Reusing the Activity
                                        respectiveintent.putExtra(MainActivity.MOVIE_OBJ, mapping.get(which));
                                        respectiveintent.putExtra(MainActivity.ADD, "EDIT");
                                       // mapping.put(0, String.valueOf(queryDocumentSnapshots.getDocuments().get(which)));
                                        startActivity(respectiveintent);
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Deleted Movie : " + movienameslist.get(Sindex), Toast.LENGTH_SHORT).show();

                                        MainActivity.db.collection("Movies").document(String.valueOf(mapping.get(which)))
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                              //  Toast.makeText(MainActivity.this, "Deleted Movie : " + movienameslist.get(Sindex), Toast.LENGTH_SHORT).show();
                                                Log.d("Delete", "Movie Deleted");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Error deleting the movie", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        movieArrayList.clear();
                                        movienameslist.clear();
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
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }

                } });

        /*    if(movienameslist.size() > 0) {
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
            }*/
        }
        else if(clickedbutton == R.id.listbyyear || clickedbutton == R.id.listbyrating) {
            movieArrayList.clear();
            final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
            mFirestore.collection("Movies").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d("demo", "onSuccess: LIST EMPTY");
                        Toast.makeText(MainActivity.this, "No Movies to Show", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //movieArrayList.clear();
                        List<Movie> types = queryDocumentSnapshots.toObjects(Movie.class);

                        // Add all to your list
                        movieArrayList.addAll(types);
                        // movienameslist.clear();
                        for (int i = 0; i < movieArrayList.size(); i++) {
                            movienameslist.add(movieArrayList.get(i).getName().toString());
                        }
                        Log.d("demo", "onSuccess: " + movieArrayList);
                        Log.d("demo", "movieNames: " + movienameslist);
                        if (movieArrayList.size() > 0) {
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
                        } else {
                            Toast.makeText(MainActivity.this, "No Movies to Show", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            });
        }
        else {
                        Intent respectiveintent = new Intent(this, buttonsmap.get(clickedbutton));
            respectiveintent.putExtra(MainActivity.ADD, "ADD");
                        startActivity(respectiveintent);
                    }
                }

            }
