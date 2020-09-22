package com.example.homework07;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    String title, location,coverphotourl, creator, chatroomid, tripid;
    ArrayList<String> users = new ArrayList<String>();
    ArrayList<String> triprequests = new ArrayList<String>();

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public ArrayList<String> getTriprequests() {
        return triprequests;
    }

    public void setUser(ArrayList<String> users) {
        this.users.addAll(users);
    }

    public Trip(String tripid, String title, String location, String coverphotourl, String creator, String chatroomid) {
        this.tripid = tripid;
        this.title = title;
        this.location = location;
        this.coverphotourl = coverphotourl;
        this.creator = creator;
        this.chatroomid = chatroomid;
    }

    public String getChatroomid() {
        return chatroomid;
    }

    public void setChatroomid(String chatroomid) {
        this.chatroomid = chatroomid;
    }

    public Trip(){

    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getCoverphotourl() {
        return coverphotourl;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCoverphotourl(String coverphotourl) {
        this.coverphotourl = coverphotourl;
    }
}
