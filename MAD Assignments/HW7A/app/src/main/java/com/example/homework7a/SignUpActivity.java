package com.example.homework7a;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText firstName,lastName,email,password,confirmPassword;
    private Button signUp,cancel;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference coverphotoreference;
    String imageURL = null;
    HashMap<String,Object> userDetails = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName=findViewById(R.id.editTextFirstname);
        lastName=findViewById(R.id.editTextLastname);
        email=findViewById(R.id.editTextEmail);
        password=findViewById(R.id.editTextPassword);
        confirmPassword=findViewById(R.id.editTextConfirmPassword);
        cancel=findViewById(R.id.buttonCancel);
        signUp=findViewById(R.id.buttonSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
       db = FirebaseFirestore.getInstance();
        signUp.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.buttonCancel){
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            finishAffinity();
        }else{
            String getFirstName = firstName.getText().toString().trim();
            String getLastName = lastName.getText().toString().trim();
            String getEmail = email.getText().toString().trim();
            String getPassword = password.getText().toString().trim();
            String getConfirmPassword = confirmPassword.getText().toString().trim();

            if(getFirstName.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Type First Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(getLastName.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Type Last Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(getEmail.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Type Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
                Toast.makeText(SignUpActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if(getPassword.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Type Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(getConfirmPassword.isEmpty()){
                Toast.makeText(SignUpActivity.this, "Confirm Your Password ", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!getPassword.equals(getConfirmPassword)){
                Toast.makeText(SignUpActivity.this, "Passwords Do not match ", Toast.LENGTH_SHORT).show();
                return;
            }
            if(getPassword.length()<6){
                Toast.makeText(SignUpActivity.this, "Password should consist of minimum 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            callSignUp(getEmail,getPassword);
        }

    }
    private void callSignUp(String email,String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Signed up Failed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //String UserID=user.getEmail().replace("@","").replace(".","");
                            String userID = user.getUid();
                            DocumentReference ref1= db.collection("Users").document(userID);

                            userDetails.put("GivenName",firstName.getText().toString().trim());
                            userDetails.put("FamilyName",lastName.getText().toString().trim());
                            userDetails.put("Email",user.getEmail());
                            userDetails.put("PhotoUrl", getPicture());
                            Log.d("demo",""+getPicture());
                            ref1.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Log.d("TESTING", "Sign up Successful" + task.isSuccessful());
                                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                    Toast.makeText(SignUpActivity.this, "Account Created ", Toast.LENGTH_SHORT).show();
                                    Log.d("TESTING", "Created Account");
                                    finish();
                                }
                            });
                        }
                    }
                });
    }
    private String getPicture(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        coverphotoreference = storageReference.child("images/" + user.getUid() + ".jpg");
        /*Uri imageUri = Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/"+R.drawable.anonymous);
        UploadTask uploadTask;

        uploadTask = coverphotoreference.putFile(imageUri);*/
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.anonymous);
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = coverphotoreference.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
               // return null;
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
                     imageURL = task.getResult().toString();
                }
            }
        });
        return imageURL;
    }

}
