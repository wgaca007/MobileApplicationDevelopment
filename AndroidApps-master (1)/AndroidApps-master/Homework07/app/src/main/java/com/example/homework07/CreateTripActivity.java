package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CreateTripActivity extends AppCompatActivity implements UsersFragment.OnListFragmentInteractionListener{

    EditText title, citysearch;
    Button pickup;

    ImageView coverphoto;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatoomsref = db.collection("chatrooms");
    Trip trip;
    Intent i;
    Uri selectedImageuri;
    UsersFragment usersfragment;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    StorageReference coverphotoreference;
    String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<String> users = new ArrayList<String>(){{
        add(currentuserid);
    }};

    AlertDialog.Builder builder;
    String selectedcity = null;
    ArrayList<String> citylist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        title = findViewById(R.id.tripinfotitle);
        pickup = findViewById(R.id.pickup);
        coverphoto = findViewById(R.id.coverphoto);
        citysearch = findViewById(R.id.citysearch);

        builder = new AlertDialog.Builder(CreateTripActivity.this);
        builder.setTitle("Select the City");

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = ((EditText) findViewById(R.id.citysearch)).getText().toString();
                new GetCity().execute(place);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.usersframelayout, new UsersFragment()).commit();
        //usersfragment = (UsersFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        //usersfragment.setCreateTrip(true);

        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedcity == null){
                    Toast.makeText(CreateTripActivity.this, "Select the City", Toast.LENGTH_SHORT).show();
                    return;
                }

                coverphotoreference = storageReference.child("images/" + UUID.randomUUID()+ ".jpg");
                /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable) coverphoto.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
*/
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageuri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                UploadTask uploadTask = coverphotoreference.putStream(inputStream);
                //UploadTask uploadTask = coverphotoreference.putBytes(data);
                //db.collection("trips").document(UUID.randomUUID().toString())

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return coverphotoreference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Image Download URL" + task.getResult());
                            String imageURL = task.getResult().toString();
                            String chatroomid = UUID.randomUUID().toString();
                            String tripid = UUID.randomUUID().toString();
                            trip = new Trip(tripid, title.getText().toString(), selectedcity, imageURL, currentuserid, chatroomid);
                            trip.setUser(users);
                            db.collection("trips").document(tripid).set(trip);
                            for(String userid : users){
                                db.collection("Users").document(userid).update("trips", FieldValue.arrayUnion(tripid));
                            }
                            finish();
                        }
                    }
                });
            }
        });

        setTitle("Create Trip");
    }

    protected class GetCity extends AsyncTask<String, Void, ArrayList<String>> {

        HttpURLConnection connection = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            builder.setItems(s.toArray(new String[s.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedcity = citylist.get(which);
                    citysearch.setText(selectedcity);
                }
            });
            builder.show();

        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAlxnZIyWkn46Cr9CgPRGdOR82U8AucPYw&type=(cities)&input=" + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray predictionsarray = root.getJSONArray("predictions");
                    for (int i=0;i<predictionsarray.length();i++) {
                        JSONObject jsonObject = predictionsarray.getJSONObject(i);
                        String description = jsonObject.getString("description");
                        citylist.add(description);

                        //citylist.add(new City(jsonObject.getString("place_id"), description, ""));
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return citylist;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {

            selectedImageuri = data.getData();
            coverphoto.setImageURI(selectedImageuri);

        }
    }


    @Override
    public void onListFragmentInteraction(User item) {
        //item.setSelected(!model.isSelected());
        users.add(item.userId);
    }
}
