package com.example.inclass10;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public String name,description,genre, imdb;
    public int rating, year;

    public Movie() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getImdb() {
        return imdb;
    }

    public int getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", imdb='" + imdb + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                '}';
    }

    public Movie(String name, String description, String genre, String imdb, int rating, int year) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.imdb = imdb;
        this.rating = rating;
        this.year = year;
    }


    protected Movie(Parcel in) {
        name = in.readString();
        description = in.readString();
        genre = in.readString();
        imdb = in.readString();
        rating = in.readInt();
        year = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(genre);
        dest.writeString(imdb);
        dest.writeInt(rating);
        dest.writeInt(year);
    }
}
