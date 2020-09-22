package com.example.homework7a;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CreateTripActivity extends AppCompatActivity {

    TextView title;
    Button pickup;

    ImageView coverphoto;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatoomsref = db.collection("chatrooms");
    Trip trip;
    Intent i;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    StorageReference coverphotoreference;
    String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        title = findViewById(R.id.tripinfotitle);
        pickup = findViewById(R.id.pickup);
        coverphoto = findViewById(R.id.coverphoto);

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


                coverphotoreference = storageReference.child("images/" + UUID.randomUUID()+ ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable) coverphoto.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = coverphotoreference.putBytes(data);
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
                            trip = new Trip(title.getText().toString(), "Las Vegas", imageURL, currentuserid, chatroomid);
                            trip.setUser(currentuserid);
                            db.collection("trips").document(UUID.randomUUID().toString()).set(trip);
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {

            Uri selectedImage = data.getData();
                coverphoto.setImageURI(selectedImage);

        }
    }


}
