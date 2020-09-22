package com.example.musicsearchapplication;

import android.util.Log;

import java.io.Serializable;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TrackDetails implements Serializable {
    String TrackName, Genre, Artist, Album, Track_Price, Album_Price, releaseDate, trackViewUrl;

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTrackName() {
        return TrackName;
    }

    public String getTrackViewUrl() {
        return trackViewUrl;
    }

    public void setTrackViewUrl(String trackViewUrl) {
        this.trackViewUrl = trackViewUrl;
    }

    public void setTrackName(String trackName) {
        TrackName = trackName;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getTrack_Price() {
        return Track_Price;
    }

    public void setTrack_Price(String track_Price) {
        Track_Price = track_Price;
    }

    public String getAlbum_Price() {
        return Album_Price;
    }

    public void setAlbum_Price(String album_Price) {
        Album_Price = album_Price;
    }

    public static Comparator<TrackDetails> PriceComparator = new Comparator<TrackDetails>() {
        @Override
        public int compare(TrackDetails o1, TrackDetails o2) {
            Log.d("demo", ""+o1.getTrack_Price() + "-" + o2.getTrack_Price());

            float price1 = Float.parseFloat(o1.getTrack_Price());
            float price2 = Float.parseFloat(o2.getTrack_Price());

            return (int) (price1 - price2);
        }
    };

    public static Comparator<TrackDetails> DateComparator = new Comparator<TrackDetails>() {
        @Override
        public int compare(TrackDetails o1, TrackDetails o2) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date Pdate = null;
            Date Qdate= null;
            try {
                Pdate = df.parse(o1.getReleaseDate());
                Qdate = df.parse(o2.getReleaseDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("demo", "Date 1: " + Pdate);
            Log.d("demo", "Date 2: " + Qdate);
            Log.d("demo", "Comparison: " + Pdate.compareTo(Qdate));
            return Pdate.compareTo(Qdate);
        }
    };
}
