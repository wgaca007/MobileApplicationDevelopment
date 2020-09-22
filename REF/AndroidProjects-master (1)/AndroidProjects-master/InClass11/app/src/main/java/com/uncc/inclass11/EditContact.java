package com.uncc.inclass11;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EditContact extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth auth;
    EditText editTextFirstName;
    EditText editTextLastName;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("contacts");
    EditText editTextEmail;
    EditText editTextPhone;
    Bitmap imageCapture;
    final public static int IMAGE_CAPTURE = 100;
    final public static int IMAGE_GALLERY = 101;
    private GoogleApiClient mGoogleApiClient;
    ImageView imageView;
    private FirebaseStorage storageUpload = FirebaseStorage.getInstance();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        auth = FirebaseAuth.getInstance();

        String phone = (String) getIntent().getStringExtra("phone");

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstname);
        editTextLastName = (EditText) findViewById(R.id.editTextLastname);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhone = (EditText) findViewById(R.id.phone);
        imageView = (ImageView) findViewById(R.id.avatar);
        Button save = (Button) findViewById(R.id.submitButton);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditContact.this);
                builder.setMessage("Choose the app to be used?")
                        .setCancelable(true)
                        .setPositiveButton("Camera",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {
                                            Intent action = new Intent(
                                                    "android.media.action.IMAGE_CAPTURE");
                                            //action.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                                            startActivityForResult(action, AddContact.IMAGE_CAPTURE);
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton("Gallery",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        try {
                                            ActivityCompat.requestPermissions(EditContact.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                                            Intent photoPickerIntent = new Intent(
                                                    Intent.ACTION_GET_CONTENT);
                                            photoPickerIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                            photoPickerIntent.setType("image/*");
                                            startActivityForResult(photoPickerIntent, AddContact.IMAGE_GALLERY);
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        final Contact[] contact = new Contact[1];
        mRootRef.child("contacts").child(auth.getCurrentUser().getUid()).child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {

                    contact[0] = new Contact();
                    contact[0].setFirst_name(ds.child("first_name").getValue(String.class));
                    contact[0].setLast_name(ds.child("last_name").getValue(String.class));
                    contact[0].setPhone(ds.child("phone").getValue(String.class));
                    contact[0].setEmail(ds.child("email").getValue(String.class));
                    contact[0].setContactImage(ds.child("contactImage").getValue(String.class));
                editTextFirstName.setText(contact[0].getFirst_name());
                editTextLastName.setText(contact[0].getLast_name());
                editTextEmail.setText(contact[0].getLast_name());
                editTextEmail.setEnabled(false);
                editTextPhone.setText(contact[0].getPhone());
                if(contact[0].getContactImage()!=null && !"".equalsIgnoreCase(contact[0].getContactImage()))
                    Picasso.with(EditContact.this).load(contact[0].getContactImage()).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && (imageCapture != null)) {
                    uploadImage();
                }
                else {
                    if (firstName.isEmpty()) {
                        Toast.makeText(EditContact.this, "First name cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (lastName.isEmpty()) {
                        Toast.makeText(EditContact.this, "Last name cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (email.isEmpty()) {
                        Toast.makeText(EditContact.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (phone.isEmpty()) {
                        Toast.makeText(EditContact.this, "Phone cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (imageCapture == null) {
                        Toast.makeText(EditContact.this, "Please provide an image", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            signOut();
            auth.signOut();
            startActivity(new Intent(EditContact.this,MainActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            }
        });
    }

    public String uploadImage()
    {
        final String[] url = {""};
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String path = "users/contacts/"+ editTextEmail.getText().toString()+".png";
        StorageReference firememeref = storageUpload.getReference(path);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("text",editTextFirstName.getText().toString())
                .build();
        UploadTask uploadTask = firememeref.putBytes(data,metadata);

        uploadTask.addOnSuccessListener(EditContact.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri = taskSnapshot.getDownloadUrl();
                url[0] = uri.toString();
                writeNewContact(auth.getUid(),editTextFirstName.getText().toString(),editTextLastName.getText().toString(),editTextPhone.getText().toString(),editTextEmail.getText().toString(),url[0]);
                Toast.makeText(EditContact.this, "Saved Contact",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditContact.this,HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                int errorCode = ((StorageException) exception).getErrorCode();
                String errorMessage = exception.getMessage();

            }
        });
        return  url[0];
    }

    private void writeNewContact(String userId,String first_name, String lastname, String phone, String email, String url) {
        Contact contact = new Contact(first_name,  lastname,  phone,  email,  url);
        mUserRef.child(userId).child(phone).setValue(contact);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    imageCapture = (Bitmap)extras.getParcelable("data");
                    imageView.setImageBitmap(imageCapture);
                }
                break;
            case IMAGE_GALLERY:
                imageCapture = null;
                int flags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageView.setImageBitmap(imageCapture);
                Uri uri = data.getData();
                try {
                    Bitmap photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageCapture = photoBitmap;
                    imageView.setImageBitmap(photoBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                imageCapture = null;
                Toast.makeText(EditContact.this, "You didn't choose and image", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
