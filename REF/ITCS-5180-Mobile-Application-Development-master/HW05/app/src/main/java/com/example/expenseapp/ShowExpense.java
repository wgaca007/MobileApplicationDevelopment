package com.example.expenseapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ShowExpense extends AppCompatActivity {
    Expense trackDetails = new Expense();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        if (getIntent() != null && getIntent().getExtras() != null) {
            trackDetails = (Expense) getIntent().getExtras().getSerializable(MainActivity.MUSIC_TRACK_KEY);

            TextView textView_Name = findViewById(R.id.tv_ShowName);
            TextView textView_Cost = findViewById(R.id.tv_ShowCost);
            TextView textView_Date = findViewById(R.id.tv_ShowDate);
            ImageView imageView = findViewById(R.id.iv_ShowImage);

            textView_Name.setText(textView_Name.getText() + " " + trackDetails.getExpenseName());
            textView_Cost.setText(textView_Cost.getText() + " " + trackDetails.getExpenseCost());
            textView_Date.setText(textView_Date.getText() + " " + trackDetails.getExpenseDate());

            Bitmap bitmap = decodeFromFirebaseBase64(trackDetails.getImg());
            imageView.setImageBitmap(bitmap);
            //Uri myuri = getImageUri(this, bitmap);
            //Picasso.get().load(myuri).into(imageView);
        }

        Button button_Close = findViewById(R.id.button_Close);
        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
