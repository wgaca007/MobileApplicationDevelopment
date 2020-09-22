package com.example.movieapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Movie implements Serializable {

    private String name;
    private String description;
    private String imdb;
    private Integer genre,year,rating;

    public Movie(String name, String description, String imdb, Integer genre, Integer year, Integer rating) {
        this.name = name;
        this.description = description;
        this.imdb = imdb;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
