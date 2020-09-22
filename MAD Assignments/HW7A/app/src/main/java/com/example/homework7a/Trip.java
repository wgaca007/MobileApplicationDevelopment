package com.example.homework7a;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    String title, location,coverphotourl, userid, chatroomid;
    ArrayList<String> users = new ArrayList<String>();
    ArrayList<String> triprequests = new ArrayList<String>();


    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUser(String user) {
        this.users.add(user);
    }

    public Trip(String title, String location, String coverphotourl, String userid, String chatroomid) {
        this.title = title;
        this.location = location;
        this.coverphotourl = coverphotourl;
        this.userid = userid;
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

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
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
