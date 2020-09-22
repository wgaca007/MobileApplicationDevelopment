package com.example.midtermtemplate;

import java.io.Serializable;
import java.util.Date;

public class Track implements Serializable {

    String songName;
    String artistName;
    String albumName;
    String date;
//    Double songRating, artistRating;
    String trackUrl;

    public Track() {
    }

    @Override
    public String toString() {
        return "Track{" +
                "songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", date=" + date +
                ", trackUrl='" + trackUrl + '\'' +
                '}';
    }
}
