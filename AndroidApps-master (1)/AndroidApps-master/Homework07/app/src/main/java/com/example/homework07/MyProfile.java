package com.example.homework07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    CircularImageView circularImageView;
    EditText firstName,lastName;
    TextView firstNametv,lastNametv;
    Button setImageButton,editButton,saveButton;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference coverphotoreference;
    int currentprogress = 0;
    HashMap<String,Object> userDetails = new HashMap<>();    FirebaseUser user = firebaseAuth.getCurrentUser();
    DocumentReference ref1 =db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        setImageButton = findViewById(R.id.setImageButton);
        editButton = findViewById(R.id.editButton);
        circularImageView = findViewById(R.id.circularImageView);
        firstNametv = findViewById(R.id.firstNametv);
        lastNametv = findViewById(R.id.lastNametv);
        saveButton = findViewById(R.id.saveButton);
        editButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        loadDetails();
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Please Wait !!");
        progressDialog.setMessage("Saving Information..");

    }

    private Bitmap getBitmapFromUrl(String photoUrl) {
        Picasso.get().load(photoUrl).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                circularImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
        return null;
    }

    @Override
    public void onClick(View view) {
if(view.getId()==R.id.editButton) {
    editButton.setVisibility(View.GONE);
    saveButton.setVisibility(View.VISIBLE);
    firstNametv.setVisibility(View.GONE);
    lastNametv.setVisibility(View.GONE);
    firstName.setVisibility(View.VISIBLE);
    lastName.setVisibility(View.VISIBLE);
    setImageButton.setVisibility(View.VISIBLE);
    setImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, 1);
        }
    });
}if(view.getId()==R.id.saveButton) {
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
    progressDialog.show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            coverphotoreference = storageReference.child("images/" + user.getUid() + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) circularImageView.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = coverphotoreference.putBytes(data);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                     double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                     currentprogress = (int) progress;
                     progressDialog.setProgress(currentprogress);

                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        userDetails.put("firstname", firstName.getText().toString().trim());
                        userDetails.put("lastname", lastName.getText().toString().trim());
                        userDetails.put("photourl", imageURL);
                        ref1.update(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                saveButton.setVisibility(View.INVISIBLE);
                                editButton.setVisibility(View.VISIBLE);
                                setImageButton.setVisibility(View.INVISIBLE);
                                firstNametv.setVisibility(View.VISIBLE);
                                lastNametv.setVisibility(View.VISIBLE);
                                firstName.setVisibility(View.GONE);
                                lastName.setVisibility(View.GONE);
                                ref1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        firstNametv.setText(documentSnapshot.get("firstname").toString());
                                        lastNametv.setText(documentSnapshot.get("lastname").toString());
                                        progressDialog.dismiss();
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Toast.makeText(MyProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        finish();
                    } else {
                        Toast.makeText(MyProfile.this, "Profile Not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {

            Uri selectedImage = data.getData();
            circularImageView.setImageURI(selectedImage);

        }
    }

    private void loadDetails() {
        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Bitmap image = null;
                if (task.isSuccessful() && task.getResult() != null) {
                    String givenName = task.getResult().getString("firstname");
                    String familyName = task.getResult().getString("lastname");
                    String photoUrl = task.getResult().getString("photourl");
                    firstName.setVisibility(View.GONE);
                    lastName.setVisibility(View.GONE);
                    firstNametv.setVisibility(View.VISIBLE);
                    lastNametv.setVisibility(View.VISIBLE);
                    if(photoUrl != null) {
                        Picasso.get().load(photoUrl).into(circularImageView);
                        //circularImageView.setImageBitmap(getBitmapFromUrl(photoUrl));
                    }
                    firstNametv.setText(givenName);
                    lastNametv.setText(familyName);
                    firstName.setText(givenName);
                    lastName.setText(familyName);

                }
            }
        });


    }

/*   @Override
    protected void onResume() {
        super.onResume();
        loadDetails();
    }*/

}
