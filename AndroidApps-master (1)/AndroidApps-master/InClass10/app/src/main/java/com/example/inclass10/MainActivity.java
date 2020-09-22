package com.example.inclass10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
NAME : AKHIL CHUNDARATHIL
GROUP: GROUPS1 41
ASSIGNMENT: INCLASS10
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    private ArrayList<String> movienameslist = new ArrayList<String>();
    private Map<Integer, Class> buttonsmap;

    public static final String MOVIE_OBJ = "Movie";
    public static final String MOVIE_BY_WHAT = "moviebywhat";
    public static final String TAG = "tag";

    public static final String SELECTED_MOVIE = "selectedmovie";
    public static final String ADD_OR_EDIT = "addoredit";

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public LinkedHashMap<Integer, Integer>idmap = new LinkedHashMap<>();

    static int index = 0;
    int counter;


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
    public void onClick(View v) {

        final int clickedbutton = v.getId();

        if(clickedbutton == R.id.edit || clickedbutton == R.id.deletemovie){

            idmap.clear();
            counter = -1;
            movienameslist.clear();

            db.collection("Movies")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    idmap.put(++counter, Integer.valueOf(document.getId()));
                                    movienameslist.add(document.getString("name"));
                                }

                                    if(idmap.size() > 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Pick a Movie");
                                        builder.setItems(movienameslist.toArray(new String[movienameslist.size()]), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final int selectedindex = which;

                                                if(clickedbutton == R.id.edit) {
                                                    Intent respectiveintent = new Intent(MainActivity.this, AddMovieActivity.class); //Reusing the Activity
                                                    respectiveintent.putExtra(MainActivity.SELECTED_MOVIE, idmap.get(which));
                                                    respectiveintent.putExtra(MainActivity.ADD_OR_EDIT, "EDIT");
                                                    startActivity(respectiveintent);
                                                }
                                                else {
                                                    MainActivity.db.collection("Movies").document(String.valueOf(idmap.get(which)))
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(MainActivity.this, "Deleted Movie : " + movienameslist.get(selectedindex), Toast.LENGTH_SHORT).show();
                                                                    Log.d(MainActivity.TAG, "DocumentSnapshot successfully written!");

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(MainActivity.this, "Error deleting the movie", Toast.LENGTH_SHORT).show();
                                                                    Log.w(MainActivity.TAG, "Error writing document", e);
                                                                }
                                                            });

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
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }
        else if(clickedbutton == R.id.listbyyear || clickedbutton == R.id.listbyrating){
            movieArrayList.clear();
            MainActivity.db.collection("Movies")
                    .orderBy(clickedbutton == R.id.listbyyear ? "year" : "rating", clickedbutton == R.id.listbyyear ? Query.Direction.ASCENDING : Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    movieArrayList.add(document.toObject(Movie.class));
                                }
                                if(movieArrayList.size() > 0){
                                    Intent i = new Intent("com.example.inclass10.intent.action.VIEW");
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    if(clickedbutton == R.id.listbyyear){
                                        i.putExtra(MainActivity.MOVIE_BY_WHAT, "moviebyyear");
                                    }
                                    else{
                                        i.putExtra(MainActivity.MOVIE_BY_WHAT, "moviebyrating");
                                    }
                                    i.putParcelableArrayListExtra(MainActivity.MOVIE_OBJ, movieArrayList);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "No Movies to Show", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Log.d(MainActivity.TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        else {
            Intent respectiveintent = new Intent(this, buttonsmap.get(clickedbutton));
            respectiveintent.putExtra(MainActivity.ADD_OR_EDIT, "ADD");
            startActivity(respectiveintent);
        }
    }

}
