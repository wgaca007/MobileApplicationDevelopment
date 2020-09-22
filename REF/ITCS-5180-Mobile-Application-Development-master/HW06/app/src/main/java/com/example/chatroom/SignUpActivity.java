package com.example.chatroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private static final String MY_TAG = "Sign Up";

    FirebaseAuth firebaseAuth;
    User user = new User();

    EditText emailField;
    EditText passwordField;
    EditText cPasswordField;
    EditText firstName;
    EditText lastName;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = "";
        if (firebaseUser != null) {
            // intent to contact list
            userId = firebaseUser.getUid();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra(MainActivity.MY_KEY, userId);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.editTextSugnUpEmail);
        passwordField = findViewById(R.id.editTextSignUpPassword1);
        cPasswordField = findViewById(R.id.editTextSignUpPassword2);
        firstName = findViewById(R.id.editTextSignUpFirstName);
        lastName = findViewById(R.id.editTextSignUpLastName);
        Button signUp = findViewById(R.id.buttonSignUpSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() && (passwordField.getText().toString().compareTo(cPasswordField.getText().toString()) == 0)) {
                    firebaseAuth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user1 = firebaseAuth.getCurrentUser();
                                        user.firstName = firstName.getText().toString();
                                        user.lastName = lastName.getText().toString();
                                        String userId = user1.getUid();
                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                        databaseReference.child(userId).setValue(user);
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        intent.putExtra(MainActivity.MY_KEY, userId);
                                        startActivity(intent);
                                        Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.w("", "failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

        Button cancel = findViewById(R.id.buttonSignUpCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isValidForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}
