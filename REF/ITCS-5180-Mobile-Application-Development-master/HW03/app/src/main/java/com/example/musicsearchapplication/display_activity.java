package com.example.musicsearchapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class display_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null && getIntent().getExtras() != null) {
            TrackDetails trackDetails = (TrackDetails) getIntent().getExtras().getSerializable(MainActivity.MUSIC_TRACK_KEY);

            TextView textView_trackvalue = findViewById(R.id.textView_TrackValue1);
            TextView textView_genre = findViewById(R.id.textView_GenreValue);
            TextView textView_artist = findViewById(R.id.textView_ArtistValue);
            TextView textView_album = findViewById(R.id.textView_AlbumValue);
            TextView textView_trackprice = findViewById(R.id.textView_TrackPrizeValue);
            TextView textView_albumprice = findViewById(R.id.textView_AlbumPrizeValue);

            textView_trackvalue.setText(trackDetails.getTrackName());
            textView_genre.setText(trackDetails.getGenre());
            textView_artist.setText(trackDetails.getArtist());
            textView_album.setText(trackDetails.getAlbum());
            textView_trackprice.setText(trackDetails.getTrack_Price());
            textView_albumprice.setText(trackDetails.getAlbum_Price());

            Log.d("demo", trackDetails.getTrackViewUrl());
            Picasso.get().load(trackDetails.getTrackViewUrl()).resize(100,100).into((ImageView) findViewById(R.id.imageView2));
        }

        findViewById(R.id.button_Finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
