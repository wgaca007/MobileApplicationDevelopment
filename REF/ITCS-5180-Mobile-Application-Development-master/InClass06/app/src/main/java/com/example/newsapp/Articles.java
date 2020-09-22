package com.example.newsapp;

public class Articles {
    String title, description, publishedAt, urlToImage;

    public Articles() {
//        this.title = title;
//        this.description = description;
//        this.publishedAt = publishedAt;
//        this.urlToImage = urlToImage;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }
}
