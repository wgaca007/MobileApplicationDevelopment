package com.example.expenseapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Calendar;

public class EditExpense extends AppCompatActivity {
    public String date;
    Expense trackDetails = new Expense();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView textViewDate = findViewById(R.id.tvDate);
        Button pickDate = (Button) findViewById(R.id.buttonPickDateEdit);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = month + "/" + dayOfMonth + "/" + year;
                        Log.d("demo", date);
                        textViewDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            trackDetails = (Expense) getIntent().getExtras().getSerializable(MainActivity.MUSIC_TRACK_KEY);

            EditText editTextName = findViewById(R.id.etNameEdit);
            EditText cost = findViewById(R.id.etCostEdit);
            TextView textViewDateEdit = findViewById(R.id.tvEditDate);
            ImageView imageViewEdit = findViewById(R.id.IVEditExpense);

            editTextName.setText(trackDetails.getExpenseName());
            cost.setText(trackDetails.getExpenseCost().toString());
            textViewDateEdit.setText(trackDetails.getExpenseDate());

            date = trackDetails.getExpenseDate();

            Bitmap bitmap = decodeFromFirebaseBase64(trackDetails.getImg());
            imageViewEdit.setImageBitmap(bitmap);
            //Uri myuri = getImageUri(getBaseContext(), bitmap);
            //Picasso.get().load(myuri).into(imageViewEdit);
            //imageViewEdit.setImageBitmap(decodeFromFirebaseBase64(trackDetails.getImg()));
        }

        Button addExpenseEdit = findViewById(R.id.buttonAddExpenseEdit);
        addExpenseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = findViewById(R.id.etNameEdit);
                EditText cost = findViewById(R.id.etCostEdit);
                TextView textViewDateEdit = findViewById(R.id.tvEditDate);
                ImageView imageViewEdit = findViewById(R.id.IVEditExpense);

                if (!editTextName.getText().toString().isEmpty() && !cost.getText().toString().isEmpty() &&
                        !textViewDateEdit.getText().toString().isEmpty()) {
                    trackDetails.setExpenseName(editTextName.getText().toString());
                    trackDetails.setExpenseCost(Double.parseDouble(cost.getText().toString()));
                    trackDetails.setExpenseDate(date);

                    DatabaseReference ref2 = mDatabase.child("users");
                    String key = trackDetails.getId();
                    ref2.child(key).setValue(trackDetails);
                    finish();
                }
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        //expense.setImg(imageString);
        Log.d("demo", imageString);
    }
}
