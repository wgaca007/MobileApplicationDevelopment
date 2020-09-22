package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/*
NAMES: AKHIL CHUNDARATHIL, RAVI THEJA GOALLA,
GROUP: GROUPS 1 41
ASSIGNMENT: INCLASS11
 */

public class MainActivity extends AppCompatActivity implements MyAdapter.ImageItemClick{

    RecyclerView myrecyclerview;
    private RecyclerView.LayoutManager layoutManager;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String TAG = "demo";

    Button takephoto;
    ProgressBar progressBar;

    Bitmap bitmapUpload = null;


    ArrayList<ImageInfo> imageinfolist = new ArrayList<ImageInfo>();
    MyAdapter myAdapter;
    StorageReference imageRepo;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takephoto = findViewById(R.id.takephoto);
        progressBar = findViewById(R.id.progressBar);

        myrecyclerview = findViewById(R.id.myrecyclerview);

        layoutManager = new LinearLayoutManager(MainActivity.this);
        myrecyclerview.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(imageinfolist, this);
        myrecyclerview.setAdapter(myAdapter);


        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        storageReference.child("images").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                for(final StorageReference reference : task.getResult().getItems()){

                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        String imageurl;
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                imageurl = task.getResult().toString();
                                imageinfolist.add(new ImageInfo(imageurl, reference));
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }

            }
        });




    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Camera Callback........
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //iv_TakePhoto.setImageBitmap(imageBitmap);

            bitmapUpload = imageBitmap;
            uploadImage(bitmapUpload);
        }
    }

    private void uploadImage(Bitmap  photoBitmap) {


        imageRepo = storageReference.child("images/" + UUID.randomUUID()+ ".jpg");

//        Converting the Bitmap into a bytearrayOutputstream....
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Image Download URL" + task.getResult());
                    String imageURL = task.getResult().toString();

                    imageinfolist.add(new ImageInfo(imageURL, imageRepo));
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                System.out.println("Upload is " + progress + "% done");
            }
        });
    }

    @Override
    public void onImageItemClick(final ImageInfo imageInfo) {

        imageInfo.imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(MainActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                imageinfolist.remove(imageInfo);
                myAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("demo", exception.getMessage());
            }
        });
    }
}
