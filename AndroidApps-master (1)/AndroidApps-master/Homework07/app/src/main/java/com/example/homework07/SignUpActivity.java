package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText firstName,lastName,email,password,confirmPassword;
    private Button signUp,cancel;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
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

        setTitle("Trip Planner");

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

                            /*userDetails.put("First_Name",firstname.getText().toString().trim());
                            userDetails.put("Last_Name",lastname.getText().toString().trim());
                            userDetails.put("Email",user.getEmail());*/
                            ref1.set(new User(firstName.getText().toString().trim(), lastName.getText().toString().trim(), user.getEmail(), userID, null)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                        userProfile();
                    }
                });
    }

    private void userProfile()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName.getText().toString().trim())
                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))  // here you can set image link also.
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile updated.");
                            }
                        }
                    });
        }
    }

}
